package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.damage.DamageType

@Suppress("UnstableApiUsage")
class DamageTypeArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<DamageType>, DamageType>(parent, name, Registry.DAMAGE_TYPE)