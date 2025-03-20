package com.undefined.stellar.argument.structure

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.block.structure.Mirror

/**
 * An argument that allows you to pass in one of the following: `none`, `front_back` and `left_right`, returning a [Mirror].
 */
class MirrorArgument(name: String) : AbstractStellarArgument<MirrorArgument, Mirror>(name)