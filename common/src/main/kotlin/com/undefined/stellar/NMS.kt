package com.undefined.stellar

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.command.CommandSender

interface NMS {
    fun register(command: LiteralArgumentBuilder<*>)
    fun getArgumentType(argument: AbstractStellarArgument<*, *>): ArgumentType<*>
    fun getBukkitSender(context: CommandContext<Any>): CommandSender
}