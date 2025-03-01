package com.undefined.stellar.argument

abstract class ArgumentHandler {

    val arguments: MutableList<AbstractStellarArgument<*, *>> = mutableListOf()

    fun <T : AbstractStellarArgument<T, *>> addArgument(argument: T): T = argument.apply { arguments.add(argument) }

}