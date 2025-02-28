package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.entity.Frog

class FrogVariantArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<Frog.Variant>, Frog.Variant>(parent, name, Registry.FROG_VARIANT)