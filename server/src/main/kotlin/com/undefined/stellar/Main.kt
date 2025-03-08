package com.undefined.stellar

import com.undefined.stellar.argument.phrase.PhraseArgument
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addArgument(PhraseArgument("args"))
            .onWord(index = 0) { // Extension function of WordArgument
                addExecution<Player> { // extension function of PhraseCommandContext
                    this[0]
                }
            }
            .addWordSuggestions(index = 1, "first", "second")
            .register(this)
    }

}