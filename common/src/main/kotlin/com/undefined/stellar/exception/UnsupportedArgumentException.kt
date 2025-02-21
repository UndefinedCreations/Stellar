package com.undefined.stellar.exception

import com.undefined.stellar.argument.AbstractStellarArgument

/**
 * This exception is thrown whenever an argument is used that is not fully implemented by the Stellar API.
 * This is not supposed to happen, and if encountered, inform the current maintainers.
 */
class UnsupportedArgumentException(val argument: AbstractStellarArgument<*, *>) : RuntimeException("${argument::class.simpleName} is unsupported by Stellar! This is entirely unintentional behaviour.")