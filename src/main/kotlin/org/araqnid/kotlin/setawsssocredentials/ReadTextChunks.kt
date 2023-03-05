package org.araqnid.kotlin.setawsssocredentials

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import node.buffer.BufferEncoding
import node.events.Event
import node.stream.Readable

fun Readable.readTextChunks(encoding: BufferEncoding = BufferEncoding.utf8): Flow<String> {
    return callbackFlow {
        setEncoding(encoding)

        on(Event.DATA) { chunk ->
            val fastResult = trySend(chunk.unsafeCast<String>())
            if (fastResult.isSuccess) return@on
            pause()
            launch {
                send(chunk.unsafeCast<String>())
                resume()
            }
        }

        on(Event.CLOSE) {
            close()
        }

        on(Event.ERROR) { err ->
            close(err)
        }

        awaitClose {
            this@readTextChunks.destroy()
        }
    }
}
