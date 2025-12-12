package com.undefined.stellar.data.argument

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.undefined.stellar.*
import com.undefined.stellar.argument.LiteralArgument
import com.undefined.stellar.argument.list.ListArgument
import com.undefined.stellar.data.exception.DuplicateArgumentNameException
import com.undefined.stellar.nms.NMSHelper
import io.papermc.paper.command.brigadier.MessageComponentSerializer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import com.mojang.brigadier.context.CommandContext as BrigadierCommandContext

@ApiStatus.Internal
object MojangAdapter {

    private val UNKNOWN_COMMAND = DynamicCommandExceptionType { msg ->
        val message: String = msg as? String ?: throw error("Message must be string!")
        val component = Component.translatable("command.unknown.command")
            .appendNewline()
            .append(Component.text("... $message").color(NamedTextColor.GRAY))
            .append(Component.translatable("command.context.here"))
        MessageComponentSerializer.message().serialize(component).also { println("message: $it") }
    }

    fun getStellarCommandContext(context: BrigadierCommandContext<Any>): CommandContext<CommandSender> {
        val input = context.input.removePrefix("/")
        val rootNodeName = context.rootNode.name.takeIf { it.isNotBlank() }
        val sender = NMSHelper.getBukkitSender(context.source)

        val baseCommand: StellarCommand<*> = StellarConfig.getStellarCommand(rootNodeName ?: context.nodes[0].node.name) ?: throw IllegalStateException("Could not find command!")
        val arguments = ArgumentHelper.getArguments(baseCommand, context, if (rootNodeName != null) 0 else 1)
        if (arguments.filter { it !is LiteralArgument }.groupingBy { it.name }.eachCount().any { it.value > 1 }) throw DuplicateArgumentNameException()

        val parsedArguments: CommandTree =
            arguments.associate<StellarArgument<*>, String, (CommandContext<CommandSender>) -> Any> { argument ->
                Pair(argument.name) {
                    if (argument is ListArgument<*, *>) {
                        val argumentInput = NMSHelper.getArgumentInput(context, argument.name) ?: error("Could not get argument input (${argument.name})")

                        return@Pair argument.parseInternal(
                            sender,
                            NMSManager.nms.parseArgument(context, argument.base) ?: context.getArgument(argument.name, Any::class.java)
                        ) ?: throw UNKNOWN_COMMAND.create(argumentInput)
                    }
                    (NMSManager.nms.parseArgument(context, argument as ParameterArgument<*, *>) ?: context.getArgument(argument.name, Any::class.java)) ?: throw IllegalArgumentException("Argument return value cannot be null!")
                }
            } as CommandTree

        return CommandContext(context, parsedArguments, sender, input)
    }

}