package com.undefined.stellar

import com.undefined.stellar.sub.brigadier.entity.EntityDisplayType
import com.undefined.stellar.sub.brigadier.primitive.StringType
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")

            .addStringSubCommand("string", StringType.SINGLE_WORD)
            .addStringExecute<Player> { input ->
                sendMessage(input)
            }
            .addEntitySubCommand("entity", EntityDisplayType.ENTITIES)
            .addEntitySubCommand<Player> { entity ->
                println(entity.location)
            }
            .addIntegerSubCommand("integer", 10, 100)
            .addIntegerExecute<Player> { input ->
                sendMessage(input.toString())
            }
            .addGameProfileSubCommand("gameProfile")
            .addGameProfileExecute<Player> { gameProfile ->
                gameProfile.forEach { sendMessage(it.name) }
            }
            .addLocationSubCommand("location")
            .addLocationExecute<Player> {
                sendMessage(location.toString())
            }
            .register()
    }

}