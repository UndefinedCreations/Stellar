package com.undefined.stellar

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

interface NMS {
    fun getCommandDispatcher(): CommandDispatcher<Any>
    fun getArgumentType(argument: AbstractStellarArgument<*, *>): ArgumentType<*>
    fun getBukkitSender(source: Any): CommandSender
    fun hasPermission(player: Player, level: Int): Boolean
    fun getCommandSourceStack(sender: CommandSender): Any
}