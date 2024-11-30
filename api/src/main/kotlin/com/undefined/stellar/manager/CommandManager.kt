package com.undefined.stellar.manager

import com.undefined.stellar.listener.StellarListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

object CommandManager {
    val initializedPlugins: MutableList<JavaPlugin> = mutableListOf()
    fun initialize(plugin: JavaPlugin) {
        if (initializedPlugins.contains(plugin)) return
        initializedPlugins.add(plugin)
        Bukkit.getPluginManager().registerEvents(StellarListener, plugin)
    }
}