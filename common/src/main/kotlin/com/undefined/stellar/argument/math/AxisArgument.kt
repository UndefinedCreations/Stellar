package com.undefined.stellar.argument.math

import com.undefined.stellar.AbstractStellarArgument
import org.bukkit.Axis
import java.util.*

/**
 * An argument that allows you to pass in any non-repeating combination of the characters `x`, `y`, and `z`, returning an [EnumSet] of [Axis].
 */
class AxisArgument(name: String) : AbstractStellarArgument<AxisArgument, EnumSet<Axis>>(name)