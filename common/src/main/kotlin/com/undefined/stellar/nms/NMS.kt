package com.undefined.stellar.nms

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

interface NMS {
    fun getCommandDispatcher(): CommandDispatcher<Any>
    fun getArgumentType(argument: AbstractStellarArgument<*, *>, plugin: JavaPlugin): ArgumentType<*>
    fun parseArgument(ctx: CommandContext<Any>, argument: AbstractStellarArgument<*, *>): Any?
    fun getBukkitSender(source: Any): CommandSender
    fun hasPermission(player: Player, level: Int): Boolean
    fun getCommandSourceStack(sender: CommandSender): Any
}