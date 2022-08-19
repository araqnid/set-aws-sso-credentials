plugins {
    kotlin("js") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
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
        compilations["main"].packageJson {
        }
    }
}

dependencies {
    api(kotlin("stdlib-js"))
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.6.4"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:1.3.3"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
    implementation(npm("@aws-sdk/client-sso", "^3.145.0"))
    implementation(npm("@aws-sdk/shared-ini-file-loader", "^3.127.0"))
    implementation(npm("readlines", "^0.2.0"))

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
