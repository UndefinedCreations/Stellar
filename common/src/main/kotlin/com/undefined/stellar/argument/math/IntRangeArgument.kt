package com.undefined.stellar.argument.math

import com.undefined.stellar.ParameterArgument

/**
 * An argument that allows you to pass in a valid integer range (e.g. 0..5), returning an [IntRange]. Only works in Kotlin.
 */
class IntRangeArgument(name: String) : ParameterArgument<IntRangeArgument, IntRange>(name)