plugins {
    id("setup")
//    id("publishing-convention")
    `maven-publish`
    id("com.gradleup.shadow") version "8.3.5"
}

dependencies {
    compileOnly(libs.papermc)
    compileOnly(libs.brigadier)
    api(project(":paper:lib"))
    api(project(":paper:v1_21_4"))
}

group = properties["group"]!!
version = properties["version"]!!

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = rootProject.name
            from(components["java"])
        }
    }
    repositories {
        repositories {
            maven {
                name = "undefined-repo"
                url = uri("https://repo.undefinedcreations.com/releases")
                credentials(PasswordCredentials::class) {
                    username = System.getenv("MAVEN_NAME") ?: property("mavenUser").toString()
                    password = System.getenv("MAVEN_SECRET") ?: property("mavenPassword").toString()
                }
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
        exclude("**/kotlin/**")
        archiveClassifier = "paper"
    }
}