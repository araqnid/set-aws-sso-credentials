package org.araqnid.kotlin.setawsssocredentials

@JsModule("node:stream")
external object Stream {
    interface WritableOptions {
        var decodeStrings: Boolean?
        var write: ((chunk: Any, encoding: String, callback: (error: Throwable?) -> Unit) -> Unit)?
        var final: ((callback: (error: Throwable?) -> Unit) -> Unit)?
    }

    class Writable(options: WritableOptions) {
        fun write(text: String)
    }

    class Readable {
        fun setEncoding(encoding: String)
        fun pipe(target: Writable)
    }
}