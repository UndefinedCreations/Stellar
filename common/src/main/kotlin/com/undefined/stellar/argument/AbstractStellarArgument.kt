package com.undefined.stellar.argument

import com.mojang.brigadier.arguments.ArgumentType
import com.undefined.stellar.command.AbstractStellarCommand
import org.bukkit.plugin.java.JavaPlugin

abstract class AbstractStellarArgument<T : AbstractStellarArgument<T, *>, R>(name: String, val argumentType: ArgumentType<R>? = null) : AbstractStellarCommand<T>(name) {
    open lateinit var parent: AbstractStellarCommand<*>

    @Suppress("UNCHECKED_CAST")
    override fun register(plugin: JavaPlugin): T = apply { parent.register(plugin) } as T
}