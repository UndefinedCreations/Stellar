package com.undefined.stellar.argument.registry

import com.undefined.stellar.AbstractStellarCommand
import org.bukkit.Registry
import org.bukkit.inventory.meta.trim.TrimPattern

@Suppress("UnstableApiUsage")
class TrimPatternArgument(parent: AbstractStellarCommand<*>, name: String) : RegistryArgument<Registry<TrimPattern>, TrimPattern>(parent, name, Registry.TRIM_PATTERN)