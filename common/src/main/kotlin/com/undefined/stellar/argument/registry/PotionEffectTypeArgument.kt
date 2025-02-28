package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.potion.PotionEffectType

class PotionEffectTypeArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<PotionEffectType>, PotionEffectType>(parent, name, Registry.EFFECT)