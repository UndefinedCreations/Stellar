package com.undefined.stellar.v1_20_4

import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.StellarCommands
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralStellarArgument
import com.undefined.stellar.argument.types.custom.CustomArgument
import com.undefined.stellar.data.argument.CommandNode
import com.undefined.stellar.data.argument.PhraseCommandContext
import com.undefined.stellar.exception.DuplicateArgumentNameException
import com.undefined.stellar.exception.LiteralArgumentMismatchException
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
        val arguments = BrigadierCommandHelper.getArguments(baseCommand, context)
        if (arguments.filter { it !is LiteralStellarArgument }.groupingBy { it.name }.eachCount().any { it.value > 1 }) throw DuplicateArgumentNameException()
        val parsedArguments: CommandNode =
            BrigadierCommandHelper.getArguments(baseCommand, context)
                .associate<AbstractStellarArgument<*>, String, (com.undefined.stellar.data.argument.CommandContext<CommandSender>) -> Any?> { argument ->
                    if (argument is CustomArgument) return@associate Pair(argument.name) { argument.parse(it) }
                    if (argument is LiteralStellarArgument) return@associate Pair(argument.name) { throw LiteralArgumentMismatchException() }
                    Pair(argument.name) {
                        ArgumentHelper.getParsedArgument(context, argument)
                    }
                } as CommandNode
        return com.undefined.stellar.data.argument.CommandContext(
            parsedArguments,
            context.source.bukkitSender,
            input
        )
    }

    fun getGreedyCommandContext(context: CommandContext<CommandSourceStack>): PhraseCommandContext<CommandSender> {
        val input = context.input.removePrefix("/")
        val words = input.split(' ').toMutableList()

        val totalOtherArguments = context.nodes.size - 1
        for (i in (1..totalOtherArguments)) words.removeFirst()
        return PhraseCommandContext(
            words,
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
    @Suppress("DEPRECATION")
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