package com.undefined.stellar.argument.misc

import com.undefined.stellar.AbstractStellarArgument
import io.papermc.paper.registry.RegistryKey
import org.bukkit.NamespacedKey

@Suppress("UnstableApiUsage", "DEPRECATION")
class RegistryArgument(
    name: String,
    val registry: RegistryKey<*>,
) : AbstractStellarArgument<RegistryArgument, NamespacedKey>(name)