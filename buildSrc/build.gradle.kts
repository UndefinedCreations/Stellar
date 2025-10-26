plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:2.2.21")
    implementation("com.gradleup.shadow:com.gradleup.shadow.gradle.plugin:9.2.2")
}