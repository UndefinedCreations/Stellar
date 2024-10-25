# **Undefined Plugin Template** ![](https://repo.undefinedcreation.com/api/badge/latest/repo/com/redmagic/UndefinedAPI/) [![](https://dcbadge.limes.pink/api/server/https://discord.gg/NtWa9e3vv3?style=flat)](https://discord.gg/NtWa9e3vv3)

This template is created to be easily create new kotlin spigot plugins.

## **SetUp**

Go to `build.gradle.kts` and update all the dependencies to the lastse version. (See below)

```kotlin
dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    implementation("com.undefined:api:0.5.56:mapped")
}
```

Still inside you build file change the `group` to you group id. (See below)

```kotlin
group = "com.undefined"
```

Next go to the `settings.gradle.kts` and change the project name to your project. (See below) 

```kotlin
rootProject.name = "Template"
```

After that rename your folders to the same as your group id you set.

Then change your main class name.

Next go to your `plugin.yml` and change the project name and main class path. (See below)

```kotlin
name: Template
version: '0.0.1'
main: com.undefined.stellar.Template
api-version: '1.21'
```

Have fun coding :)