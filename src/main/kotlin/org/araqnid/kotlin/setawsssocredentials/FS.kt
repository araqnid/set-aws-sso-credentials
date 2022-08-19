package org.araqnid.kotlin.setawsssocredentials

import kotlinx.coroutines.await
import kotlin.js.Promise

@JsModule("node:fs")
external object FS {
    @JsName("promises")
    object Promises {
        fun readFile(filename: String, encoding: String): Promise<String>
    }
}

suspend fun readFile(filename: String, encoding: String): String {
    return FS.Promises.readFile(filename, encoding).await()
}
