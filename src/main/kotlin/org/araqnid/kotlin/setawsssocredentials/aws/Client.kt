package org.araqnid.kotlin.setawsssocredentials.aws

import kotlinx.coroutines.await
import org.araqnid.kotlin.setawsssocredentials.jsObject
import org.araqnid.kotlin.setawsssocredentials.withAbortSignal
import kotlin.js.Promise

external interface Client<ServiceInputTypes, ServiceOutputTypes, ResolvedClientConfiguration> {
    val config: ResolvedClientConfiguration

    fun <I : ServiceInputTypes, O : ServiceOutputTypes> send(
        input: Command<I, O>,
        options: HttpHandlerOptions = definedExternally
    ): Promise<O>

    fun <I : ServiceInputTypes, O : ServiceOutputTypes> send(
        input: Command<I, O>,
        cb: (err: Any?, data: O?) -> Unit
    )

    fun <I : ServiceInputTypes, O : ServiceOutputTypes> send(
        input: Command<I, O>,
        options: HttpHandlerOptions = definedExternally,
        cb: (err: Any?, data: O?) -> Unit
    )

    /**
     * Destroy underlying resources, like sockets. It's usually not necessary to do this.
     * However in Node.js, it's best to explicitly shut down the client's agent when it is no longer needed.
     * Otherwise, sockets might stay open for quite a long time before the server terminates them.
     */
    fun destroy()
}

suspend fun <I, O, CI : I, CO : O> Client<I, O, *>.sendCancellable(command: Command<CI, CO>): CO {
    return withAbortSignal { abortSignal ->
        send(command, jsObject<HttpHandlerOptions> { this.abortSignal = abortSignal }).await()
    }
}

inline fun <C : Client<*, *, *>, R> C.use(block: (C) -> R): R {
    return try {
        block(this)
    } finally {
        destroy()
    }
}