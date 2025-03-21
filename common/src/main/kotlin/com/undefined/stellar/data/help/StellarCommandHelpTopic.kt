package com.undefined.stellar.data.help

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.help.HelpTopic
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
class StellarCommandHelpTopic(
    name: String,
    description: String,
    information: Map<String, String>,
    val requirement: CommandSender.() -> Boolean
) : HelpTopic() {
    init {
        super.name = "/$name"
        super.shortText = description
        val fullDescription = StringBuilder()
        for ((key, value) in information)
            fullDescription.append("${ChatColor.GOLD}$key: ${ChatColor.WHITE}$value\n")
        fullText = fullDescription.toString()
    }

    override fun canSee(sender: CommandSender): Boolean {
        if (sender is ConsoleCommandSender) return true
        return requirement(sender)
    }
}