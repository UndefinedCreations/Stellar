package com.undefined.stellar.data.argument

import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.command.AbstractStellarCommand

object ArgumentHelper {

    fun getArguments(
        baseCommand: AbstractStellarCommand<*>,
        context: CommandContext<Any>,
        currentIndex: Int,
        listOfArguments: List<AbstractStellarArgument<*, *>> = emptyList()
    ): List<AbstractStellarArgument<*, *>> {
        val currentNodeName = context.nodes.getOrNull(currentIndex)?.node?.name ?: return listOfArguments
        for (argument in baseCommand.arguments)
            if (argument.name == currentNodeName || currentNodeName in argument.aliases)
                return getArguments(argument, context, currentIndex + 1, listOfArguments + argument)
        return emptyList()
    }

}