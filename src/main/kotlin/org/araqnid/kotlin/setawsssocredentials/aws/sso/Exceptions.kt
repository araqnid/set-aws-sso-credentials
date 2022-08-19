@file:JsModule("@aws-sdk/client-sso")

package org.araqnid.kotlin.setawsssocredentials.aws.sso

import org.araqnid.kotlin.setawsssocredentials.aws.ServiceException

open external class SSOServiceException : ServiceException

/**
 * Indicates that a problem occurred with the input to the request. For example, a required
 * parameter might be missing or out of range.
 */
external class InvalidRequestException : SSOServiceException

/**
 * The specified resource doesn't exist.
 */
external class ResourceNotFoundException : SSOServiceException

/**
 * Indicates that the request is being made too frequently and is more than what the server
 * can handle.
 */
external class TooManyRequestsException : SSOServiceException

/**
 * Indicates that the request is not authorized. This can happen due to an invalid access
 * token in the request.
 */
external class UnauthorizedException : SSOServiceException
