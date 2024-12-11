package com.undefined.stellar.data.requirement

import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
data class StellarRequirement<C : CommandSender>(val execution: C.() -> Boolean) {
    operator fun invoke(sender: CommandSender): Boolean {
        return execution(sender as? C ?: return true)
    }
}