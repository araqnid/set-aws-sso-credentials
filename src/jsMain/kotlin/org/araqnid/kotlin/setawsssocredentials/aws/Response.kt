package org.araqnid.kotlin.setawsssocredentials.aws

external interface ResponseMetadata {
    /**
     * The status code of the last HTTP response received for this operation.
     */
    var httpStatusCode: Number?

    /**
     * A unique identifier for the last request sent for this operation. Often
     * requested by AWS service teams to aid in debugging.
     */
    var requestId: String?

    /**
     * A secondary identifier for the last request sent. Used for debugging.
     */
    var extendedRequestId: String?

    /**
     * A tertiary identifier for the last request sent. Used for debugging.
     */
    var cfId: String?

    /**
     * The number of times this operation was attempted.
     */
    var attempts: Number?

    /**
     * The total amount of time (in milliseconds) that was spent waiting between
     * retry attempts.
     */
    var totalRetryDelay: Number?
}

external interface MetadataBearer {
    /**
     * Metadata pertaining to this request.
     */
    @JsName("\$metadata")
    val metadata: ResponseMetadata
}