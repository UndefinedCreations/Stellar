package com.undefined.stellar.argument.player

import com.mojang.authlib.GameProfile
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class GameProfileArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<GameProfileArgument, GameProfile>(parent, name)