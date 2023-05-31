package org.araqnid.kotlin.setawsssocredentials

import js.core.jso
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import node.events.Event
import org.araqnid.kotlin.setawsssocredentials.childProcess.spawn

sealed interface CommandOutput {
    interface TextOutput {
        val text: String
    }

    data class Stdout(override val text: String) : CommandOutput, TextOutput
    data class Stderr(override val text: String) : CommandOutput, TextOutput
    data class Exit(val exitCode: Int) : CommandOutput
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
            spawned.stdout!!.readTextChunks().extractLines().collect { send(CommandOutput.Stdout(it)) }
        }

        val stderr = launch {
            spawned.stderr!!.readTextChunks().extractLines().collect { send(CommandOutput.Stderr(it)) }
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
