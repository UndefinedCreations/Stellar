package com.undefined.stellar.argument.basic

import com.mojang.brigadier.arguments.FloatArgumentType
import com.undefined.stellar.AbstractStellarArgument

class FloatArgument(name: String, minimum: Float = Float.MIN_VALUE, maximum: Float = Float.MAX_VALUE) : AbstractStellarArgument<FloatArgument, Float>(name, FloatArgumentType.floatArg(minimum, maximum))