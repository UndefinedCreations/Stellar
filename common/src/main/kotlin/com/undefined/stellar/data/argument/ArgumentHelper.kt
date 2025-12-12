package com.undefined.stellar.data.argument

import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.StellarArgument
import com.undefined.stellar.StellarCommand
import org.jetbrains.annotations.ApiStatus

object ArgumentHelper {

    @ApiStatus.Internal
    tailrec fun getArguments(
        baseCommand: StellarCommand<*>,
        context: CommandContext<Any>,
        currentIndex: Int,
        listOfArguments: List<StellarArgument<*>> = emptyList()
    ): List<StellarArgument<*>> {
        val currentNodeName = context.nodes.getOrNull(currentIndex)?.node?.name ?: return listOfArguments
        for (argument in baseCommand.arguments)
            if (argument.name == currentNodeName || currentNodeName in argument.aliases)
                return getArguments(argument, context, currentIndex + 1, listOfArguments + argument)
        return emptyList()
    }

    @ApiStatus.Internal
    tailrec fun getCommandAndArguments(command: StellarCommand<*>, arguments: List<StellarArgument<*>> = listOf()): List<StellarCommand<*>> =
        if (command !is StellarArgument<*>) arguments
        else getCommandAndArguments(command.parent, arguments + command)

}