package com.undefined.stellar.exception

import com.undefined.stellar.argument.AbstractStellarArgument

class UnsupportedArgumentException(val argument: AbstractStellarArgument<*, *>) : RuntimeException("${argument::class.simpleName} is unsupported by Stellar! This is totally unintentional behaviour.")