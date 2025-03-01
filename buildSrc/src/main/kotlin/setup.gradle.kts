import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    kotlin("jvm")
}

group = "com.undefined"
version = "0.1.0"

repositories {
    mavenCentral()
    mavenLocal()
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_1_8
    }
    compileJava {
        options.release = 8
    }
}

java {
    withSourcesJar()
    withJavadocJar()
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}