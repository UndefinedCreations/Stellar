package com.undefined.stellar.data.argument

import org.bukkit.Particle

data class ParticleData<T>(val particle: Particle, val options: T)