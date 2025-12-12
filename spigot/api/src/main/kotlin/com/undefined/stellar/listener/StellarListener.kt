package com.undefined.stellar.listener

import com.undefined.stellar.StellarCommand
import com.undefined.stellar.NMSManager
import com.undefined.stellar.StellarConfig
import com.undefined.stellar.data.argument.ArgumentHelper
import com.undefined.stellar.data.argument.MojangAdapter
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.server.ServerCommandEvent
import com.mojang.brigadier.context.CommandContext as BrigadierCommandContext

object StellarListener : Listener {

    var hasBeenInitialized = false

    @EventHandler
    fun onPlayerCommandPreprocess(event: PlayerCommandPreprocessEvent) {
        event.isCancelled = handleCommandFailure(event.player, event.message.removePrefix("/"))
    }

    @EventHandler
    fun onServerCommand(event: ServerCommandEvent) {
        event.isCancelled = handleCommandFailure(event.sender, event.command.removePrefix("/"))
    }

    private fun handleCommandFailure(sender: CommandSender, input: String): Boolean {
        val source = NMSManager.nms.getCommandSourceStack(sender)
        val results = NMSManager.nms.getCommandDispatcher().parse(input, source)
        val context = results.context.withSource(source).build(input)

        if (results.reader.remainingLength == 0) return false
        if (context.nodes.isEmpty()) return false

        val rootNodeName = context.rootNode.name.takeIf { it.isNotBlank() }
        val baseCommand: StellarCommand<*> = StellarConfig.getStellarCommand(rootNodeName ?: context.nodes[0].node.name) ?: return false
        val argument = ArgumentHelper.getArguments(baseCommand, context, if (rootNodeName != null) 0 else 1).lastOrNull()
        argument?.let {
            sendFailureExecutions(argument, context)
            if (argument.hideDefaultFailureMessages.hide) return true
        } ?: run {
            sendFailureExecutions(baseCommand, context)
            if (baseCommand.hideDefaultFailureMessages.hide) return true
        }

        return baseCommand.hasGlobalHiddenDefaultFailureMessages()
    }

    fun sendFailureExecutions(command: StellarCommand<*>, context: BrigadierCommandContext<Any>) {
        val stellarContext = MojangAdapter.getStellarCommandContext(context)
        for (execution in command.failureExecutions) execution(stellarContext)
        for (execution in command.globalFailureExecutions) execution(stellarContext)
    }

}