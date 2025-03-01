package com.undefined.stellar

import com.undefined.stellar.argument.ArgumentHandler
import org.bukkit.plugin.java.JavaPlugin

abstract class AbstractStellarCommand<T : AbstractStellarCommand<T>>(val name: String) : ArgumentHandler() {
    abstract fun register(plugin: JavaPlugin)
}