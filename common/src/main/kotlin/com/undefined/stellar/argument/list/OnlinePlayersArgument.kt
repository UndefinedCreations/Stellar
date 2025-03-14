package com.undefined.stellar.argument.list

import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.argument.basic.StringType
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class OnlinePlayersArgument(name: String, filter: (Player) -> Boolean = { true }, async: Boolean = false) : ListArgument<Player, String>(
    StringArgument(name, StringType.WORD),
    Bukkit.getServer().onlinePlayers,
    { it.takeIf(filter)?.let { Suggestion.withText(it.name) } ?: Suggestion.empty() },
    { Bukkit.getPlayer(it) },
    async,
)