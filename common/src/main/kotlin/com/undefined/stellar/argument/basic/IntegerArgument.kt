package com.undefined.stellar.argument.basic

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.undefined.stellar.AbstractStellarArgument

class IntegerArgument(name: String, minimum: Int = Int.MIN_VALUE, maximum: Int = Int.MAX_VALUE) : AbstractStellarArgument<IntegerArgument, Int>(name, IntegerArgumentType.integer(minimum, maximum))