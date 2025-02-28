package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.inventory.meta.trim.TrimMaterial

@Suppress("UnstableApiUsage")
class TrimMaterialArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<TrimMaterial>, TrimMaterial>(parent, name, Registry.TRIM_MATERIAL)