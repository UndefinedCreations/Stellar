package com.undefined.stellar.v1_21_4

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.undefined.stellar.NMS
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer

object NMS1_21_4 : NMS {

    @Suppress("UNCHECKED_CAST")
    override fun register(command: LiteralArgumentBuilder<*>) {
        MinecraftServer.getServer().functions.dispatcher.register(command as LiteralArgumentBuilder<CommandSourceStack>)
    }
}