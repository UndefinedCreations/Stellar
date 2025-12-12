package com.undefined.stellar.argument.world

import com.undefined.stellar.StellarCommand
import com.undefined.stellar.ParameterArgument
import org.bukkit.Particle

/**
 * An argument that allows you to pass in a [Particle], in the format of `particle_type_id{configuration tags}` for particle types with configurations, or the format of `particle_type_id` for particle types without configurations.
 */
class ParticleArgument(name: String) : ParameterArgument<ParticleArgument, Particle>(name)

/**
 * Adds a [ParticleArgument] to the command with the given name.
 * @return The created [ParticleArgument].
 */
fun StellarCommand<*>.addParticleArgument(name: String): ParticleArgument = addArgument(ParticleArgument(name))