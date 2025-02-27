package com.undefined.stellar.v1_20_2

import com.mojang.brigadier.CommandDispatcher
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.StellarCommands
import com.undefined.stellar.registrar.AbstractCommandRegistrar
import com.undefined.stellar.util.getPrivateField
import com.undefined.stellar.v1_20_2.BrigadierCommandHelper
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_20_R2.command.CraftCommandMap
import org.bukkit.plugin.java.JavaPlugin

object CommandRegistrar : AbstractCommandRegistrar {

    lateinit var plugin: JavaPlugin
    val dispatcher: CommandDispatcher<CommandSourceStack> by lazy { MinecraftServer.getServer().functions.dispatcher }

    override fun register(command: AbstractStellarCommand<*>, plugin: JavaPlugin) {
        this.plugin = plugin
        BrigadierCommandHelper.handleHelpTopic(command)
        for (name in command.aliases + command.name)
            dispatcher.register(CommandAdapter.getBaseCommand(command, name))
    }

    override fun unregister(name: String, plugin: JavaPlugin) {
        val map = plugin.server.pluginManager.getPrivateField<CraftCommandMap>("commandMap")
        val knownCommands: HashMap<String, Command> = map.knownCommands as HashMap<String, Command>
        for ((key, value) in knownCommands) if (key == name) value.unregister(map)
        knownCommands.remove(name)
    }

    override fun handleCommandFailure(sender: CommandSender, input: String): Boolean {
        val results = dispatcher.parse(input, BrigadierCommandHelper.COMMAND_SOURCE)
        val context = results.context.withSource(CommandContextAdapter.getCommandSourceStack(sender)).build(input)

        if (results.reader.remainingLength == 0) return false
        if (context.nodes.isEmpty()) return false

        val baseCommand: AbstractStellarCommand<*> = StellarCommands.getStellarCommand(context.nodes[0].node.name) ?: return false
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