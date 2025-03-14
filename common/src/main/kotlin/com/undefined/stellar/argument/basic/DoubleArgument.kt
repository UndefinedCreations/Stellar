package com.undefined.stellar.argument.basic

import com.mojang.brigadier.arguments.DoubleArgumentType
import com.undefined.stellar.AbstractStellarArgument

class DoubleArgument(name: String, minimum: Double = Double.MIN_VALUE, maximum: Double = Double.MAX_VALUE) : AbstractStellarArgument<DoubleArgument, Double>(name, DoubleArgumentType.doubleArg(minimum, maximum))