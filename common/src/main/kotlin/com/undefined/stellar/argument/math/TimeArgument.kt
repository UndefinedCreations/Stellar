package com.undefined.stellar.argument.math

import com.undefined.stellar.AbstractStellarArgument

class TimeArgument(name: String, val minimum: Int = 0) : AbstractStellarArgument<TimeArgument, Int>(name)