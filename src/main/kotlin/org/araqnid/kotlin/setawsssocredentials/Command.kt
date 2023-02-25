package org.araqnid.kotlin.setawsssocredentials

import js.core.jso
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import node.buffer.BufferEncoding
import node.events.Event
import node.stream.Readable
import node.stream.Writable
import org.araqnid.kotlin.setawsssocredentials.childProcess.spawn

sealed interface CommandOutput {
    interface TextOutput {
        val text: String
    }

    data class Stdout(override val text: String) : CommandOutput, TextOutput
    data class Stderr(override val text: String) : CommandOutput, TextOutput
    data class Exit(val exitCode: Int) : CommandOutput
}

private fun Readable.readTextChunks(encoding: BufferEncoding = BufferEncoding.utf8): Flow<String> {
    return channelFlow {
        setEncoding(encoding)
        pipe(Writable(jso {
            decodeStrings = false
            write = { chunk, _, callback ->
                val fastResult = trySend(chunk.unsafeCast<String>())
                if (fastResult.isSuccess) {
                    callback(null)
                } else {
                    launch {
                        try {
                            send(chunk.unsafeCast<String>())
                            callback(null)
                        } catch (err: Throwable) {
                            callback(err.unsafeCast<Error>())
                            throw err
                        }
                    }
                }
            }
            final = { callback ->
                close()
                callback(null)
            }
        }))
        awaitClose()
    }
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
        val spawned = spawn(command, args, jso {
            stdio = arrayOf("inherit", "pipe", "pipe")
        })

        val deferredExitCode = CompletableDeferred<Int>()

        val stdout = launch {
            spawned.stdout.readTextChunks().splitLines().collect { send(CommandOutput.Stdout(it)) }
        }

        val stderr = launch {
            spawned.stderr.readTextChunks().splitLines().collect { send(CommandOutput.Stderr(it)) }
        }

        spawned.on(Event.ERROR) { err: Throwable ->
            close(err)
        }

        spawned.on(Event.CLOSE) { exitCode: Int ->
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
