package com.undefined.stellar.data.exception

import com.undefined.stellar.ParameterArgument

/**
 * This exception is thrown whenever an argument is used that is not fully implemented by the Stellar API.
 * This is not supposed to happen.
 */
class UnsupportedArgumentException(val argument: ParameterArgument<*, *>) : RuntimeException("${argument::class.simpleName} is unsupported by Stellar! This is entirely unintentional behaviour.")