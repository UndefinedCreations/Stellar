package com.undefined.stellar.argument.world

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.World.Environment

class EnvironmentArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<EnvironmentArgument, Environment>(parent, name)