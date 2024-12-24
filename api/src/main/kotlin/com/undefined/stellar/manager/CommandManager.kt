package com.undefined.stellar.manager

import com.undefined.stellar.listener.StellarListener
import com.undefined.stellar.registrar.AbstractCommandRegistrar
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import kotlin.reflect.KClass

@ApiStatus.Internal
object CommandManager {
    val registrars: Map<String, KClass<out AbstractCommandRegistrar>> = mapOf(
        "1.16.5" to com.undefined.stellar.v1_16_5.CommandRegistrar::class,
        "1.17" to com.undefined.stellar.v1_17.CommandRegistrar::class,
        "1.17.1" to com.undefined.stellar.v1_17_1.CommandRegistrar::class,
        "1.18" to com.undefined.stellar.v1_18_1.CommandRegistrar::class,
        "1.18.1" to com.undefined.stellar.v1_18_1.CommandRegistrar::class,
        "1.18.2" to com.undefined.stellar.v1_18_2.CommandRegistrar::class,
        "1.19" to com.undefined.stellar.v1_19_2.CommandRegistrar::class,
        "1.19.1" to com.undefined.stellar.v1_19_2.CommandRegistrar::class,
        "1.19.2" to com.undefined.stellar.v1_19_2.CommandRegistrar::class,
        "1.19.3" to com.undefined.stellar.v1_19_3.CommandRegistrar::class,
        "1.19.4" to com.undefined.stellar.v1_19_4.CommandRegistrar::class,
        "1.20" to com.undefined.stellar.v1_20.CommandRegistrar::class,
        "1.20.1" to com.undefined.stellar.v1_20_1.CommandRegistrar::class,
        "1.20.2" to com.undefined.stellar.v1_20_2.CommandRegistrar::class,
        "1.20.3" to com.undefined.stellar.v1_20_2.CommandRegistrar::class,
        "1.20.4" to com.undefined.stellar.v1_20_4.CommandRegistrar::class,
        "1.20.5" to com.undefined.stellar.v1_20_6.CommandRegistrar::class,
        "1.20.6" to com.undefined.stellar.v1_20_6.CommandRegistrar::class,
        "1.21" to com.undefined.stellar.v1_21.CommandRegistrar::class,
        "1.21.1" to com.undefined.stellar.v1_21_1.CommandRegistrar::class,
        "1.21.2" to com.undefined.stellar.v1_21_1.CommandRegistrar::class,
        "1.21.3" to com.undefined.stellar.v1_21_3.CommandRegistrar::class,
        "1.21.4" to com.undefined.stellar.v1_21_4.CommandRegistrar::class,
    )

    private val initializedPlugins: MutableList<JavaPlugin> = mutableListOf()
    fun initialize(plugin: JavaPlugin) {
        if (initializedPlugins.contains(plugin)) return
        initializedPlugins.add(plugin)
        Bukkit.getPluginManager().registerEvents(StellarListener, plugin)
    }
}