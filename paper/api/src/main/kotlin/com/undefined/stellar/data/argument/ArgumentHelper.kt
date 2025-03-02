package com.undefined.stellar.data.argument

import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.command.AbstractStellarCommand

object ArgumentHelper {

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