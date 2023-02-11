package org.araqnid.kotlin.setawsssocredentials

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.job
import web.abort.AbortController
import web.abort.AbortSignal

suspend fun <R> withAbortSignal(block: suspend (AbortSignal) -> R): R {
    return coroutineScope {
        val abortController = AbortController()
        coroutineContext.job.invokeOnCompletion { ex ->
            abortController.abort(ex)
        }
        block(abortController.signal)
    }
}
