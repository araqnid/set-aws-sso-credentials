@file:JsModule("node:child_process")
@file:Suppress("unused")

package org.araqnid.kotlin.setawsssocredentials.childProcess

import node.events.EventEmitter
import node.stream.Readable
import node.stream.Writable

external class Subprocess : EventEmitter {
    val stdout: Readable
    val stderr: Readable
    val stdin: Writable
    fun kill(signal: String): Boolean
    fun kill(signal: Int): Boolean
    fun kill(): Boolean
}
