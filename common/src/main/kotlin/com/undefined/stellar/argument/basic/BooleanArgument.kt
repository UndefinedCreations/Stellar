package com.undefined.stellar.argument.basic

import com.mojang.brigadier.arguments.BoolArgumentType
import com.undefined.stellar.ParameterArgument

/**
 * An argument that allows you to type either `true` or `false`, and returns a primitive type of `boolean`.
 * @since 1.13
 */
class BooleanArgument(name: String) : ParameterArgument<BooleanArgument, Boolean>(name, BoolArgumentType.bool())