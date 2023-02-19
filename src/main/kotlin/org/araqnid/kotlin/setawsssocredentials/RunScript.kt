package org.araqnid.kotlin.setawsssocredentials

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import node.process.process
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

fun runScript(context: CoroutineContext = EmptyCoroutineContext, body: suspend CoroutineScope.() -> Unit) {
    val job = Job(parent = context[Job])
    val scope = CoroutineScope(context + job)

    job.invokeOnCompletion { ex ->
        if (ex != null) {
            console.error("Fatal error", ex)
            process.exitCode = 1
        }
    }

    body.startCoroutine(scope, Continuation(scope.coroutineContext) { result ->
        result.fold({
            job.complete()
        }, { ex ->
            job.completeExceptionally(ex)
        })
    })
}
