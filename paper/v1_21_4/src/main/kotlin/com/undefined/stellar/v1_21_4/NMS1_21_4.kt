package com.undefined.stellar.v1_21_4

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.undefined.stellar.NMS
import com.undefined.stellar.argument.AbstractStellarArgument
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player

@Suppress("UNCHECKED_CAST")
object NMS1_21_4 : NMS {

    override fun getCommandDispatcher(): CommandDispatcher<Any> = MinecraftServer.getServer().functions.dispatcher as CommandDispatcher<Any>

    override fun getArgumentType(argument: AbstractStellarArgument<*, *>): ArgumentType<*> {
        TODO()
    }

    override fun getBukkitSender(source: Any): CommandSender = (source as CommandSourceStack).bukkitSender

    override fun hasPermission(player: Player, level: Int): Boolean = (player as CraftPlayer).handle.hasPermissions(level)

}