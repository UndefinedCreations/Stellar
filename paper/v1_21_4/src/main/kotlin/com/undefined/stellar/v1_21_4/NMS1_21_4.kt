package com.undefined.stellar.v1_21_4

import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.NMS
import com.undefined.stellar.argument.AbstractStellarArgument
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
object NMS1_21_4 : NMS {

    @Suppress("UNCHECKED_CAST")
    override fun register(command: LiteralArgumentBuilder<*>) {
        MinecraftServer.getServer().functions.dispatcher.register(command as LiteralArgumentBuilder<CommandSourceStack>)
    }

    override fun getArgumentType(argument: AbstractStellarArgument<*, *>): ArgumentType<*> {
        TODO()
    }

    override fun getBukkitSender(context: CommandContext<Any>): CommandSender = (context as CommandContext<CommandSourceStack>).source.bukkitSender

}