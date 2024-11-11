package com.undefined.stellar.data.arguments

import org.bukkit.Particle

data class ParticleData<T>(val particle: Particle, val options: T)