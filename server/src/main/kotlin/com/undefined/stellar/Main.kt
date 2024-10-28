package com.undefined.stellar

import com.undefined.stellar.sub.brigadier.entity.EntityDisplayType
import com.undefined.stellar.sub.brigadier.primitive.StringType
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

enum class Status {
    SUCCESS,
    FAILURE,
    PENDING
}

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addEnumSubCommand<Status>("enum", { enum -> enum.name.lowercase().replaceFirstChar { char -> char.uppercase() } }, { Status.valueOf(it.uppercase()) })
            .addEnumExecution<Player> { enum ->
                sendMessage(enum.name)
            }
            .register()
    }

}