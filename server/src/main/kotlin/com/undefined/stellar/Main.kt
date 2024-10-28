package com.undefined.stellar

import com.undefined.stellar.sub.brigadier.entity.EntityDisplayType
import com.undefined.stellar.sub.brigadier.primitive.StringType
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addStringListSubCommand("test", listOf("test", "other", "fdnajfjdas", "aj"))
            .addListExecution<Player> { string ->
                sendMessage(string)
            }
            .addUUIDListSubCommand("uuid", listOf(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()))
            .addListExecution<Player> { uuid ->
                sendMessage(uuid.toString())
            }
            .register()
    }

}