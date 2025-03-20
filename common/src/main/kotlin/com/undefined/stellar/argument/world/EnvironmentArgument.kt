package com.undefined.stellar.argument.world

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.NamespacedKey
import org.bukkit.World

/**
 * An argument that allows you to pass in a [NamespacedKey], which resolves into [World.Environment].
 */
class EnvironmentArgument(name: String) : AbstractStellarArgument<EnvironmentArgument, World.Environment>(name)