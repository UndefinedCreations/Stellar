package com.undefined.stellar.argument.player

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import java.util.UUID

class GameModeArgument(name: String) : AbstractStellarArgument<GameModeArgument, Collection<GameMode>>(name)