package com.undefined.stellar.argument.math

import com.undefined.stellar.ParameterArgument
import org.bukkit.Axis
import java.util.*

/**
 * An argument that allows you to pass in any non-repeating combination of the characters `x`, `y`, and `z`, returning an [EnumSet] of [Axis].
 * @since 1.13
 */
class AxisArgument(name: String) : ParameterArgument<AxisArgument, EnumSet<Axis>>(name)