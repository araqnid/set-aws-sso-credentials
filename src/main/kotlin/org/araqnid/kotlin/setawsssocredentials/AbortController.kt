package org.araqnid.kotlin.setawsssocredentials

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.job

external class AbortController {
    fun abort(reason: Any? = definedExternally)
    val signal: AbortSignal
}

external interface EventListener<in E> {
    fun handleEvent(event: E)
}

external interface EventListenerOptions {
    var once: Boolean?
    var passive: Boolean?
    var signal: AbortSignal?
}

open external class EventTarget {
    fun addEventListener(type: String, listener: EventListener<*>, options: EventListenerOptions = definedExternally)
    fun removeEventListener(type: String, listener: EventListener<*>, options: EventListenerOptions = definedExternally)
    fun dispatchEvent(event: Any)
}

external class AbortSignal : EventTarget {
    @JsName("aborted")
    val isAborted: Boolean

    val reason: Any?

    fun throwIfAborted()
}

suspend fun <R> withAbortSignal(block: suspend (AbortSignal) -> R): R {
    return coroutineScope {
        val abortController = AbortController()
        coroutineContext.job.invokeOnCompletion { ex ->
            abortController.abort(ex)
        }
        block(abortController.signal)
    }
}
