package com.undefined.stellar.argument.structure

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import org.bukkit.block.structure.Mirror

class MirrorArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<MirrorArgument, Mirror>(parent, name)