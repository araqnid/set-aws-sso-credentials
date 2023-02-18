plugins {
    kotlin("js") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
    id("nodejs-application")
}

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        nodejs()
        useCommonJs()
        binaries.executable()
        binaries.withType<org.jetbrains.kotlin.gradle.targets.js.ir.JsIrBinary>()
            .matching { it.name == "productionExecutable" }
            .configureEach {
                linkTask.configure {
                    val fullMemberNames = project.properties["kotlin.fullMemberNames"]
                    if (fullMemberNames is String && fullMemberNames.toBoolean()) {
                        compilerOptions.freeCompilerArgs.add("-Xir-minimized-member-names=false")
                    }
                }
            }
    }
}

dependencies {
    val kotlinxCoroutines = "1.6.4"
    val kotlinxSerialization = "1.4.1"
    val kotlinWrappers = "1.0.0-pre.495"

    api(kotlin("stdlib-js"))
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:$kotlinxCoroutines"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:$kotlinxSerialization"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
    implementation(platform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappers"))
    implementation("org.jetbrains.kotlin-wrappers:kotlin-node")
    implementation(npm("@aws-sdk/client-sso", "^3.267.0"))
    implementation(npm("@aws-sdk/shared-ini-file-loader", "^3.267.0"))

    testImplementation(kotlin("test-js"))
    testImplementation("org.araqnid.kotlin.assert-that:assert-that:0.1.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
}

nodeJsApplication {
    v8cache.set(false)
    minify.set(false)
    useNcc.set(properties["nodejs.package"] != "exploded")
    moduleName.set("set-aws-sso-credentials-kotlin")
}

node {
    val nodejsVersion = properties["nodejs.version"]
    if (nodejsVersion is String) {
        download.set(true)
        version.set(nodejsVersion)
    }
}