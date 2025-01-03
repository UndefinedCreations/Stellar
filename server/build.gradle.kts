plugins {
    kotlin("jvm") version "1.9.22"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("com.gradleup.shadow") version "8.3.5"
}

val versionVar = version
val groupIdVar = "com.undefined"
val artifactIdVar = "stellar"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")

    implementation(project(":api"))
    implementation(project(":common"))
    implementation(project(":v1_13"))
    implementation(project(":v1_13_1"))
    implementation(project(":v1_13_2"))
    implementation(project(":v1_14_1"))
    implementation(project(":v1_14_2"))
    implementation(project(":v1_14_3"))
    implementation(project(":v1_14_4"))
    implementation(project(":v1_15"))
    implementation(project(":v1_15_1"))
    implementation(project(":v1_15_2"))
    implementation(project(":v1_16_1"))
    implementation(project(":v1_16_2"))
    implementation(project(":v1_16_3"))
    implementation(project(":v1_16_4"))
    implementation(project(":v1_16_5"))
    implementation(project(":v1_17"))
    implementation(project(":v1_17_1:", "reobf"))
    implementation(project(":v1_18_1:", "reobf"))
    implementation(project(":v1_18_2:", "reobf"))
    implementation(project(":v1_19_2:", "reobf"))
    implementation(project(":v1_19_3:", "reobf"))
    implementation(project(":v1_19_4:", "reobf"))
    implementation(project(":v1_20:", "reobf"))
    implementation(project(":v1_20_1:", "reobf"))
    implementation(project(":v1_20_2:", "reobf"))
    implementation(project(":v1_20_4:", "reobf"))
    implementation(project(":v1_20_6:", "reobf"))
    implementation(project(":v1_21:", "reobf"))
    implementation(project(":v1_21_1:", "reobf"))
    implementation(project(":v1_21_3:", "reobf"))
    implementation(project(":v1_21_4:"))
    compileOnly(project(":v1_20_6"))
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }

    val shadowJar by getting(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
        archiveFileName.set("Stellar-shadow.jar")
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }

    compileJava {
        options.release = 21
    }

    runServer {
        minecraftVersion("1.20.6")
        jvmArgs("-Xmx4G")
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}