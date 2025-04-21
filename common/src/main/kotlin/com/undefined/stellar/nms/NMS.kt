package com.undefined.stellar.nms

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.ParameterArgument
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

interface NMS {
    fun getCommandDispatcher(): CommandDispatcher<Any>
    fun getArgumentType(argument: ParameterArgument<*, *>, plugin: JavaPlugin): ArgumentType<*>
    fun parseArgument(ctx: CommandContext<Any>, argument: ParameterArgument<*, *>): Any?
    fun getCommandSourceStack(sender: CommandSender): Any
}