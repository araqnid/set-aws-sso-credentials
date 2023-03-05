@file:JsModule("@aws-sdk/client-sts")
@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.aws.sts

import kotlin.js.Date

external interface AssumedRoleUser {
    @JsName("AssumedRoleId")
    var assumedRoleId: String?

    @JsName("Arn")
    var arn: String?
}

external interface PolicyDescriptorType {
    @JsName("Arn")
    var arn: String?
}

external interface Tag {
    @JsName("Key")
    var key: String?

    @JsName("Value")
    var value: String?
}

external interface AssumeRoleRequest {
    @JsName("RoleArn")
    var roleArn: String?

    @JsName("RoleSessionName")
    var roleSessionName: String?

    @JsName("PolicyArns")
    var policyArns: Array<PolicyDescriptorType>?

    @JsName("Policy")
    var policy: String?

    @JsName("DurationSeconds")
    var durationSeconds: Int?

    @JsName("Tags")
    var tags: Array<Tag>?

    @JsName("TransitiveTagKeys")
    var transitiveTagKeys: Array<String>?

    @JsName("ExternalId")
    var externalId: String?

    @JsName("SerialNumber")
    var serialNumber: String?

    @JsName("TokenCode")
    var tokenCode: String?

    @JsName("SourceIdentity")
    var sourceIdentity: String?
}

external interface Credentials {
    @JsName("AccessKeyId")
    var accessKeyId: String?

    @JsName("SecretAccessKey")
    var secretAccessKey: String?

    @JsName("SessionToken")
    var sessionToken: String?

    @JsName("Expiration")
    var expiration: Date?
}

external interface AssumeRoleResponse {
    @JsName("Credentials")
    var credentials: Credentials?

    @JsName("AssumedRoleUser")
    var assumedRoleUser: AssumedRoleUser?

    @JsName("PackedPolicySize")
    var packedPolicySize: Int?

    @JsName("SourceIdentity")
    var sourceIdentity: String?
}

external interface AssumeRoleWithSAMLRequest {
    @JsName("RoleArn")
    var roleArn: String?

    @JsName("PrincipalArn")
    var principalArn: String?

    @JsName("SAMLAssertion")
    var samlAssertion: String?

    @JsName("PolicyArns")
    var policyArns: Array<PolicyDescriptorType>?

    @JsName("Policy")
    var policy: String?

    @JsName("DurationSeconds")
    var durationSeconds: Int?
}

external interface AssumeRoleWithSAMLResponse {
    @JsName("Credentials")
    var credentials: Credentials?

    @JsName("AssumedRoleUser")
    var assumedRoleUser: AssumedRoleUser?

    @JsName("PackedPolicySize")
    var packedPolicySize: Int?

    @JsName("Subject")
    var subject: String?

    @JsName("SubjectType")
    var subjectType: String?

    @JsName("Issuer")
    var issuer: String?

    @JsName("Audience")
    var audience: String?

    @JsName("NameQualifier")
    var nameQualifier: String?

    @JsName("SourceIdentity")
    var sourceIdentity: String?
}

external interface AssumeRoleWithWebIdentityRequest {
    @JsName("RoleArn")
    var roleArn: String?

    @JsName("RoleSessionName")
    var roleSessionName: String?

    @JsName("WebIdentityToken")
    var webIdentityToken: String?

    @JsName("ProviderId")
    var providerId: String?

    @JsName("PolicyArns")
    var policyArns: Array<PolicyDescriptorType>?

    @JsName("Policy")
    var policy: String?

    @JsName("DurationSeconds")
    var durationSeconds: Int?
}

external interface AssumeRoleWithWebIdentityResponse {
    @JsName("Credentials")
    var credentials: Credentials?

    @JsName("SubjectFromWebIdentityToken")
    var subjectFromWebIdentityToken: String?

    @JsName("AssumedRoleUser")
    var assumedRoleUser: AssumedRoleUser?

    @JsName("PackedPolicySize")
    var packedPolicySize: Int?

    @JsName("Provider")
    var provider: String?

    @JsName("Audience")
    var audience: String?

    @JsName("SourceIdentity")
    var sourceIdentity: String?
}

external interface DecodeAuthorizationMessageRequest {
    @JsName("EncodedMessage")
    var encodedMessage: String?
}

external interface DecodeAuthorizationMessageResponse {
    @JsName("DecodedMessage")
    var decodedMessage: String?
}

external interface GetAccessKeyInfoRequest {
    @JsName("AccessKeyId")
    var accessKeyId: String?
}

external interface GetAccessKeyInfoResponse {
    @JsName("Account")
    var account: String?
}

external interface GetCallerIdentityRequest {
}

/**
 * Contains the response to a successful [GetCallerIdentityRequest] request,
 * including information about the entity making the request.
 */
external interface GetCallerIdentityResponse {
    /**
     * The unique identifier of the calling entity. The exact value depends on the type of
     * entity that is making the call. The values returned are those listed in the **aws:userid**
     * column in the
     * [Principal table](https://docs.aws.amazon.com/IAM/latest/UserGuide/reference_policies_variables.html#principaltable)
     *  found on the **Policy Variables** reference
     * page in the *IAM User Guide*.
     */
    @JsName("UserId")
    var userId: String?

    /**
     * The Amazon Web Services account ID number of the account that owns or contains the calling
     * entity.
     */
    @JsName("Account")
    var account: String?

    /**
     * The Amazon Web Services ARN associated with the calling entity.
     */
    @JsName("Arn")
    var arn: String?
}

external interface GetFederationTokenRequest {
    @JsName("Name")
    var name: String?

    @JsName("Policy")
    var policy: String?

    @JsName("PolicyArns")
    var policyArns: Array<PolicyDescriptorType>?

    @JsName("DurationSeconds")
    var durationSeconds: Int?

    @JsName("Tags")
    var tags: Array<Tag>?
}

external interface FederatedUser {
    @JsName("FederatedUserId")
    var federatedUserId: String?

    @JsName("Arn")
    var arn: String?
}

external interface GetFederationTokenResponse {
    @JsName("Credentials")
    var credentials: Credentials?

    @JsName("FederatedUser")
    var federatedUser: FederatedUser?

    @JsName("PackedPolicySize")
    var packedPolicySize: Int?
}

external interface GetSessionTokenRequest {
    @JsName("DurationSeconds")
    var durationSeconds: Int?

    @JsName("SerialNumber")
    var serialNumber: Int?

    @JsName("TokenCode")
    var tokenCode: String?
}

external interface GetSessionTokenResponse {
    @JsName("Credentials")
    var credentials: Credentials?
}
