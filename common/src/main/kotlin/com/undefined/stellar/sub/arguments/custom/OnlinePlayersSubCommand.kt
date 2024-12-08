package com.undefined.stellar.sub.arguments.custom

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class OnlinePlayersSubCommand(
    parent: AbstractStellarCommand<*>,
    name: String,
    players: () -> List<Player>
) : ListSubCommand<Player>(
    parent,
    name,
    players,
    { it.name },
    { Bukkit.getPlayer(it) }
)
