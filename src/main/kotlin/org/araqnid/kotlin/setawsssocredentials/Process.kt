package org.araqnid.kotlin.setawsssocredentials

@JsModule("node:process")
external object Process {
    fun exit(exitCode: Int)
    val argv: Array<String>
    val env: JsRecord<String>
    val stdout: Stream.Writable
    val stderr: Stream.Writable
}
