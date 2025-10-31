package org.araqnid.kotlin.setawsssocredentials.aws

open external class ServiceException : Throwable, MetadataBearer, SmithyException {
    override val fault: String? /* = "client" | "server" */
    override val name: String
    override val response: HttpResponse?
    override val service: String?

    @JsName("\$retryable")
    val retryable: RetryableTrait?
    override val metadata: ResponseMetadata
}

external interface SmithyException {
    @JsName("\$fault")
    val fault: String? /* = "client" | "server" */
    val name: String

    @JsName("\$service")
    val service: String?

    @JsName("\$response")
    val response: HttpResponse?
}

external interface RetryableTrait {
    val throttling: Boolean?
}
