package org.araqnid.kotlin.setawsssocredentials.aws

import node.Dict
import web.abort.AbortSignal

/**
 * A mapping of header names to string values. Multiple values for the same
 * header should be represented as a single string with values separated by
 * `, `.
 *
 * Keys should be considered case insensitive, even if this is not enforced by a
 * particular implementation. For example, given the following HeaderBag, where
 * keys differ only in case:
 *
 *    {
 *        'x-amz-date': '2000-01-01T00:00:00Z',
 *        'X-Amz-Date': '2001-01-01T00:00:00Z'
 *    }
 *
 * The SDK may at any point during processing remove one of the object
 * properties in favor of the other. The headers may or may not be combined, and
 * the SDK will not deterministically select which header candidate to use.
 */
typealias HeaderBag = Dict<String>

external interface HttpMessage {
    var headers: HeaderBag
}

/**
 * A mapping of query parameter names to strings or arrays of strings, with the
 * second being used when a parameter contains a list of values. Value can be set
 * to null when query is not in key-value pairs shape
 */
typealias QueryParameterBag = Dict<Any> // Record<string, string | string[] | null>

external interface Endpoint {
    var protocol: String
    var hostname: String
    val port: Number?
    var path: String
    var query: QueryParameterBag?
}

/**
 * Interface an HTTP request class. Contains
 * addressing information in addition to standard message properties.
 */
external interface HttpRequest : HttpMessage, Endpoint {
    var method: String
}

/**
 * Represents an HTTP message as received in reply to a request. Contains a
 * numeric status code in addition to standard message properties.
 */
external interface HttpResponse : HttpMessage {
    var statusCode: Number
}

/**
 * Represents the options that may be passed to an Http Handler.
 */
external interface HttpHandlerOptions {
    var abortSignal: AbortSignal
}
