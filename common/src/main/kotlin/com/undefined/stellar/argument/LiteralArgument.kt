package com.undefined.stellar.argument

import com.undefined.stellar.AbstractStellarArgument

/**
 * An argument that represents an option you can choose from.
 *
 * You cannot get the parsed value of this argument, if you try, you will get a [].
 */
class LiteralArgument(name: String) : AbstractStellarArgument<LiteralArgument>(name)