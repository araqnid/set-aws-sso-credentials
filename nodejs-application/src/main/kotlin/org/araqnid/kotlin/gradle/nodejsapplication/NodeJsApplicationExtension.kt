package org.araqnid.kotlin.gradle.nodejsapplication

import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty

abstract class NodeJsApplicationExtension {
    abstract val useNcc: Property<Boolean>
    abstract val minify: Property<Boolean>
    abstract val v8cache: Property<Boolean>
    abstract val target: Property<String>
    abstract val sourceMap: Property<Boolean>
    abstract val moduleName: Property<String>
    abstract val externalModules: SetProperty<String>

    init {
        minify.convention(true)
        v8cache.convention(false)
        sourceMap.convention(false)
        useNcc.convention(true)
    }
}
