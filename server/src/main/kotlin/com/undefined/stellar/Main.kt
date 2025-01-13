package com.undefined.stellar

import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.Bukkit
import org.bukkit.World.Environment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.CompletableFuture

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addEnumArgument<Environment>("env")
            .addExecution<Player> {
                val env = getArgument<Environment>("env")
                sender.sendMessage(env.name)
            }
            .addStringArgument("test")
            .addAsyncSuggestion<Player> {
                CompletableFuture.supplyAsync {
                    return@supplyAsync listOf(Suggestion.withText("test"))
                }
            }
            .addSuggestion("a")
            .register(this)
    }

}