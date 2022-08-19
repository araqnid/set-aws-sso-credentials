package org.araqnid.kotlin.setawsssocredentials

@Suppress("NOTHING_TO_INLINE")
inline fun <T> jsObject(): T = js("{}").unsafeCast<T>()

inline fun <T> jsObject(initialiser: T.() -> Unit): T = jsObject<T>().apply(initialiser)
