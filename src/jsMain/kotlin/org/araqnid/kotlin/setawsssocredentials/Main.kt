package org.araqnid.kotlin.setawsssocredentials

import js.objects.Object
import js.objects.jso
import kotlinx.coroutines.await
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import node.ErrnoException
import node.WritableStream
import node.buffer.BufferEncoding
import node.fs.readFile
import node.os.EOL
import node.process.process
import node.stream.WritableEvent
import org.araqnid.kotlin.setawsssocredentials.aws.TokenProviderError
import org.araqnid.kotlin.setawsssocredentials.aws.fixedCredentials
import org.araqnid.kotlin.setawsssocredentials.aws.loadSharedConfigFiles
import org.araqnid.kotlin.setawsssocredentials.aws.sso.*
import org.araqnid.kotlin.setawsssocredentials.aws.sts.*
import org.araqnid.kotlin.setawsssocredentials.aws.use
import kotlin.coroutines.resume
import kotlin.js.Date
import kotlin.time.toKotlinInstant
import org.araqnid.kotlin.setawsssocredentials.aws.fromSso as getSsoTokenProvider

@Serializable
private data class CredentialsCacheFile(val accessToken: String)

private val json = Json { ignoreUnknownKeys = true }

private val ssoCacheDir = "${process.env["HOME"]}/.aws/sso/cache"

private data class SSOProfileConfig(
    val profileName: String,
    val sessionName: String?,
    val startUrl: String,
    val region: String
)

private suspend fun loadAccessToken(ssoProfileConfig: SSOProfileConfig): String? {
    if (ssoProfileConfig.sessionName != null) {
        val tokenProvider = getSsoTokenProvider(jso {
            profile = ssoProfileConfig.profileName
        })
        val tokenIdentity = try {
            tokenProvider().await().also { tokenIdentity ->
                printlnStderr("SSO token expires at ${tokenIdentity.expiration?.toKotlinInstant()}")
            }
        } catch (_: TokenProviderError) {
            null
        }
        return tokenIdentity?.token
    }
    try {
        val cacheKey = sha1(ssoProfileConfig.sessionName ?: ssoProfileConfig.startUrl)
        val credentialsCacheFile = "$ssoCacheDir/${cacheKey}.json"
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
    suspendCancellableCoroutine { cont ->
        if (write(str)) {
            cont.resume(Unit)
        } else {
            once(WritableEvent.DRAIN) {
                cont.resume(Unit)
            }
        }
    }
}

private suspend fun printlnStderr(str: String) {
    process.stderr.writeFully(str + EOL)
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
    profileName: String,
    ssoProfileConfig: SSOProfileConfig,
): GetRoleCredentialsCommandOutput {
    val accessToken = loadAccessToken(ssoProfileConfig)
    if (accessToken != null) {
        return try {
            sso.getRoleCredentials {
                this.accountId = accountId
                this.roleName = roleName
                this.accessToken = accessToken
            }
        } catch (_: UnauthorizedException) {
            attemptSSOLogin(profileName)
            val newAccessToken = loadAccessToken(ssoProfileConfig) ?: error("No access token after SSO login")
            sso.getRoleCredentials {
                this.accountId = accountId
                this.roleName = roleName
                this.accessToken = newAccessToken
            }
        }
    }
    attemptSSOLogin(profileName)
    val newAccessToken = loadAccessToken(ssoProfileConfig) ?: error("No access token after SSO login")
    return sso.getRoleCredentials {
        this.accountId = accountId
        this.roleName = roleName
        this.accessToken = newAccessToken
    }
}

private suspend fun listProfiles() {
    val sharedConfig = loadSharedConfigFiles().await()
    for ((name, value) in Object.entries(sharedConfig.configFile)) {
        if (value["sso_session"] != null) {
            println(name)
        } else if (value["sso_start_url"] != null && !name.startsWith("sso-session.")) {
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

private suspend fun withProfileDefaultRole(
    profile: String,
    block: suspend (region: String, roleCredentials: RoleCredentials, sts: STS) -> Unit
) {
    val sharedConfig = loadSharedConfigFiles().await()
    val profileSection = sharedConfig.configFile[profile] ?: error($$"No such profile in $HOME/.aws/config: $$profile")
    val ssoSession = profileSection["sso_session"]
    val ssoProfileConfig = run {
        if (ssoSession != null) {
            val sessionSection = sharedConfig.configFile["sso-session.${ssoSession}"]
                ?: error("Profile \"$profile\" refers to unknown SSO session \"$ssoSession\"")
            SSOProfileConfig(
                profileName = profile,
                sessionName = ssoSession,
                region = sessionSection["sso_region"] ?: error("SSO session \"$ssoSession\" does not have sso_region"),
                startUrl = sessionSection["sso_start_url"]
                    ?: error("SSO session \"$ssoSession\" does not have sso_start_url"),
            )
        } else {
            SSOProfileConfig(
                profileName = profile,
                sessionName = null,
                region = profileSection["sso_region"]
                    ?: error("Profile \"$profile\" does not have sso_session nor sso_region"),
                startUrl = profileSection["sso_start_url"] ?: error("Profile \"$profile\" does not have sso_start_url"),
            )
        }
    }
    val accountId = profileSection["sso_account_id"] ?: error("Profile \"$profile\" does not have sso_account_id")
    val roleName = profileSection["sso_role_name"] ?: error("Profile \"$profile\" does not have sso_role_name")
    createSSO(region = ssoProfileConfig.region, defaultsMode = "standard").use { sso ->
        val response = getRoleCredentialsPossiblyLogin(sso, accountId, roleName, profile, ssoProfileConfig)

        response.roleCredentials!!.let { roleCredentials ->
            val expirationDate = roleCredentials.expiration?.let { epochMillis -> Date(epochMillis) }

            createSTS(
                region = ssoProfileConfig.region,
                defaultsMode = "standard",
                credentialsProvider = {
                    fixedCredentials(
                        roleCredentials.accessKeyId!!,
                        roleCredentials.secretAccessKey!!,
                        roleCredentials.sessionToken,
                        expirationDate
                    )
                }
            ).use { sts ->
                block(ssoProfileConfig.region, roleCredentials, sts)
            }
        }
    }
}

private suspend fun assumeProfileDefaultRole(profile: String) {
    withProfileDefaultRole(profile) { region, roleCredentials, sts ->
        val expirationDate = roleCredentials.expiration?.let { epochMillis -> Date(epochMillis) }
        val callerIdentity = sts.getCallerIdentity { }
        printlnStderr("As ${callerIdentity.arn} until ${expirationDate?.toKotlinInstant()}")
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
        printlnStderr("As ${assumed.assumedRoleUser!!.arn} until ${secondExpirationDate?.toKotlinInstant()}")
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
