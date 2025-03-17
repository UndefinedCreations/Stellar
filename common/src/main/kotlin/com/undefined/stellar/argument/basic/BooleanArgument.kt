package com.undefined.stellar.argument.basic

import com.mojang.brigadier.arguments.BoolArgumentType
import com.undefined.stellar.AbstractStellarArgument

/**
 * An argument that allows you to type either `true` or `false`, and returns a primitive type of `boolean`.
 */
class BooleanArgument(name: String) : AbstractStellarArgument<BooleanArgument, Boolean>(name, BoolArgumentType.bool())