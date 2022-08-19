package org.araqnid.kotlin.setawsssocredentials

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

@JsModule("node:child_process")
external object ChildProcess {
    fun spawn(command: String, args: Array<out String>, options: SpawnOptions): Subprocess

    interface SpawnOptions {
        var stdio: Array<String> // 3 elements: "inherit" | "pipe" for stdin, stdout, stderr
    }

    interface EventEmitter {
        fun on(eventType: String, listener: (value: Any?) -> Unit)
        fun off(eventType: String, listener: (value: Any?) -> Unit)
        fun once(eventType: String, listener: (value: Any?) -> Unit)
    }

    interface Subprocess : EventEmitter {
        val stdout: Stream.Readable
        val stderr: Stream.Readable
        val stdin: Stream.Writable
        fun kill(signal: String): Boolean
        fun kill(signal: Int): Boolean
        fun kill(): Boolean
    }
}

inline fun ChildProcess.Subprocess.onError(noinline cb: (err: Throwable) -> Unit) {
    on("error", cb.unsafeCast<(value: Any?) -> Unit>())
}

inline fun ChildProcess.Subprocess.onClose(noinline cb: (exitCode: Int) -> Unit) {
    on("close", cb.unsafeCast<(value: Any?) -> Unit>())
}

sealed interface CommandOutput {
    interface TextOutput {
        val text: String
    }

    data class Stdout(override val text: String) : CommandOutput, TextOutput
    data class Stderr(override val text: String) : CommandOutput, TextOutput
    data class Exit(val exitCode: Int) : CommandOutput
}

private fun Stream.Readable.readTextChunks(encoding: String = "utf-8"): ReceiveChannel<String> {
    val chunks = Channel<String>()
    setEncoding(encoding)
    pipe(Stream.Writable(jsObject {
        decodeStrings = false
        write = write@{ chunk, encoding, callback ->
            val fastResult = chunks.trySend(chunk.unsafeCast<String>())
            if (fastResult.isSuccess) return@write
            val block: suspend () -> Unit = {
                chunks.send(chunk.unsafeCast<String>())
            }
            block.startCoroutine(Continuation(EmptyCoroutineContext) { res ->
                callback(res.fold({ null }, { it }))
            })
        }
        final = { callback ->
            chunks.close()
            callback(null)
        }
    }))
    return chunks
}

fun Flow<String>.splitLines(): Flow<String> {
    return flow {
        val residual = StringBuilder()

        collect { chunk ->
            var lastBreak = -1
            for (i in chunk.indices) {
                if (chunk[i] == '\n') {
                    if (lastBreak >= 0) {
                        val line = chunk.substring(lastBreak + 1, i)
                        emit(line)
                    } else {
                        residual.append(chunk.subSequence(0, i))
                        emit(residual.toString())
                        residual.clear()
                    }
                    lastBreak = i
                }
            }
            residual.append(if (lastBreak >= 0) chunk.subSequence(lastBreak + 1, chunk.length) else chunk)
        }

        if (residual.isNotEmpty())
            emit(residual.toString())
    }
}

fun command(command: String, vararg args: String): Flow<CommandOutput> {
    return callbackFlow {
        val spawned = ChildProcess.spawn(command, args, jsObject {
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