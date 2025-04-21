import com.github.jengelman.gradle.plugins.shadow.ShadowJavaPlugin

plugins {
    id("setup")
    `publishing-convention`
}

val baseShadowJar by tasks.registering(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
    group = ShadowJavaPlugin.SHADOW_GROUP
    description = "Create a combined JAR of all SpigotMC dependencies."

    minimize {
        exclude("**/kotlin/**")
        exclude("**/intellij/**")
        exclude("**/jetbrains/**")
    }
    archiveClassifier = ""

    from(sourceSets.map { it.output })
    configurations = project.configurations.runtimeClasspath.map { listOf(it) }.get()
}

publishing {
    publications {
        create<MavenPublication>("baseJar") {
            artifactId = rootProject.name
//            from(components["shadow"])
            artifact(baseShadowJar)

            pom {
                name = "Stellar"
                description = "A simple, yet very powerful command framework for Kotlin."
                url = "https://www.github.com/UndefinedCreations/Stellar"
                licenses {
                    license {
                        name = "MIT"
                        url = "https://mit-license.org/"
                        distribution = "https://mit-license.org/"
                    }
                }
                developers {
                    developer {
                        id = "lutto"
                        name = "StillLutto"
                        url = "https://github.com/StillLutto/"
                    }
                }
                scm {
                    url = "https://github.com/UndefinedCreations/Stellar/"
                    connection = "scm:git:git://github.com/UndefinedCreations/Stellar.git"
                    developerConnection = "scm:git:ssh://git@github.com/UndefinedCreations/Stellar.git"
                }
            }
        }
    }
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.brigadier)
    api(project(":spigot:lib"))
    api(project(":spigot:v1_21_5"))
    api(project(":spigot:v1_21_4"))
    api(project(":spigot:v1_21_3"))
    api(project(":spigot:v1_21_1"))
    api(project(":spigot:v1_20_6"))
    api(project(":spigot:v1_20_4"))
    api(project(":spigot:v1_20_2"))
    api(project(":spigot:v1_20_1"))
    api(project(":spigot:v1_19_4"))
    api(project(":spigot:v1_19_3"))
    api(project(":spigot:v1_19_2"))
    api(project(":spigot:v1_18_2"))
    api(project(":spigot:v1_18_1"))
    api(project(":spigot:v1_17_1"))
    api(project(":spigot:v1_16_5"))
    api(project(":spigot:v1_16_4"))
    api(project(":spigot:v1_16_3"))
    api(project(":spigot:v1_16_1"))
    api(project(":spigot:v1_15_2"))
    api(project(":spigot:v1_15_1"))
    api(project(":spigot:v1_14_4"))
    api(project(":spigot:v1_14_3"))
    api(project(":spigot:v1_14_2"))
    api(project(":spigot:v1_14_1"))
    api(project(":spigot:v1_14"))
    api(project(":spigot:v1_13_2"))
    api(project(":spigot:v1_13_1"))
    api(project(":spigot:v1_13"))
}

tasks {
    shadowJar {
        minimize {
            exclude("**/kotlin/**")
            exclude("**/intellij/**")
            exclude("**/jetbrains/**")
        }
        archiveClassifier = "spigot"
    }
}