package com.undefined.stellar.argument.basic

import com.mojang.brigadier.arguments.FloatArgumentType
import com.undefined.stellar.ParameterArgument

/**
 * An argument that allows you to type any valid [float](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html), and returns a primitive type of `float`.
 * @since 1.13
 */
class FloatArgument(name: String, minimum: Float = Float.MIN_VALUE, maximum: Float = Float.MAX_VALUE) : ParameterArgument<FloatArgument, Float>(name, FloatArgumentType.floatArg(minimum, maximum))