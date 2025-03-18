package com.undefined.stellar.argument.math

import com.undefined.stellar.AbstractStellarArgument

/**
 * An argument that allows you to pass in a valid double range (e.g. 0.2..5.1), returning a [ClosedFloatingPointRange] of [Double]. Only works in Kotlin.
 */
class DoubleRangeArgument(name: String) : AbstractStellarArgument<DoubleRangeArgument, ClosedFloatingPointRange<Double>>(name)