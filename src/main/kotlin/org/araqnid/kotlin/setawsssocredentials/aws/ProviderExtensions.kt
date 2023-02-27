package org.araqnid.kotlin.setawsssocredentials.aws

import kotlin.js.Promise

inline operator fun <T> Provider<T>.invoke(): Promise<T> {
    return this.asDynamic()().unsafeCast<Promise<T>>()
}
