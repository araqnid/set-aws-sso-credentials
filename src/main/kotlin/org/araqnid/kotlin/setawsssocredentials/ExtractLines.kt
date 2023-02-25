package org.araqnid.kotlin.setawsssocredentials

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun Flow<CharSequence>.extractLines(): Flow<String> {
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
