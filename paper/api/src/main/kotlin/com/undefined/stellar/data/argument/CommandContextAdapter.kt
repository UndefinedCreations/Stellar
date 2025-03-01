package com.undefined.stellar.data.argument

import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.NMSManager
import com.undefined.stellar.Stellar
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralArgument
import com.undefined.stellar.command.AbstractStellarCommand
import com.undefined.stellar.exception.DuplicateArgumentNameException
import org.bukkit.command.CommandSender

object CommandContextAdapter {

    fun getStellarCommandContext(context: CommandContext<Any>): com.undefined.stellar.data.argument.CommandContext<CommandSender> {
        val input = context.input.removePrefix("/")
        val baseCommand: AbstractStellarCommand<*> = Stellar.getStellarCommand(context.nodes[0].node.name.substringAfter(":"))!!
        val arguments = getArguments(baseCommand, context)
        if (arguments.filter { it !is LiteralArgument }.groupingBy { it.name }.eachCount().any { it.value > 1 }) throw DuplicateArgumentNameException()
        val parsedArguments: CommandNode =
            getArguments(baseCommand, context)
                .associate<AbstractStellarArgument<*, *>, String, (com.undefined.stellar.data.argument.CommandContext<CommandSender>) -> Any?> { argument ->
                    Pair(argument.name) { context.getArgument(argument.name, Any::class.java) }
                } as CommandNode
        return CommandContext(
            parsedArguments,
            NMSManager.nms.getBukkitSender(context),
            input
        )
    }

    fun getArguments(
        baseCommand: AbstractStellarCommand<*>,
        context: CommandContext<Any>,
        currentIndex: Int = 1,
        listOfArguments: List<AbstractStellarArgument<*, *>> = emptyList()
    ): List<AbstractStellarArgument<*, *>> {
        if (listOfArguments.size == context.nodes.size - 1) return listOfArguments
        for (argument in baseCommand.arguments)
            if (argument.name == context.nodes[currentIndex].node.name)
                return getArguments(argument, context, currentIndex + 1, listOfArguments + argument)
        return emptyList()
    }

}