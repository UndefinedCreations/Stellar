package com.undefined.stellar.v1_13_1

import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.StellarCommands
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralStellarArgument
import com.undefined.stellar.data.argument.CommandNode
import com.undefined.stellar.data.argument.PhraseCommandContext
import com.undefined.stellar.exception.DuplicateArgumentNameException
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minecraft.server.v1_13_R2.*
import org.bukkit.command.CommandSender

object CommandContextAdapter {

    fun getStellarCommandContext(context: CommandContext<CommandListenerWrapper>): com.undefined.stellar.data.argument.CommandContext<CommandSender> {
        val input = context.input.removePrefix("/")
        val baseCommand: AbstractStellarCommand<*> = StellarCommands.getStellarCommand(context.nodes.entries.first().key.name.substringAfter(':'))!!
        val arguments = BrigadierCommandHelper.getArguments(baseCommand, context)
        if (arguments.filter { it !is LiteralStellarArgument }.groupingBy { it.name }.eachCount().any { it.value > 1 }) throw DuplicateArgumentNameException()
        val parsedArguments: CommandNode =
            BrigadierCommandHelper.getArguments(baseCommand, context)
                .associate<AbstractStellarArgument<*, *>, String, (com.undefined.stellar.data.argument.CommandContext<CommandSender>) -> Any?> { argument ->
                    Pair(argument.name) { ArgumentHelper.getParsedArgument(context, argument) }
                } as CommandNode
        return com.undefined.stellar.data.argument.CommandContext(
            parsedArguments,
            context.source.bukkitSender,
            input
        )
    }

    fun getGreedyCommandContext(context: CommandContext<CommandListenerWrapper>): PhraseCommandContext<CommandSender> {
        val stellarContext = getStellarCommandContext(context)
        val input = context.input.removePrefix("/")
        val words = input.split(' ').toMutableList()

        val totalOtherArguments = context.nodes.size - 1
        for (i in (1..totalOtherArguments)) words.removeFirst()
        return PhraseCommandContext(
            stellarContext,
            words,
            context.source.bukkitSender,
            input
        )
    }

    @Suppress("DEPRECATION")
    fun getCommandListenerWrapper(sender: CommandSender): CommandListenerWrapper {
        val overworld = MinecraftServer.getServer().getWorldServer(DimensionManager.OVERWORLD)
        return CommandListenerWrapper(
            Source(sender),
            Vec3D(overworld.spawn),
            Vec2F.a,
            overworld,
            4,
            sender.name,
            ChatComponentText(sender.name),
            MinecraftServer.getServer(),
            null
        )
    }

    private data class Source(val sender: CommandSender) : ICommandListener {
        override fun sendMessage(message: IChatBaseComponent) {
            this.sender.sendMessage(LegacyComponentSerializer.legacySection().serialize(asAdventure(message)))
        }
        override fun a(): Boolean = true
        override fun b(): Boolean = true
        override fun B_(): Boolean = false
        override fun getBukkitSender(stack: CommandListenerWrapper): CommandSender = this.sender
    }

    fun asAdventure(component: IChatBaseComponent): net.kyori.adventure.text.Component =
        GsonComponentSerializer.gson().deserializeFromTree(IChatBaseComponent.ChatSerializer.b(component))

}