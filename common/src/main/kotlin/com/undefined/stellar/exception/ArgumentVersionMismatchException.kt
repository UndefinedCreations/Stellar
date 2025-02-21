package com.undefined.stellar.exception

import com.undefined.stellar.argument.AbstractStellarArgument

/**
 * This exception is thrown when a specific argument is not support for your Minecraft server version.
 */
class ArgumentVersionMismatchException(val argument: AbstractStellarArgument<*, *>, version: String) : RuntimeException("Argument ${argument::class.simpleName} is not supported for version $version")