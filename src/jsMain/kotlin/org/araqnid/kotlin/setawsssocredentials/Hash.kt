package org.araqnid.kotlin.setawsssocredentials

import node.crypto.BinaryToTextEncoding
import node.crypto.createHash

fun sha1(input: String): String {
    val hasher = createHash("sha1")
    hasher.update(input)
    return hasher.digest(BinaryToTextEncoding.hex)
}
