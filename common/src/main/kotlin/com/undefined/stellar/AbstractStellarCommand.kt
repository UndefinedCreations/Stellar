package com.undefined.stellar

import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.plugin.java.JavaPlugin

abstract class AbstractStellarCommand<T : AbstractStellarCommand<T>>(val name: String) {

    val arguments: MutableList<AbstractStellarArgument<*, *>> = mutableListOf()

    fun <T : AbstractStellarArgument<T, *>> addArgument(argument: T): T = argument.apply {
        argument.parent = this@AbstractStellarCommand
        this@AbstractStellarCommand.arguments.add(argument)
    }

    abstract fun register(plugin: JavaPlugin): T

}