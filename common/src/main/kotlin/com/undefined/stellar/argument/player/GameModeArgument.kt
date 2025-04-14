package com.undefined.stellar.argument.player

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.GameMode

/**
 * An argument that allows you to pass in a one of the following: `survival`, `creative`, `adventure` or `spectator`. It returns the respective [GameMode].
 *
 * @since 1.19.3
 */
class GameModeArgument(name: String) : AbstractStellarArgument<GameModeArgument, Collection<GameMode>>(name)