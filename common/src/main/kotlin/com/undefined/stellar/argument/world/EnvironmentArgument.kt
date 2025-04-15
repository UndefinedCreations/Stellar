package com.undefined.stellar.argument.world

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.NamespacedKey
import org.bukkit.World

/**
 * An argument that allows you to pass in a [NamespacedKey], which resolves into [World.Environment].
 *
 * @since 1.13.1
 */
class EnvironmentArgument(name: String) : AbstractStellarArgument<EnvironmentArgument, World.Environment>(name)