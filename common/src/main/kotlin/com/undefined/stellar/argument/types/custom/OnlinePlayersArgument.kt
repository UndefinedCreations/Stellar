package com.undefined.stellar.argument.types.custom

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class OnlinePlayersArgument(
    parent: AbstractStellarCommand<*>,
    name: String,
    players: () -> List<Player>
) : ListArgument<Player>(
    parent,
    name,
    players,
    { it.name },
    { Bukkit.getPlayer(it) }
)
