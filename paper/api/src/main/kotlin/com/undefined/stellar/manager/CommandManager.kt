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
        "1.21.4" to com.undefined.stellar.v1_21_4.CommandRegistrar::class,
    )

    private val initializedPlugins: MutableList<JavaPlugin> = mutableListOf()
    fun initialize(plugin: JavaPlugin) {
        if (initializedPlugins.contains(plugin)) return
        initializedPlugins.add(plugin)
        Bukkit.getPluginManager().registerEvents(StellarListener, plugin)
    }
}