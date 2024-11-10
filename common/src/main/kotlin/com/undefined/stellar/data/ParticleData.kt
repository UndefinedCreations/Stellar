package com.undefined.stellar.data

import org.bukkit.Particle

data class ParticleData<T>(val particle: Particle, val options: T)