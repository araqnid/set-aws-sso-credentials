plugins {
    kotlin("js") version "1.8.22"
    kotlin("plugin.serialization") version "1.8.22"
    id("org.araqnid.kotlin-nodejs-application") version "0.0.4"
}

repositories {
    mavenCentral()
}

fun isPropertySet(key: String, valueIfNotSpecified: Boolean = false): Provider<Boolean> =
    providers.gradleProperty(key).map { it.toBoolean() }.orElse(valueIfNotSpecified)

kotlin {
    js(IR) {
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
    val kotlinxCoroutines = "1.7.1"
    val kotlinxSerialization = "1.5.1"
    val kotlinWrappers = "1.0.0-pre.565"

    api(kotlin("stdlib-js"))
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:$kotlinxCoroutines"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:$kotlinxSerialization"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
    implementation(platform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappers"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-node")
    implementation(npm("@aws-sdk/client-sso", "^3.279.0"))
    implementation(npm("@aws-sdk/client-sts", "^3.279.0"))
    implementation(npm("@aws-sdk/shared-ini-file-loader", "^3.272.0"))

    testImplementation(kotlin("test-js"))
    testImplementation("org.araqnid.kotlin.assert-that:assert-that:0.1.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
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
