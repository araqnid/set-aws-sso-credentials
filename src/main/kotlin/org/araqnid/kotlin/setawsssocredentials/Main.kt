package org.araqnid.kotlin.setawsssocredentials

import js.core.Record
import js.core.get
import kotlinx.coroutines.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import node.WritableStream
import node.buffer.BufferEncoding
import node.fs.readFile
import node.process.process
import org.araqnid.kotlin.setawsssocredentials.aws.loadSharedConfigFiles
import org.araqnid.kotlin.setawsssocredentials.aws.sso.*
import org.araqnid.kotlin.setawsssocredentials.aws.use
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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
        if (err.asDynamic().code == "ENOENT") {
            return null
        }
        throw err
    }
}

private suspend fun WritableStream.writeFully(str: String) {
    suspendCoroutine { cont ->
        write(str) { err ->
            if (err == null) {
                cont.resume(Unit)
            } else {
                cont.resumeWithException(err.unsafeCast<Throwable>())
            }
        }
    }
}

private suspend fun attemptSSOLogin(profileName: String) {
    command("aws", "--profile", profileName, "sso", "login").collect { output ->
        when (output) {
            is CommandOutput.Stdout ->
                process.stderr.unsafeCast<WritableStream>().writeFully("aws sso login: ${output.text}\n")

            is CommandOutput.Stderr ->
                process.stderr.unsafeCast<WritableStream>().writeFully("aws sso login:(stderr): ${output.text}\n")

            is CommandOutput.Exit ->
                if (output.exitCode > 0)
                    error("aws command terminated with exit code ${output.exitCode}")
        }
    }
}

private suspend fun getRoleCredentialsPossiblyLogin(
    ssoClient: SSOClient,
    accountId: String,
    roleName: String,
    profileName: String
): GetRoleCredentialsCommandOutput {
    val accessToken = loadAccessToken()
    if (accessToken != null) {
        return try {
            ssoClient.getRoleCredentials(
                accountId = accountId,
                roleName = roleName,
                accessToken = accessToken
            )
        } catch (err: UnauthorizedException) {
            attemptSSOLogin(profileName)
            val newAccessToken = loadAccessToken() ?: error("No access token after SSO login")
            ssoClient.getRoleCredentials(
                accountId = accountId,
                roleName = roleName,
                accessToken = newAccessToken
            )
        }
    }
    attemptSSOLogin(profileName)
    val newAccessToken = loadAccessToken() ?: error("No access token after SSO login")
    return ssoClient.getRoleCredentials(
        accountId = accountId,
        roleName = roleName,
        accessToken = newAccessToken
    )
}

fun main() = runScript {
    val args = process.argv.drop(2)
    val profile = if (args.isNotEmpty()) args[0] else null
    val sharedConfig = loadSharedConfigFiles().await()
    if (profile != null) {
        val ssoConfig = sharedConfig.configFile[profile] ?: error("No such profile in \$HOME/.aws/config: $profile")
        val region = ssoConfig["sso_region"]!!
        val accountId = ssoConfig["sso_account_id"]!!
        val roleName = ssoConfig["sso_role_name"]!!
        createSSOClient(region = region, defaultsMode = "standard").use { ssoClient ->
            val response = getRoleCredentialsPossiblyLogin(ssoClient, accountId, roleName, profile)

            response.roleCredentials?.let { roleCredentials ->
                println("AWS_ACCESS_KEY_ID=${roleCredentials.accessKeyId};")
                println("AWS_SECRET_ACCESS_KEY=${roleCredentials.secretAccessKey};")
                println("AWS_SESSION_TOKEN=${roleCredentials.sessionToken};")
                println("AWS_DEFAULT_REGION=${ssoConfig["sso_region"]};")
                println("export AWS_ACCESS_KEY_ID AWS_SECRET_ACCESS_KEY AWS_SESSION_TOKEN AWS_DEFAULT_REGION;")
            }
        }
    } else {
        for ((name, value) in sharedConfig.configFile.entries) {
            if (value["sso_start_url"] != null) {
                println(name)
            }
        }
    }
}

private val <K : Any, V : Any> Record<K, V>.entries: List<Pair<K, V>>
    get() = js("Object.entries")(this@entries).unsafeCast<Array<Array<dynamic>>>()
        .map { (key, value) -> Pair(key, value) }
