package org.araqnid.kotlin.setawsssocredentials

import js.core.jso
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import node.buffer.BufferEncoding
import node.childProcess.SpawnOptions
import node.events.EventEmitter
import node.events.EventType
import node.stream.Readable
import node.stream.Writable
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

@JsModule("node:child_process")
private external object ChildProcess {
    fun spawn(command: String, args: Array<out String>, options: SpawnOptions): Subprocess

    class Subprocess : EventEmitter {
        val stdout: Readable
        val stderr: Readable
        val stdin: Writable
        fun kill(signal: String): Boolean
        fun kill(signal: Int): Boolean
        fun kill(): Boolean
    }
}

private inline fun ChildProcess.Subprocess.onError(noinline cb: (err: Throwable) -> Unit) {
    on(EventType("error"), cb.unsafeCast<(value: Any?) -> Unit>())
}

private inline fun ChildProcess.Subprocess.onClose(noinline cb: (exitCode: Int) -> Unit) {
    on(EventType("close"), cb.unsafeCast<(value: Any?) -> Unit>())
}

sealed interface CommandOutput {
    interface TextOutput {
        val text: String
    }

    data class Stdout(override val text: String) : CommandOutput, TextOutput
    data class Stderr(override val text: String) : CommandOutput, TextOutput
    data class Exit(val exitCode: Int) : CommandOutput
}

private fun Readable.readTextChunks(encoding: BufferEncoding = BufferEncoding.utf8): ReceiveChannel<String> {
    val chunks = Channel<String>()
    setEncoding(encoding)
    pipe(Writable(jso {
        decodeStrings = false
        write = write@{ chunk, _, callback ->
            val fastResult = chunks.trySend(chunk.unsafeCast<String>())
            if (fastResult.isSuccess) {
                callback(null)
                return@write
            }
            val block: suspend () -> Unit = {
                chunks.send(chunk.unsafeCast<String>())
            }
            block.startCoroutine(Continuation(EmptyCoroutineContext) { res ->
                callback(res.fold({ null }, { it.unsafeCast<Error>() }))
            })
        }
        final = { callback ->
            chunks.close()
            callback(null)
        }
    }))
    return chunks
}

internal fun Flow<String>.splitLines(): Flow<String> {
    return flow {
        val residual = StringBuilder()

        collect { chunk ->
            var lastBreak = -1
            while (true) {
                val nextBreak = chunk.indexOf('\n', lastBreak + 1)
                if (nextBreak < 0) break
                if (lastBreak >= 0 || residual.isEmpty()) {
                    emit(chunk.substring(lastBreak + 1, nextBreak))
                } else {
                    residual.append(chunk.subSequence(0, nextBreak))
                    emit(residual.toString())
                    residual.clear()
                }
                lastBreak = nextBreak
            }
            residual.append(if (lastBreak >= 0) chunk.subSequence(lastBreak + 1, chunk.length) else chunk)
        }

        if (residual.isNotEmpty())
            emit(residual.toString())
    }
}

/**
 * Run command and return flow of stdout and stderr lines, followed by exit code.
 */
fun command(command: String, vararg args: String): Flow<CommandOutput> {
    return callbackFlow {
        val spawned = ChildProcess.spawn(command, args, jso {
            stdio = arrayOf("inherit", "pipe", "pipe")
        })

        val deferredExitCode = CompletableDeferred<Int>()

        val stdoutChunks = spawned.stdout.readTextChunks()
        val stdout = launch {
            stdoutChunks.consumeAsFlow().splitLines().collect { send(CommandOutput.Stdout(it)) }
        }

        val stderrChunks = spawned.stderr.readTextChunks()
        val stderr = launch {
            stderrChunks.consumeAsFlow().splitLines().collect { send(CommandOutput.Stderr(it)) }
        }

        spawned.onError { err ->
            close(err)
        }

        spawned.onClose { exitCode ->
            deferredExitCode.complete(exitCode)
        }

        launch {
            stdout.join()
            stderr.join()
            send(CommandOutput.Exit(deferredExitCode.await()))
            close()
        }

        awaitClose {
            spawned.kill()
        }
    }
}
