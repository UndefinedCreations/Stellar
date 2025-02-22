package com.undefined.stellar.argument.player

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.GameMode

class GameModeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<GameModeArgument, GameMode>(parent, name)