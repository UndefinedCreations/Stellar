package com.undefined.stellar.argument.math

import com.undefined.stellar.ParameterArgument

/**
 * An argument that allows you to pass in a valid double range (e.g. 0.2..5.1), returning a [ClosedFloatingPointRange] of [Double]. Only works in Kotlin.
 * @since 1.13
 */
class DoubleRangeArgument(name: String) : ParameterArgument<DoubleRangeArgument, ClosedFloatingPointRange<Double>>(name)