package com.undefined.stellar

import com.undefined.stellar.argument.custom.EnumFormatting
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.World.Environment
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.CompletableFuture

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test", "t")
            .addEnumArgument<Environment>("env", EnumFormatting.LOWERCASE)
            .addExecution<Player> {
                val env = getArgument<Environment>("env")
                sender.sendMessage(env.name)
            }
            .addStringArgument("test")
            .addFutureSuggestion<Player> {
                CompletableFuture.supplyAsync {
                    return@supplyAsync listOf(Suggestion.withText("test"))
                }
            }
            .addSuggestion("a")
            .register(this)
    }

}