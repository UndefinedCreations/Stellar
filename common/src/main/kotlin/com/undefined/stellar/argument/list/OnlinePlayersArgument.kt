package com.undefined.stellar.argument.list

import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.argument.basic.StringType
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * An extension of [ListArgument] using [StringArgument] as a base. It lists the currently online players using `Bukkit.getServer().getOnlinePlayers`.
 *
 * @param name The name of the argument.
 * @param filter A function passing in a [Player] and returning a `boolean`. It filters any players that return `false` (default: exclude sender).
 * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
 */
class OnlinePlayersArgument(name: String, filter: CommandSender.(Player) -> Boolean = { it != this }, async: Boolean = false) : ListArgument<Player, String>(
    StringArgument(name, StringType.WORD),
    Bukkit.getServer().onlinePlayers,
    { it.takeIf { filter(this, it) }?.let { Suggestion.withText(it.name) } ?: Suggestion.empty() },
    { Bukkit.getPlayer(it) },
    async,
)