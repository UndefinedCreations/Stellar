package com.undefined.stellar

import com.undefined.stellar.argument.AbstractStellarArgument

abstract class StellarArgument(val type: AbstractStellarArgument<*, *>, val permissions: List<String>) {

    constructor(type: AbstractStellarArgument<*, *>, vararg permissions: String) : this(type, permissions.toList())

    val argument: AbstractStellarArgument<*, *> by lazy {
        setup().apply { addRequirements(*permissions.toTypedArray()) }
    }

    abstract fun setup(): AbstractStellarArgument<*, *>
    open fun arguments(): List<StellarArgument> = listOf()

    fun createArgument(init: AbstractStellarArgument<*, *>.() -> Unit): AbstractStellarArgument<*, *> = type.apply { init() }

    fun getFullArgument(): AbstractStellarArgument<*, *> {
        val command = setup()
        command.addRequirements(*permissions.toTypedArray())
        for (argument in arguments()) command.addArgument(argument.getFullArgument())
        return command
    }

}
