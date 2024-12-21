package com.undefined.stellar.manager

import com.undefined.stellar.listener.StellarListener
import com.undefined.stellar.registrar.AbstractCommandRegistrar
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
object CommandManager {
    val registrars: Map<String, AbstractCommandRegistrar> = mapOf(
        "1.18" to com.undefined.stellar.v1_18_1.CommandRegistrar,
        "1.18.1" to com.undefined.stellar.v1_18_1.CommandRegistrar,
        "1.18.2" to com.undefined.stellar.v1_18_2.CommandRegistrar,
        "1.19" to com.undefined.stellar.v1_19_2.CommandRegistrar,
        "1.19.1" to com.undefined.stellar.v1_19_2.CommandRegistrar,
        "1.19.2" to com.undefined.stellar.v1_19_2.CommandRegistrar,
        "1.19.3" to com.undefined.stellar.v1_19_3.CommandRegistrar,
        "1.19.4" to com.undefined.stellar.v1_19_4.CommandRegistrar,
        "1.20" to com.undefined.stellar.v1_20.CommandRegistrar,
        "1.20.1" to com.undefined.stellar.v1_20_1.CommandRegistrar,
        "1.20.2" to com.undefined.stellar.v1_20_2.CommandRegistrar,
        "1.20.3" to com.undefined.stellar.v1_20_2.CommandRegistrar,
        "1.20.4" to com.undefined.stellar.v1_20_4.CommandRegistrar,
        "1.20.5" to com.undefined.stellar.v1_20_6.CommandRegistrar,
        "1.20.6" to com.undefined.stellar.v1_20_6.CommandRegistrar,
        "1.21" to com.undefined.stellar.v1_21.CommandRegistrar,
        "1.21.1" to com.undefined.stellar.v1_21_1.CommandRegistrar,
        "1.21.2" to com.undefined.stellar.v1_21_1.CommandRegistrar,
        "1.21.3" to com.undefined.stellar.v1_21_3.CommandRegistrar,
        "1.21.4" to com.undefined.stellar.v1_21_4.CommandRegistrar,
    )

    private val initializedPlugins: MutableList<JavaPlugin> = mutableListOf()
    fun initialize(plugin: JavaPlugin) {
        if (initializedPlugins.contains(plugin)) return
        initializedPlugins.add(plugin)
        Bukkit.getPluginManager().registerEvents(StellarListener, plugin)
    }
}