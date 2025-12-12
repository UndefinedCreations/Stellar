package com.undefined.stellar.data.argument

import com.undefined.stellar.AbstractStellarArgument

/**
 * Represents a lambda used to configure an argument after it's added.
 */
fun interface ArgumentConfiguration<T : AbstractStellarArgument<*>> {
    fun create(command: T)
}