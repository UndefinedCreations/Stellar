package com.undefined.stellar.kotlin

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.misc.RegistryArgument
import org.bukkit.Registry

/**
 * Adds a [RegistryArgument] to the command with the given name and [Registry].
 * @return The created [RegistryArgument].
 */
fun AbstractStellarCommand<*>.registryArgument(name: String, registry: Registry<*>): RegistryArgument = addArgument(RegistryArgument(name, registry))