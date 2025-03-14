package com.undefined.stellar.argument.basic

import com.mojang.brigadier.arguments.LongArgumentType
import com.undefined.stellar.AbstractStellarArgument

class LongArgument(name: String, minimum: Long = Long.MIN_VALUE, maximum: Long = Long.MAX_VALUE) : AbstractStellarArgument<LongArgument, Long>(name, LongArgumentType.longArg(minimum, maximum))