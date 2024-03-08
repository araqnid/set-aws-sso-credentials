package org.araqnid.kotlin.setawsssocredentials.aws

import js.iterable.AsyncIterable
import js.symbol.Symbol
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
        for (value in this@asFlow[Symbol.asyncIterator]()) {
            emit(value)
        }
    }
}

fun <T, U> Paginator<T>.flattenedAsFlow(flatten: (T) -> Array<U>?): Flow<U> {
    return asFlow().transform { output ->
        flatten(output)?.let { emitAll(it.asFlow()) }
    }
}
