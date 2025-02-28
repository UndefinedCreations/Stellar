package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.entity.Wolf

class WolfVariantArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<Wolf.Variant>, Wolf.Variant>(parent, name, Registry.WOLF_VARIANT)