package org.araqnid.kotlin.setawsssocredentials

import js.core.Object
import kotlinx.coroutines.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import node.ErrnoException
import node.WritableStream
import node.buffer.BufferEncoding
import node.events.Event
import node.fs.readFile
import node.os.EOL
import node.process.process
import org.araqnid.kotlin.setawsssocredentials.aws.fixedCredentials
import org.araqnid.kotlin.setawsssocredentials.aws.loadSharedConfigFiles
import org.araqnid.kotlin.setawsssocredentials.aws.sso.*
import org.araqnid.kotlin.setawsssocredentials.aws.sts.*
import org.araqnid.kotlin.setawsssocredentials.aws.use
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.js.Date

@Serializable
private data class CredentialsCacheFile(val accessToken: String)

private val json = Json { ignoreUnknownKeys = true }

private suspend fun loadAccessToken(): String? {
    try {
        val credentialsCacheFile = "${process.env["HOME"]}/.aws/sso/cache/6cd2b2dcd05b0cd585381193b0b81dbf3e62d5b2.json"
        val fileJson = json.decodeFromString<CredentialsCacheFile>(
            readFile(
                credentialsCacheFile,
                BufferEncoding.utf8
            )
        )
        return fileJson.accessToken
    } catch (err: Throwable) {
        if (err.unsafeCast<ErrnoException>().code == "ENOENT") {
            return null
        }
        throw err
    }
}

private suspend fun WritableStream.writeFully(str: String) {
    suspendCoroutine { cont ->
        if (write(str)) {
            cont.resume(Unit)
        } else {
            once(Event.DRAIN) {
                cont.resume(Unit)
            }
        }
    }
}

private suspend fun printlnStderr(str: String) {
    process.stderr.unsafeCast<WritableStream>().writeFully(str + EOL)
}

private suspend fun attemptSSOLogin(profileName: String) {
    command("aws", "--profile", profileName, "sso", "login").collect { output ->
        when (output) {
            is CommandOutput.Stdout ->
                printlnStderr("aws sso login: ${output.text}")

            is CommandOutput.Stderr ->
                printlnStderr("aws sso login:(stderr): ${output.text}")

            is CommandOutput.Exit ->
                if (output.exitCode > 0)
                    error("aws command terminated with exit code ${output.exitCode}")
        }
    }
}

private suspend fun getRoleCredentialsPossiblyLogin(
    sso: SSO,
    accountId: String,
    roleName: String,
    profileName: String
): GetRoleCredentialsCommandOutput {
    val accessToken = loadAccessToken()
    if (accessToken != null) {
        return try {
            sso.getRoleCredentials {
                this.accountId = accountId
                this.roleName = roleName
                this.accessToken = accessToken
            }
        } catch (err: UnauthorizedException) {
            attemptSSOLogin(profileName)
            val newAccessToken = loadAccessToken() ?: error("No access token after SSO login")
            sso.getRoleCredentials {
                this.accountId = accountId
                this.roleName = roleName
                this.accessToken = newAccessToken
            }
        }
    }
    attemptSSOLogin(profileName)
    val newAccessToken = loadAccessToken() ?: error("No access token after SSO login")
    return sso.getRoleCredentials {
        this.accountId = accountId
        this.roleName = roleName
        this.accessToken = newAccessToken
    }
}

private suspend fun listProfiles() {
    val sharedConfig = loadSharedConfigFiles().await()
    for ((name, value) in Object.entries(sharedConfig.configFile)) {
        if (value["sso_start_url"] != null) {
            println(name)
        }
    }
}

private data class ExportableCredentials(
    val accessKeyId: String,
    val secretAccessKey: String,
    val sessionToken: String,
    val defaultRegion: String
)

private fun export(credentials: ExportableCredentials) {
    println("AWS_ACCESS_KEY_ID=${credentials.accessKeyId};")
    println("AWS_SECRET_ACCESS_KEY=${credentials.secretAccessKey};")
    println("AWS_SESSION_TOKEN=${credentials.sessionToken};")
    println("AWS_DEFAULT_REGION=${credentials.defaultRegion};")
    println("export AWS_ACCESS_KEY_ID AWS_SECRET_ACCESS_KEY AWS_SESSION_TOKEN AWS_DEFAULT_REGION;")
}

private fun RoleCredentials.toExportable(defaultRegion: String) = ExportableCredentials(
    accessKeyId = accessKeyId!!,
    secretAccessKey = secretAccessKey!!,
    sessionToken = sessionToken!!,
    defaultRegion = defaultRegion
)

private fun Credentials.toExportable(defaultRegion: String) = ExportableCredentials(
    accessKeyId = accessKeyId!!,
    secretAccessKey = secretAccessKey!!,
    sessionToken = sessionToken!!,
    defaultRegion = defaultRegion
)

private suspend fun withProfileDefaultRole(profile: String, block: suspend (region: String, roleCredentials: RoleCredentials, sts: STS) -> Unit) {
    val sharedConfig = loadSharedConfigFiles().await()
    val ssoConfig = sharedConfig.configFile[profile] ?: error("No such profile in \$HOME/.aws/config: $profile")
    val region = ssoConfig["sso_region"]!!
    val accountId = ssoConfig["sso_account_id"]!!
    val roleName = ssoConfig["sso_role_name"]!!
    createSSO(region = region, defaultsMode = "standard").use { sso ->
        val response = getRoleCredentialsPossiblyLogin(sso, accountId, roleName, profile)

        response.roleCredentials?.let { roleCredentials ->
            val expirationDate = roleCredentials.expiration?.let { epochMillis -> Date(epochMillis) }

            createSTS(
                region = region,
                defaultsMode = "standard",
                credentialDefaultProvider = {
                    fixedCredentials(
                        roleCredentials.accessKeyId!!,
                        roleCredentials.secretAccessKey!!,
                        roleCredentials.sessionToken,
                        expirationDate
                    )
                }
            ).use { sts ->
                block(region, roleCredentials, sts)
            }
        }
    }
}

private suspend fun assumeProfileDefaultRole(profile: String) {
    withProfileDefaultRole(profile) { region, roleCredentials, sts ->
        val expirationDate = roleCredentials.expiration?.let { epochMillis -> Date(epochMillis) }
        val callerIdentity = sts.getCallerIdentity { }
        printlnStderr("As ${callerIdentity.arn} until $expirationDate")
        export(roleCredentials.toExportable(region))
    }
}

private suspend fun assumeProfileSpecifiedRole(profile: String, targetRole: String, sessionName: String) {
    withProfileDefaultRole(profile) { region, _, sts ->
        val callerIdentity = sts.getCallerIdentity { }
        printlnStderr("Via ${callerIdentity.arn}")
        val assumed = sts.assumeRole {
            this.roleArn = targetRole
            this.roleSessionName = sessionName
        }
        val secondExpirationDate = assumed.credentials!!.expiration
        printlnStderr("As ${assumed.assumedRoleUser!!.arn} until $secondExpirationDate")
        export(assumed.credentials!!.toExportable(region))
    }
}

fun main() = runScript {
    val args = process.argv.drop(2)
    when (args.size) {
        0 -> listProfiles()
        1 -> assumeProfileDefaultRole(args[0])
        3 -> assumeProfileSpecifiedRole(args[0], args[1], args[2])
        else -> error(
            """
            Syntax: ${process.argv.slice(0..1).joinToString(" ")}
            Syntax: ${process.argv.slice(0..1).joinToString(" ")} profile-name
            Syntax: ${process.argv.slice(0..1).joinToString(" ")} profile-name target-role session-name
        """.trimIndent()
        )
    }
}
