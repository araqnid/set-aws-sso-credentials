import com.github.gradle.node.npm.task.NpmTask
import com.github.gradle.node.task.NodeTask
import org.araqnid.kotlin.gradle.nodejsapplication.NodeJsApplicationExtension
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermission

plugins {
    base
    id("com.github.node-gradle.node")
}

val nodeJsApplicationExtension = extensions.create<NodeJsApplicationExtension>("nodeJsApplication")

val jsBuildOutput by lazy {
    rootProject.buildDir.resolve("js")
}

val packageExplodedTask = tasks.register("packageNodeJsDistributableExploded") {
    group = "package"
    description = "Package app using a node_modules directory"
    dependsOn("productionExecutableCompileSync")
    val distDir = buildDir.resolve(name)
    val nodeModulesDir = jsBuildOutput.resolve("node_modules")
    inputs.dir(nodeModulesDir)
    inputs.property("moduleName", nodeJsApplicationExtension.moduleName)
    outputs.dir(distDir)

    doLast {
        copy {
            from(nodeModulesDir)
            into(distDir.resolve("node_modules"))
        }
        distDir.resolve("index.js").printWriter().use { pw ->
            if (nodeJsApplicationExtension.sourceMap.get()) {
                pw.println("require('source-map-support').install()")
            }
            pw.println("require('${nodeJsApplicationExtension.moduleName.get()}')")
        }
    }
}

val installNccTask = tasks.register<NpmTask>("installNCC") {
    val toolDir = buildDir.resolve(name)
    val nccScript = toolDir.resolve("node_modules/@vercel/ncc/dist/ncc/cli.js")

    // every run of @vercel/ncc touches the v8 cache files, so we can't simply `outputs.dir(toolDir)` here
    outputs.file(nccScript)

    workingDir.set(toolDir)
    npmCommand.set(listOf("install"))
    args.set(listOf("@vercel/ncc"))

    doFirst {
        delete(toolDir)
        toolDir.mkdirs()
    }

    doLast {
        check(nccScript.exists()) { "npm install did not produce a @vercel/ncc executable" }
    }
}

val packageWithNccTask = tasks.register<NodeTask>("packageNodeJsDistributableWithNCC") {
    group = "package"
    description = "Package app as a single file using BCC"

    dependsOn(installNccTask)
    dependsOn("productionExecutableCompileSync")

    val toolDir = buildDir.resolve(installNccTask.name)
    val distDir = buildDir.resolve(name)
    val jsBuildFile = nodeJsApplicationExtension.moduleName.map { jsBuildOutput.resolve("packages/$it/kotlin/$it.js") }

    inputs.dir(jsBuildOutput.resolve("node_modules"))
    outputs.dir(distDir)

    doFirst {
        delete(distDir)
    }
    script.set(toolDir.resolve("node_modules/@vercel/ncc/dist/ncc/cli.js"))
    args.add("build")
    args.add(jsBuildFile.map { it.toString() })
    args.add("-o")
    args.add(distDir.toString())
    if (nodeJsApplicationExtension.minify.get()) {
        args.add("-m")
        args.add("--license")
        args.add("LICENSE.txt")
    }
    if (nodeJsApplicationExtension.v8cache.get()) {
        args.add("--v8-cache")
    }
    if (nodeJsApplicationExtension.target.isPresent) {
        args.add("--target")
        args.add(nodeJsApplicationExtension.target.get())
    }
    if (nodeJsApplicationExtension.sourceMap.get()) {
        args.add("-s")
    }
    for (module in nodeJsApplicationExtension.externalModules.get()) {
        args.add("-e")
        args.add(module)
    }
    doLast {
        if (nodeJsApplicationExtension.v8cache.get()) {
            for (file in fileTree(distDir)) {
                logger.info("set permissions of $file")
                Files.setPosixFilePermissions(
                    file.toPath(), setOf(
                        PosixFilePermission.OWNER_READ,
                        PosixFilePermission.GROUP_READ,
                        PosixFilePermission.OTHERS_READ,
                    )
                )
            }
        }
    }
}

val nodejsDistributable = tasks.register("nodejsDistributable", Zip::class.java) {
    group = "package"
    description = "Produce app distributable archive"

    destinationDirectory.set(buildDir)
    archiveAppendix.set("nodejs")
    includeEmptyDirs = false

    from(if (nodeJsApplicationExtension.useNcc.get()) packageWithNccTask else packageExplodedTask)
}

tasks.named("assemble").configure {
    dependsOn(nodejsDistributable)
}
