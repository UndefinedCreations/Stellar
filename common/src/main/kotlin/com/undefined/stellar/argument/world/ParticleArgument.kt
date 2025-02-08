package com.undefined.stellar.argument.world

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.Particle

class ParticleArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<ParticleArgument, Particle>(parent, name)