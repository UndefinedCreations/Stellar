plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(convertPlugin(libs.plugins.kotlin.jvm))
    implementation(convertPlugin(libs.plugins.detekt))
}

private fun convertPlugin(plugin: Provider<PluginDependency>): String {
    val id = plugin.get().pluginId
    return "$id:$id.gradle.plugin:${plugin.get().version}"
}