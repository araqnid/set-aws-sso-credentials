package org.araqnid.kotlin.setawsssocredentials

import js.core.jso
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import node.buffer.BufferEncoding
import node.stream.Readable
import node.stream.Writable

fun Readable.readTextChunks(encoding: BufferEncoding = BufferEncoding.utf8): Flow<String> {
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
