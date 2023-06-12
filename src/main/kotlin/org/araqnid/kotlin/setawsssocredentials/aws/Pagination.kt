package org.araqnid.kotlin.setawsssocredentials.aws

import js.core.AsyncIterable
import js.core.Symbol
import js.core.asYieldOrNull
import kotlinx.coroutines.await
import kotlinx.coroutines.flow.*

external interface Paginator<out T> : AsyncIterable<T>

external interface PaginationConfiguration {
    val client: Client<*, *, *>?
    var pageSize: Number?
    var startingToken: Any?
    var stopOnSameToken: Boolean?
}

fun <T> Paginator<T>.asFlow(): Flow<T> {
    return flow {
        val iterator = this@asFlow[Symbol.asyncIterator]()
        while (true) {
            val result = iterator.next().await()
            val yielded = result.asYieldOrNull() ?: break
            emit(yielded.value)
        }
    }
}

fun <T, U> Paginator<T>.flattenedAsFlow(flatten: (T) -> Array<U>?): Flow<U> {
    return asFlow().transform { output ->
        flatten(output)?.let { emitAll(it.asFlow()) }
    }
}
