package com.undefined.stellar.exception

import com.undefined.stellar.argument.AbstractStellarArgument

class ArgumentVersionMismatchException(val argument: AbstractStellarArgument<*, *>, version: String) : RuntimeException("Argument ${argument::class.simpleName} is not supported for version $version")