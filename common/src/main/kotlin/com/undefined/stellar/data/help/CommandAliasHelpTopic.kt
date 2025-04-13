package com.undefined.stellar.data.help

import com.google.common.base.Preconditions
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.help.HelpMap
import org.bukkit.help.HelpTopic
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
class CommandAliasHelpTopic(alias: String, aliasFor: String, private val helpMap: HelpMap) : HelpTopic() {
    private val aliasFor = if (aliasFor.startsWith("/")) aliasFor else "/$aliasFor"

    init {
        this.name = if (alias.startsWith("/")) alias else "/$alias"
        Preconditions.checkArgument(this.name != this.aliasFor, "Command %s cannot be alias for itself", this.name)
        this.shortText = "${ChatColor.YELLOW}Alias for ${ChatColor.WHITE}$aliasFor"
    }

    override fun getFullText(forWho: CommandSender): String {
        Preconditions.checkArgument(forWho != null, "CommandServer forWho cannot be null")
        val sb = StringBuilder(this.shortText)
        val aliasForTopic = helpMap.getHelpTopic(this.aliasFor)
        if (aliasForTopic != null) {
            sb.append("\n")
            sb.append(aliasForTopic.getFullText(forWho))
        }

        return sb.toString()
    }

    override fun canSee(sender: CommandSender): Boolean {
        if (this.amendedPermission == null) {
            val aliasForTopic = helpMap.getHelpTopic(this.aliasFor)
            return aliasForTopic?.canSee(sender) ?: false
        } else {
            return sender.hasPermission(this.amendedPermission)
        }
    }
}