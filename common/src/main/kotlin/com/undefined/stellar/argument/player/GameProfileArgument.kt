package com.undefined.stellar.argument.player

import com.mojang.authlib.GameProfile
import com.undefined.stellar.AbstractStellarArgument

/**
 * An argument that functions as a target selector, such as EntityArgument and returns that player's [GameProfile].
 */
class GameProfileArgument(name: String) : AbstractStellarArgument<GameProfileArgument, GameProfile>(name)