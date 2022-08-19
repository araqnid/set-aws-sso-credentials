package org.araqnid.kotlin.setawsssocredentials

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

fun runScript(context: CoroutineContext = EmptyCoroutineContext, body: suspend CoroutineScope.() -> Unit) {
    val job = Job()
    val composedContext = context + job
    val scope = CoroutineScope(composedContext)

    val completion = object : Continuation<Unit> {
        override val context: CoroutineContext
            get() = composedContext

        override fun resumeWith(result: Result<Unit>) {
            result.fold({
                job.complete()
            }, { ex ->
                job.completeExceptionally(ex)
            })
        }
    }

    job.invokeOnCompletion { ex ->
        if (ex != null) {
            console.error("Fatal error", ex)
            Process.exit(1)
        } else {
            Process.exit(0)
        }
    }

    body.startCoroutine(scope, completion)
}
