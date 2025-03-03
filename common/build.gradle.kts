plugins {
    id("setup")
}

repositories {
    maven {
        name = "minecraft-repo"
        url = uri("https://libraries.minecraft.net/")
    }
}

dependencies {
    compileOnly(libs.spigot)
    compileOnly(libs.brigadier)
    compileOnly(libs.authlib)
}