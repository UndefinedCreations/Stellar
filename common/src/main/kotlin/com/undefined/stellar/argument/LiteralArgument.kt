package com.undefined.stellar.argument

import com.undefined.stellar.StellarArgument

/**
 * An argument that represents an option you can choose from.
 *
 * You cannot get the parsed value of this argument.
 */
class LiteralArgument(name: String) : StellarArgument<LiteralArgument>(name) {
    companion object {
        fun literal(name: String): LiteralArgument = LiteralArgument(name)
    }
}