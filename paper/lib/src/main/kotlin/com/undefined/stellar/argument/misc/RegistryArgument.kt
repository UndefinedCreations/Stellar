package com.undefined.stellar.argument.misc

import com.undefined.stellar.ParameterArgument
import com.undefined.stellar.AbstractStellarCommand
import io.papermc.paper.registry.RegistryKey
import org.bukkit.NamespacedKey

/**
 * @since 1.14
 */
class RegistryArgument(
    name: String,
    val registry: RegistryKey<*>,
) : ParameterArgument<RegistryArgument, NamespacedKey>(name)

/**
 * Adds a [RegistryArgument] to the command with the given name and [RegistryKey].
 * @return The created [RegistryArgument].
 */
fun AbstractStellarCommand<*>.addRegistryArgument(name: String, registry: RegistryKey<*>): RegistryArgument = addArgument(RegistryArgument(name, registry))