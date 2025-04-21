package com.undefined.stellar.argument.structure

import com.undefined.stellar.ParameterArgument
import org.bukkit.block.structure.Mirror

/**
 * An argument that allows you to pass in one of the following: `none`, `front_back` and `left_right`, returning a [Mirror].
 *
 * @since 1.19
 */
class MirrorArgument(name: String) : ParameterArgument<MirrorArgument, Mirror>(name)