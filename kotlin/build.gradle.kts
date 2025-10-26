plugins {
    java
    id("setup")
    `maven-publish`
    id("org.jetbrains.dokka") version "2.1.0"
    id("com.gradleup.shadow")
}

private val submodules: HashMap<String, String> = hashMapOf(
    ":kotlin:spigot" to "",
    ":kotlin:spigot" to "spigot",
    ":kotlin:paper" to "paper",
)

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.brigadier)

    api(project(":kotlin:spigot"))
}

val packageKotlinJavadoc by tasks.registering(Jar::class) {
    group = "stellar"
    archiveClassifier = "javadoc"

    dependsOn(tasks.dokkaJavadocCollector)
    from(tasks.dokkaJavadocCollector.flatMap { it.outputDirectory })
}

val packageKotlinSources by tasks.registering(Jar::class) {
    group = "stellar"
    archiveClassifier = "sources"
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    from(subprojects.map { it.sourceSets.main.get().allSource })
}

publishing {
    publications {
        create<MavenPublication>("kotlin") {
            artifactId = "${rootProject.name}-kotlin"
            from(components["shadow"])
            artifact(packageKotlinJavadoc)
            artifact(packageKotlinSources)
            for (module in submodules)
                artifact(project(module.key).layout.buildDirectory.dir("libs").get().file("stellar-$version.jar")) {
                    classifier = module.value
                }

            pom {
                name = "Stellar"
                description = " A simple, yet very powerful command framework for Kotlin."
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
                    developer {
                        id = "redmagic"
                        name = "TheRedMagic"
                        url = "https://github.com/TheRedMagic/"
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
    repositories {
        maven {
            name = "undefined-releases"
            url = uri("https://repo.undefinedcreations.com/releases")
            credentials(PasswordCredentials::class) {
                username = System.getenv("MAVEN_NAME") ?: property("mavenUser").toString()
                password = System.getenv("MAVEN_SECRET") ?: property("mavenPassword").toString()
            }
        }
        maven {
            name = "undefined-snapshots"
            url = uri("https://repo.undefinedcreations.com/snapshots")
            credentials(PasswordCredentials::class) {
                username = System.getenv("MAVEN_NAME") ?: property("mavenUser").toString()
                password = System.getenv("MAVEN_SECRET") ?: property("mavenPassword").toString()
            }
        }
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    shadowJar {
        minimize {
            exclude("**/kotlin/**")
            exclude("**/intellij/**")
            exclude("**/jetbrains/**")
        }
        archiveClassifier = ""

        for (module in submodules) dependsOn(project(module.key).tasks.named("shadowJar"))
    }
}