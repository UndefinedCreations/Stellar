package com.undefined.stellar.argument.basic

import com.mojang.brigadier.arguments.BoolArgumentType
import com.undefined.stellar.AbstractStellarArgument

class BooleanArgument(name: String) : AbstractStellarArgument<BooleanArgument, Boolean>(name, BoolArgumentType.bool())