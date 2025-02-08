package com.undefined.stellar.argument.world

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.HeightMap

class HeightMapArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<HeightMapArgument, HeightMap>(parent, name)