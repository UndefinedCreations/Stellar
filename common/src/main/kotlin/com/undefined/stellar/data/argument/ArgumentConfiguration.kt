package com.undefined.stellar.data.argument

import com.undefined.stellar.StellarArgument

/**
 * Represents a lambda used to configure an argument after it's added.
 */
fun interface ArgumentConfiguration<T : StellarArgument<*>> {
    fun create(command: T)
}