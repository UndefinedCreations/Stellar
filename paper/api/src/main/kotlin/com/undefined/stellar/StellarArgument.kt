package com.undefined.stellar

abstract class StellarArgument(val type: AbstractStellarArgument<*, *>, vararg val permissions: String) {

    val fullArgument: AbstractStellarArgument<*, *> by lazy {
        setup().apply {
            addRequirements(*permissions)
            for (argument in arguments()) addArgument(argument.fullArgument)
        }
    }

    abstract fun setup(): AbstractStellarArgument<*, *>
    open fun arguments(): List<StellarArgument> = listOf()

    fun createArgument(init: AbstractStellarArgument<*, *>.() -> Unit): AbstractStellarArgument<*, *> = type.apply { init() }

}