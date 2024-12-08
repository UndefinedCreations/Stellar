package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.StellarCommands
import com.undefined.stellar.data.argument.CommandNode
import com.undefined.stellar.exception.LiteralArgumentMismatchException
import com.undefined.stellar.sub.LiteralStellarSubCommand
import com.undefined.stellar.sub.arguments.custom.CustomSubCommand
import io.papermc.paper.adventure.PaperAdventure
import net.kyori.adventure.identity.Identity
import net.minecraft.commands.CommandSource
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import org.bukkit.command.CommandSender

object CommandContextAdapter {

    fun getStellarCommandContext(context: CommandContext<CommandSourceStack>): com.undefined.stellar.data.argument.CommandContext<CommandSender> {
        val input = context.input.removePrefix("/")
        val baseCommand: AbstractStellarCommand<*> = StellarCommands.getStellarCommand(context.nodes[0].node.name)!!
        val arguments: CommandNode = CommandNode()
        arguments.putAll(
            BrigadierCommandHelper.getSubCommands(baseCommand, context)
                .associate { subCommand ->
                    if (subCommand is CustomSubCommand) return@associate Pair(subCommand.name) { subCommand.parse(it) }
                    if (subCommand is LiteralStellarSubCommand) return@associate Pair(subCommand.name) { throw LiteralArgumentMismatchException() }
                    Pair(subCommand.name) {
                        ArgumentHelper.getParsedArgument(context, subCommand)
                    }
                }
        )
        return com.undefined.stellar.data.argument.CommandContext(
            arguments,
            context.source.bukkitSender,
            input
        )
    }

    fun getCommandSourceStack(sender: CommandSender): CommandSourceStack {
        val overworld = MinecraftServer.getServer().overworld()
        return CommandSourceStack(
            Source(sender),
            Vec3.atLowerCornerOf(overworld.getSharedSpawnPos()),
            Vec2.ZERO,
            overworld,
            4,
            sender.name,
            Component.literal(sender.name),
            MinecraftServer.getServer(),
            null
        )
    }

    @JvmRecord
    private data class Source(val sender: CommandSender) : CommandSource {
        override fun sendSystemMessage(message: Component) {
            sender.sendMessage(Identity.nil(), PaperAdventure.asAdventure(message))
        }

        override fun acceptsSuccess(): Boolean {
            return true
        }

        override fun acceptsFailure(): Boolean {
            return true
        }

        override fun shouldInformAdmins(): Boolean {
            return false
        }

        override fun getBukkitSender(stack: CommandSourceStack): CommandSender {
            return this.sender
        }
    }

}