package com.undefined.stellar.data.argument

import org.bukkit.Particle

/**
 * Represents a class containing the [particle] and the [options] of that particle, if any.
 */
data class ParticleData<T>(val particle: Particle, val options: T?)