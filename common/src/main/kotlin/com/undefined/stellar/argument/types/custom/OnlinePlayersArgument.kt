package com.undefined.stellar.argument.types.custom

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.types.primitive.StringArgument
import com.undefined.stellar.argument.types.primitive.StringType
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
    { Bukkit.getPlayer(it) },
    StringArgument(parent, name, StringType.SINGLE_WORD)
)
