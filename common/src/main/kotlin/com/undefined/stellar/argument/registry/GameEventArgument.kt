package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.GameEvent
import org.bukkit.Registry

class GameEventArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<GameEvent>, GameEvent>(parent, name, Registry.GAME_EVENT)