package com.undefined.stellar.v1_19_2

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.StellarCommands
import com.undefined.stellar.registrar.AbstractCommandRegistrar
import com.undefined.stellar.v1_19_2.BrigadierCommandHelper.dispatcher
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

object CommandRegistrar : AbstractCommandRegistrar {

    lateinit var plugin: JavaPlugin

    override fun register(command: AbstractStellarCommand<*>, plugin: JavaPlugin) {
        this.plugin = plugin
        BrigadierCommandHelper.handleHelpTopic(command)
        for (name in command.aliases + command.name)
            BrigadierCommandHelper.register(CommandAdapter.getBaseCommand(command, name))
    }

    override fun handleCommandFailure(sender: CommandSender, input: String): Boolean {
        val results = dispatcher.parse(input, BrigadierCommandHelper.COMMAND_SOURCE)
        val context = results.context.withSource(CommandContextAdapter.getCommandSourceStack(sender)).build(input)

        if (results.reader.remainingLength == 0) return false
        if (context.nodes.isEmpty()) return false

        val baseCommand: AbstractStellarCommand<*> = StellarCommands.getStellarCommand(context.nodes[0].node.name)!!
        val argument = BrigadierCommandHelper.getArguments(baseCommand, context).lastOrNull()
        argument?.let {
            BrigadierCommandHelper.handleFailureMessageAndExecutions(argument, context)
            if (argument.hideDefaultFailureMessages.hide) return true
        } ?: run {
            BrigadierCommandHelper.handleFailureMessageAndExecutions(baseCommand, context)
            if (baseCommand.hideDefaultFailureMessages.hide) return true
        }

        return baseCommand.hasGlobalHiddenDefaultFailureMessages()
    }

}