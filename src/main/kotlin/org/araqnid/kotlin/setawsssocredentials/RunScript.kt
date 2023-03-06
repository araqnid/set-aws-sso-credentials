package org.araqnid.kotlin.setawsssocredentials

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import node.events.Event
import node.os.EOL
import node.process.process
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

fun runScript(context: CoroutineContext = EmptyCoroutineContext, body: suspend CoroutineScope.() -> Unit) {
    val job = Job(parent = context[Job])
    val scope = CoroutineScope(Dispatchers.Default + context + job)

    process.on(Event.EXIT) {
        if (job.isCompleted) return@on

        console.error(buildString {
            fun appendJob(cursor: Job, level: Int) {
                for (i in 0 until level) {
                    append("-> ")
                }
                append(cursor)
                if (cursor is CoroutineScope) {
                    val coroutineName = cursor.coroutineContext[CoroutineName]
                    if (coroutineName != null) {
                        append(": ")
                        append(coroutineName.name)
                    }
                }
                append(EOL)
                for (child in cursor.children) {
                    appendJob(child, level + 1)
                }
            }

            append("process exiting without script job terminating: $job$EOL")
            appendJob(job, 0)
        })
    }

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
