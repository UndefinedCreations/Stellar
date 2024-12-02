package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.StellarCommands
import com.undefined.stellar.data.argument.CommandNode
import com.undefined.stellar.sub.BaseStellarSubCommand
import com.undefined.stellar.sub.custom.CustomSubCommand
import net.minecraft.commands.CommandSourceStack

class CommandContextAdapter(private val context: CommandContext<CommandSourceStack>) {
    fun getStellarCommandContext(): com.undefined.stellar.data.argument.CommandContext {
//        val arguments: CommandNode = CommandNode()
//        arguments.addAll(context.nodes.map { ArgumentHelper.getParsedArgumentFromBrigadier(context, BrigadierCommandRegistrar.) })
        println(context.input.startsWith('/'))
        val input = context.input.removePrefix("/")
        val baseCommand: AbstractStellarCommand<*> = StellarCommands.getStellarCommand(input.substring(input.indexOf('/') + 1, input.indexOf(' ')))!!
        val arguments: CommandNode = CommandNode()
        arguments.addAll(BrigadierCommandRegistrar.getSubCommands(baseCommand, context).map {
            if (it is CustomSubCommand) return@map it.parse(context.source.bukkitSender, input)
            ArgumentHelper.getParsedArgumentFromSubCommand(context, it as? BaseStellarSubCommand ?: return@map null)
        })
        return com.undefined.stellar.data.argument.CommandContext(
            arguments,
            input
        )
    }
}