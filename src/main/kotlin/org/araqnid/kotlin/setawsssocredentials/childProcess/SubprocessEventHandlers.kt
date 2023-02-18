package org.araqnid.kotlin.setawsssocredentials.childProcess

import node.events.EventType

inline fun Subprocess.onError(noinline cb: (err: Throwable) -> Unit) {
    on(EventType("error"), cb.unsafeCast<(value: Any?) -> Unit>())
}

inline fun Subprocess.onClose(noinline cb: (exitCode: Int) -> Unit) {
    on(EventType("close"), cb.unsafeCast<(value: Any?) -> Unit>())
}
