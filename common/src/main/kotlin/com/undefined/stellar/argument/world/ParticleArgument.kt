package com.undefined.stellar.argument.world

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.Particle

/**
 * An argument that allows you to pass in a [Particle], in the format of `particle_type_id{configuration tags}` for particle types with configurations, or the format of `particle_type_id` for particle types without configurations.
 */
class ParticleArgument(name: String) : AbstractStellarArgument<ParticleArgument, Particle>(name)