package com.undefined.stellar

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.undefined.stellar.argument.AbstractStellarArgument

interface NMS {
    fun register(command: LiteralArgumentBuilder<*>)
    fun getArgumentType(argument: AbstractStellarArgument<*, *>): ArgumentType<*>
}