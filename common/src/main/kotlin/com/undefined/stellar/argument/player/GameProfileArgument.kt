package com.undefined.stellar.argument.player

import com.mojang.authlib.GameProfile
import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import java.util.UUID

class GameProfileArgument(name: String) : AbstractStellarArgument<GameProfileArgument, GameProfile>(name)