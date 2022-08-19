package org.araqnid.kotlin.setawsssocredentials

external interface JsRecord<V>

inline operator fun <V> JsRecord<V>.get(key: String): V? = this.asDynamic()[key].unsafeCast<V?>()

val <V> JsRecord<V>.entries: List<Pair<String, V>>
    get() = js("Object").entries(this).unsafeCast<Array<Array<dynamic>>>()
        .map { it[0].unsafeCast<String>() to it[1].unsafeCast<V>() }
