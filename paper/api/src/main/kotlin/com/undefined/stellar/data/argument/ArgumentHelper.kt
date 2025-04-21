package com.undefined.stellar.data.argument

import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.ParameterArgument
import com.undefined.stellar.AbstractStellarCommand

object ArgumentHelper {

    fun getArguments(
        baseCommand: AbstractStellarCommand<*>,
        context: CommandContext<Any>,
        currentIndex: Int,
        listOfArguments: List<ParameterArgument<*, *>> = emptyList()
    ): List<ParameterArgument<*, *>> {
        val currentNodeName = context.nodes.getOrNull(currentIndex)?.node?.name ?: return listOfArguments
        for (argument in baseCommand.arguments)
            if (argument.name == currentNodeName || currentNodeName in argument.aliases)
                return getArguments(argument, context, currentIndex + 1, listOfArguments + argument)
        return emptyList()
    }

}