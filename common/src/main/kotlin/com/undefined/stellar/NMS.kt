package com.undefined.stellar

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.command.CommandSender

interface NMS {
    fun getCommandDispatcher(): CommandDispatcher<Any>
    fun getArgumentType(argument: AbstractStellarArgument<*, *>): ArgumentType<*>
    fun getBukkitSender(context: CommandContext<Any>): CommandSender
}