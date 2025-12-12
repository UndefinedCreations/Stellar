import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.tasks.DokkaBaseTask

plugins {
    id("setup")
    id("com.gradleup.shadow")
    `maven-publish`
    id("org.jetbrains.dokka") version "2.1.0"
    id("org.jetbrains.dokka-javadoc") version "2.1.0"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.19" apply false
    id("co.uzzu.dotenv.gradle") version "2.0.0"
}

private val submodules: HashMap<String, String> = hashMapOf(
    ":spigot:api" to "spigot",
    ":paper:api" to "paper",
)

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.brigadier)

    api(project(":spigot:api"))

    dokkaPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:2.1.0")
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
    tasks.withType<DokkaBaseTask>()

    tasks.register<Jar>("sourceJar") {
        archiveClassifier = "sources"
        from(sourceSets.main.get().allSource)
    }
}

val packageJavadoc by tasks.registering(Jar::class) {
    group = "stellar"
    archiveClassifier = "javadoc"

    dependsOn(tasks.dokkaGenerateJavadoc)
    from(tasks.dokkaGenerateModuleJavadoc.filter { "kotlin" !in it.path }.flatMap { it.outputDirectory })
}

val packageSources by tasks.registering(Jar::class) {
    group = "stellar"
    archiveClassifier = "sources"
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    from(subprojects.filter { "kotlin" !in it.path }.map { it.sourceSets.main.get().allSource })
}

val maven_username = if (env.isPresent("MAVEN_USERNAME")) env.fetch("MAVEN_USERNAME") else ""
val maven_password = if (env.isPresent("MAVEN_PASSWORD")) env.fetch("MAVEN_PASSWORD") else ""

publishing {
    publications {
        create<MavenPublication>("main") {
            artifactId = rootProject.name
            from(components["shadow"])
            artifact(packageJavadoc)
            artifact(packageSources)
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
                username = maven_username
                password = maven_password
            }
        }
        maven {
            name = "undefined-snapshots"
            url = uri("https://repo.undefinedcreations.com/snapshots")
            credentials(PasswordCredentials::class) {
                username = maven_username
                password = maven_password
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
