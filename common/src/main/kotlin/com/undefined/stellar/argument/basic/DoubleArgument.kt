package com.undefined.stellar.argument.basic

import com.mojang.brigadier.arguments.DoubleArgumentType
import com.undefined.stellar.AbstractStellarArgument

/**
 * An argument that allows you to type any valid [double](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html), and returns a primitive type of `double`.
 */
class DoubleArgument(name: String, minimum: Double = Double.MIN_VALUE, maximum: Double = Double.MAX_VALUE) : AbstractStellarArgument<DoubleArgument, Double>(name, DoubleArgumentType.doubleArg(minimum, maximum))