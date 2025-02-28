package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.potion.PotionType

class PotionTypeArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<PotionType>, PotionType>(parent, name, Registry.POTION)