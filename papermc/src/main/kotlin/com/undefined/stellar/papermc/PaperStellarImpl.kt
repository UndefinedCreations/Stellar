package com.undefined.stellar.papermc

import com.mojang.brigadier.tree.LiteralCommandNode
import com.undefined.stellar.papermc.command.PaperCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
class PaperStellarImpl(private val plugin: JavaPlugin) : PaperStellar {

    override fun plugin(): JavaPlugin = plugin
    override fun register(command: PaperCommand) {
        plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { commands ->
            commands.registrar().register(buildCommand(command))
        }
    }

    private fun buildCommand(command: PaperCommand): LiteralCommandNode<CommandSourceStack> =
        Commands.literal(command.name)
            .build()

}