package com.undefined.stellar.argument

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.plugin.java.JavaPlugin

abstract class AbstractStellarArgument<T : AbstractStellarArgument<T, *>, R>(name: String) : AbstractStellarCommand<T>(name) {
    open lateinit var parent: AbstractStellarCommand<*>
    override fun register(plugin: JavaPlugin) = parent.register(plugin)
}