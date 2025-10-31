plugins {
    kotlin("multiplatform") version (libs.versions.kotlin)
    kotlin("plugin.serialization") version (libs.versions.kotlin)
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
    "jsMainApi"(kotlin("stdlib-js"))
    "jsMainImplementation"(platform(libs.coroutines.bom))
    "jsMainImplementation"("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    "jsMainImplementation"(platform(libs.serialization.bom))
    "jsMainImplementation"("org.jetbrains.kotlinx:kotlinx-serialization-json")
    "jsMainImplementation"(platform(libs.jswrappers.bom))
    "jsMainImplementation"("org.jetbrains.kotlin-wrappers:kotlin-node")
    "jsMainImplementation"(npm("@aws-sdk/client-sso", "^${libs.versions.aws.sdk.get()}"))
    "jsMainImplementation"(npm("@aws-sdk/client-sts", "^${libs.versions.aws.sdk.get()}"))

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
