package com.undefined.stellar.listener

import com.undefined.stellar.StellarCommand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.server.ServerCommandEvent
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
object StellarListener : Listener {

    @EventHandler
    fun onPlayerCommandPreprocess(event: PlayerCommandPreprocessEvent) {
        event.isCancelled = StellarCommand.parseAndReturnCancelled(event.player, event.message.removePrefix("/"))
    }

    @EventHandler
    fun onServerCommand(event: ServerCommandEvent) {
        event.isCancelled = StellarCommand.parseAndReturnCancelled(event.sender, event.command.removePrefix("/"))
    }

}