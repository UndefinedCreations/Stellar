package com.undefined.stellar.data.argument

import com.mojang.brigadier.context.CommandContext as BrigadierCommandContext
import com.undefined.stellar.NMSManager
import com.undefined.stellar.Stellar
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralArgument
import com.undefined.stellar.command.AbstractStellarCommand
import com.undefined.stellar.exception.DuplicateArgumentNameException
import org.bukkit.command.CommandSender
import java.lang.IllegalStateException

object MojangAdapter {

    fun getStellarCommandContext(context: BrigadierCommandContext<Any>): CommandContext<CommandSender> {
        val input = context.input.removePrefix("/")
        val rootNodeName = context.rootNode.name.takeIf { it.isNotBlank() }

        val baseCommand: AbstractStellarCommand<*> = Stellar.getStellarCommand(rootNodeName ?: context.nodes[0].node.name) ?: throw IllegalStateException("Could not find command!")
        val arguments = ArgumentHelper.getArguments(baseCommand, context, if (rootNodeName != null) 0 else 1)
        if (arguments.filter { it !is LiteralArgument }.groupingBy { it.name }.eachCount().any { it.value > 1 }) throw DuplicateArgumentNameException()

        val parsedArguments: CommandNode =
            arguments.associate<AbstractStellarArgument<*, *>, String, (CommandContext<CommandSender>) -> Any?> { argument ->
                Pair(argument.name) { context.getArgument(argument.name, Any::class.java) }
            } as CommandNode

        return CommandContext(parsedArguments, NMSManager.nms.getBukkitSender(context), input)
    }

}