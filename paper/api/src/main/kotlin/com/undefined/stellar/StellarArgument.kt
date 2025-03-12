package com.undefined.stellar

abstract class StellarArgument(val type: AbstractStellarArgument<*, *>, val permissions: List<String>) {

    constructor(type: AbstractStellarArgument<*, *>, vararg permissions: String) : this(type, permissions.toList())

    val fullArgument: AbstractStellarArgument<*, *> by lazy {
        setup().apply {
            addRequirements(*permissions.toTypedArray())
            for (argument in arguments()) addArgument(argument.fullArgument)
        }
    }

    abstract fun setup(): AbstractStellarArgument<*, *>
    open fun arguments(): List<StellarArgument> = listOf()

    fun createArgument(init: AbstractStellarArgument<*, *>.() -> Unit): AbstractStellarArgument<*, *> = type.apply { init() }

}
