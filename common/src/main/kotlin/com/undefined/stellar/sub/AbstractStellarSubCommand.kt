package com.undefined.stellar.sub

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.plugin.java.JavaPlugin

abstract class AbstractStellarSubCommand<T>(val parent: AbstractStellarCommand<*>, name: String) : AbstractStellarCommand<AbstractStellarSubCommand<T>>(name) {
    override fun getBase(): AbstractStellarCommand<*> = parent.getBase()
    override fun register(plugin: JavaPlugin) = parent.register(plugin)
}