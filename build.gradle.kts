plugins {
    kotlin("multiplatform") version "2.2.21"
    kotlin("plugin.serialization") version "2.2.21"
    id("org.araqnid.kotlin-nodejs-application") version "0.1.0"
}

repositories {
    mavenCentral()
}

fun isPropertySet(key: String, valueIfNotSpecified: Boolean = false): Provider<Boolean> =
    providers.gradleProperty(key).map { it.toBoolean() }.orElse(valueIfNotSpecified)

kotlin {
    js {
        nodejs()
        useCommonJs()
        binaries.executable()
        binaries.withType<org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary>()
            .matching { it.name == "productionExecutable" }
            .configureEach {
                linkTask.configure {
                    compilerOptions.freeCompilerArgs.addAll(
                        isPropertySet("kotlin.fullMemberNames").map { fullMemberNames ->
                            if (fullMemberNames)
                                listOf("-Xir-minimized-member-names=false")
                            else
                                emptyList()
                        }
                    )
                }
            }
    }
}

dependencies {
    val kotlinxCoroutines = "1.10.2"
    val kotlinxSerialization = "1.9.0"
    val kotlinWrappers = "1.0.0-pre.710"

    "jsMainApi"(kotlin("stdlib-js"))
    "jsMainImplementation"(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:$kotlinxCoroutines"))
    "jsMainImplementation"("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    "jsMainImplementation"(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:$kotlinxSerialization"))
    "jsMainImplementation"("org.jetbrains.kotlinx:kotlinx-serialization-json")
    "jsMainImplementation"(platform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappers"))
    "jsMainImplementation"("org.jetbrains.kotlin-wrappers:kotlin-node")
    "jsMainImplementation"(npm("@aws-sdk/client-sso", "^3.279.0"))
    "jsMainImplementation"(npm("@aws-sdk/client-sts", "^3.279.0"))
    "jsMainImplementation"(npm("@aws-sdk/shared-ini-file-loader", "^3.272.0"))

    "jsTestImplementation"(kotlin("test-js"))
    "jsTestImplementation"("org.araqnid.kotlin.assert-that:assert-that:0.1.1")
    "jsTestImplementation"("org.jetbrains.kotlinx:kotlinx-coroutines-test")
}

tasks {
    register<Sync>("syncToDist") {
        from(named("packageNodeJsDistributableWithNCC"))
        into(layout.projectDirectory.dir("dist"))
    }

    named("assemble").configure {
        dependsOn("syncToDist")
    }
}

nodeJsApplication {
    v8cache.set(isPropertySet("nodejs.v8cache", valueIfNotSpecified = false))
    minify.set(isPropertySet("nodejs.minify", valueIfNotSpecified = true))
}

node {
    val nvmrc = providers.fileContents(layout.projectDirectory.file(".nvmrc")).asText.map { it.trim() }.orElse("")
    version.set(nvmrc)
    download.set(nvmrc.map { it.isNotEmpty() })
}
