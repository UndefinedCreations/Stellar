package com.undefined.stellar.kotlin

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.misc.RegistryArgument
import io.papermc.paper.registry.RegistryKey

/**
 * Adds a [RegistryArgument] to the command with the given name and [RegistryKey].
 * @return The created [RegistryArgument].
 */
fun AbstractStellarCommand<*>.registryArgument(name: String, registry: RegistryKey<*>): RegistryArgument = addArgument(RegistryArgument(name, registry))