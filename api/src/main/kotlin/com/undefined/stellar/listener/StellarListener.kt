package com.undefined.stellar.listener

import com.undefined.stellar.StellarCommand
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

object StellarListener : Listener {

    @EventHandler
    fun onPlayerCommandPreprocess(event: PlayerCommandPreprocessEvent) {
        StellarCommand.parse(event.player, event.message)
    }

}