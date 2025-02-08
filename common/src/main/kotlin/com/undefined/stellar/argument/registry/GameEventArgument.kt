package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.GameEvent

class GameEventArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<GameEventArgument, GameEvent>(parent, name)