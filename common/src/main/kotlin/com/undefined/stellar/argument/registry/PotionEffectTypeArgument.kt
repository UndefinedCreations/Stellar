package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.potion.PotionEffectType

class PotionEffectTypeArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<PotionEffectTypeArgument, PotionEffectType>(parent, name)