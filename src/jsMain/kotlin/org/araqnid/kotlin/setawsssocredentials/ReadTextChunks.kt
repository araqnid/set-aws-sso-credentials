package org.araqnid.kotlin.setawsssocredentials

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import node.buffer.BufferEncoding
import node.stream.Readable
import node.stream.ReadableEvent

fun Readable.readTextChunks(encoding: BufferEncoding = BufferEncoding.utf8): Flow<String> {
    return callbackFlow {
        setEncoding(encoding)

        on(ReadableEvent.DATA) { chunk ->
            val fastResult = trySend(chunk.unsafeCast<String>())
            if (fastResult.isSuccess) return@on
            pause()
            launch {
                send(chunk.unsafeCast<String>())
                resume()
            }
        }

        on(ReadableEvent.CLOSE) {
            close()
        }

        on(ReadableEvent.ERROR) { err ->
            close(err)
        }

        awaitClose {
            this@readTextChunks.destroy()
        }
    }
}
