package com.undefined.stellar.argument.math

import com.undefined.stellar.AbstractStellarArgument

/**
 * An argument that allows you to pass in a `long` suffixed with a unit if wanted (1s, 1d or 1t). Returning a `long` in ticks.
 */
class TimeArgument(name: String, val minimum: Int = 0) : AbstractStellarArgument<TimeArgument, Int>(name)