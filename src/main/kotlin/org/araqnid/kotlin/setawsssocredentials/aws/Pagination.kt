package org.araqnid.kotlin.setawsssocredentials.aws

import kotlinx.coroutines.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.js.Promise

external interface IteratorResult<out T> {
    val value: T
    val done: Boolean
}

external interface Paginator<out T> {
    fun next(): Promise<IteratorResult<T>>
}

external interface PaginationConfiguration {
    val client: Client<*, *, *>?
    var pageSize: Number?
    var startingToken: Any?
    var stopOnSameToken: Boolean?
}

fun <T> Paginator<T>.asFlow(): Flow<T> {
    return flow {
        while (true) {
            val result = next().await()
            if (result.done) break
            emit(result.value)
        }
    }
}
