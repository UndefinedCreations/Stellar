package com.undefined.stellar.argument.basic

import com.mojang.brigadier.arguments.LongArgumentType
import com.undefined.stellar.AbstractStellarArgument

/**
 * An argument that allows you to type any valid [long](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html), and returns a primitive type of `long`.
 */
class LongArgument(name: String, minimum: Long = Long.MIN_VALUE, maximum: Long = Long.MAX_VALUE) : AbstractStellarArgument<LongArgument, Long>(name, LongArgumentType.longArg(minimum, maximum))