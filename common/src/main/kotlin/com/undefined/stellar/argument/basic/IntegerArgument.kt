package com.undefined.stellar.argument.basic

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.undefined.stellar.AbstractStellarArgument

/**
 * An argument that allows you to type any valid [int](https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html) (i.e. integer), and returns primitive type of `int`.
 * @since 1.13
 */
class IntegerArgument(name: String, minimum: Int = Int.MIN_VALUE, maximum: Int = Int.MAX_VALUE) : AbstractStellarArgument<IntegerArgument, Int>(name, IntegerArgumentType.integer(minimum, maximum))