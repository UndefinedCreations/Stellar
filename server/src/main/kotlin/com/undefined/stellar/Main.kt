package com.undefined.stellar

import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.World.Environment
import org.bukkit.entity.Player
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
            .addSuggestion(Suggestion.withText("text"))
            .register(this)
    }
}