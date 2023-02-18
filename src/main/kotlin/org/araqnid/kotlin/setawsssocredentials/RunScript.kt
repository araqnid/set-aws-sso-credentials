package org.araqnid.kotlin.setawsssocredentials

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import node.process.process
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

fun runScript(context: CoroutineContext = EmptyCoroutineContext, body: suspend CoroutineScope.() -> Unit) {
    val job = Job()
    val composedContext = context + job
    val scope = CoroutineScope(composedContext)

    job.invokeOnCompletion { ex ->
        if (ex != null) {
            console.error("Fatal error", ex)
            process.exit(1)
        } else {
            process.exit(0)
        }
    }

    body.startCoroutine(scope, Continuation(composedContext) { result ->
        result.fold({
            job.complete()
        }, { ex ->
            job.completeExceptionally(ex)
        })
    })
}
