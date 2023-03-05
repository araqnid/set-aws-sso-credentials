@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import js.core.jso
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import org.araqnid.kotlin.setawsssocredentials.aws.Credentials
import org.araqnid.kotlin.setawsssocredentials.aws.Provider
import org.araqnid.kotlin.setawsssocredentials.aws.send

@OptIn(DelicateCoroutinesApi::class)
fun createSTSClient(
    region: String? = null,
    defaultsMode: String? = null,
    credentialDefaultProvider: (suspend () -> Credentials)? = null
): STSClient {
    return STSClient(jso {
        if (region != null) this.region = region
        if (defaultsMode != null) this.defaultsMode = defaultsMode
        if (credentialDefaultProvider != null) this.credentialDefaultProvider =
            { GlobalScope.promise { credentialDefaultProvider() } }.unsafeCast<Provider<Credentials>>()
    })
}

/**
 * @see AssumeRoleCommand
 */
suspend inline fun STSClient.assumeRole(
    roleArn: String,
    roleSessionName: String,
    policyArns: Array<PolicyDescriptorType>? = null,
    policy: String? = null,
    durationSeconds: Int? = null,
    tags: Array<Tag>? = null,
    transitiveTagKeys: Array<String>? = null,
    externalId: String? = null,
    serialNumber: String? = null,
    tokenCode: String? = null,
    sourceIdentity: String? = null
): AssumeRoleResponse =
    send(AssumeRoleCommand(jso {
        this.roleArn = roleArn
        this.roleSessionName = roleSessionName
        this.policyArns = policyArns
        this.policy = policy
        this.durationSeconds = durationSeconds
        this.tags = tags
        this.transitiveTagKeys = transitiveTagKeys
        this.externalId = externalId
        this.serialNumber = serialNumber
        this.tokenCode = tokenCode
        this.sourceIdentity = sourceIdentity
    }))

/**
 * @see AssumeRoleWithSAMLCommand
 */
suspend inline fun STSClient.assumeRoleWithSAML(
    roleArn: String,
    principalArn: String,
    samlAssertion: String,
    policyArns: Array<PolicyDescriptorType>? = null,
    policy: String? = null,
    durationSeconds: Int? = null
): AssumeRoleWithSAMLResponse =
    send(AssumeRoleWithSAMLCommand(jso {
        this.roleArn = roleArn
        this.principalArn = principalArn
        this.samlAssertion = samlAssertion
        this.policyArns = policyArns
        this.policy = policy
        this.durationSeconds = durationSeconds
    }))

/**
 * @see AssumeRoleWithWebIdentityCommand
 */
suspend inline fun STSClient.assumeRoleWithWebIdentity(
    roleArn: String,
    roleSessionName: String,
    webIdentityToken: String,
    providerId: String? = null,
    policyArns: Array<PolicyDescriptorType>? = null,
    policy: String? = null,
    durationSeconds: Int? = null
): AssumeRoleWithWebIdentityResponse =
    send(AssumeRoleWithWebIdentityCommand(jso {
        this.roleArn = roleArn
        this.roleSessionName = roleSessionName
        this.webIdentityToken = webIdentityToken
        this.providerId = providerId
        this.policyArns = policyArns
        this.policy = policy
        this.durationSeconds = durationSeconds
    }))

/**
 * @see DecodeAuthorizationMessageCommand
 */
suspend inline fun STSClient.decodeAuthorizationMessage(encodedMessage: String): DecodeAuthorizationMessageResponse =
    send(DecodeAuthorizationMessageCommand(jso {
        this.encodedMessage = encodedMessage
    }))

/**
 * @see GetAccessKeyInfoCommand
 */
suspend inline fun STSClient.getAccessKeyInfo(accessKeyId: String): GetAccessKeyInfoResponse =
    send(GetAccessKeyInfoCommand(jso {
        this.accessKeyId = accessKeyId
    }))

/**
 * @see GetCallerIdentityCommand
 */
suspend inline fun STSClient.getCallerIdentity(): GetCallerIdentityResponse =
    send(GetCallerIdentityCommand(jso { }))

/**
 * @see GetFederationTokenCommand
 */
suspend inline fun STSClient.getFederationToken(
    name: String,
    policy: String? = null,
    policyArns: Array<PolicyDescriptorType>? = null,
    durationSeconds: Int? = null,
    tags: Array<Tag>? = null
): GetFederationTokenResponse =
    send(GetFederationTokenCommand(jso {
        this.name = name
        this.policy = policy
        this.policyArns = policyArns
        this.durationSeconds = durationSeconds
        this.tags = tags
    }))

/**
 * @see GetSessionTokenCommand
 */
suspend inline fun STSClient.getSessionToken(
    durationSeconds: Int? = null,
    serialNumber: Int? = null,
    tokenCode: String? = null
): GetSessionTokenResponse =
    send(GetSessionTokenCommand(jso {
        this.durationSeconds = durationSeconds
        this.serialNumber = serialNumber
        this.tokenCode = tokenCode
    }))
