@file:JsModule("node:child_process")

package org.araqnid.kotlin.setawsssocredentials.childProcess

import node.childProcess.SpawnOptions

external fun spawn(command: String, args: Array<out String>, options: SpawnOptions): Subprocess
