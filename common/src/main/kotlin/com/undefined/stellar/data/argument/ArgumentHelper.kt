package com.undefined.stellar.data.argument

import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.AbstractStellarArgument
import com.undefined.stellar.AbstractStellarCommand
import org.jetbrains.annotations.ApiStatus

object ArgumentHelper {

    @ApiStatus.Internal
    tailrec fun getArguments(
        baseCommand: AbstractStellarCommand<*>,
        context: CommandContext<Any>,
        currentIndex: Int,
        listOfArguments: List<AbstractStellarArgument<*>> = emptyList()
    ): List<AbstractStellarArgument<*>> {
        val currentNodeName = context.nodes.getOrNull(currentIndex)?.node?.name ?: return listOfArguments
        for (argument in baseCommand.arguments)
            if (argument.name == currentNodeName || currentNodeName in argument.aliases)
                return getArguments(argument, context, currentIndex + 1, listOfArguments + argument)
        return emptyList()
    }

    @ApiStatus.Internal
    tailrec fun getCommandAndArguments(command: AbstractStellarCommand<*>, arguments: List<AbstractStellarArgument<*>> = listOf()): List<AbstractStellarCommand<*>> =
        if (command !is AbstractStellarArgument<*>) arguments
        else getCommandAndArguments(command.parent, arguments + command)

}