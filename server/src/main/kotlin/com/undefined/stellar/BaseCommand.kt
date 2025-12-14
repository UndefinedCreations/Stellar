package com.undefined.stellar

import com.undefined.stellar.kotlin.KotlinBaseStellarCommand
import com.undefined.stellar.kotlin.literalArgument
import com.undefined.stellar.kotlin.runnable
import org.bukkit.entity.Player

object BaseCommand : KotlinBaseStellarCommand("base") {
    override fun setup(): StellarCommand = kotlinCommand {
        literalArgument("give") {
            runnable<Player> {
                val flags = getOrNull<String>("flags") ?: "FUCK"
                sender.sendMessage(flags)
                true
            }
            addPhraseArgument("flags")
                .addWordSuggestions(0, "-v", "-s")
                .addWordSuggestions(1, "-v", "-s")
        }
    }
}