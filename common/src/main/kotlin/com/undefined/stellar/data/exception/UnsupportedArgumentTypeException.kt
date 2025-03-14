package com.undefined.stellar.data.exception

import com.undefined.stellar.AbstractStellarArgument

/**
 * This exception is thrown whenever the type of list argument is a list itself.
 */
class UnsupportedArgumentTypeException(val argument: AbstractStellarArgument<*, *>) : RuntimeException("List argument doesn't support ${argument::class.simpleName}!")