package com.undefined.stellar.data.exception

import com.undefined.stellar.ParameterArgument

/**
 * This exception is thrown whenever an argument is used that is not supported for this specific version or not fully implemented by the Stellar API.
 */
class UnsupportedArgumentException(val argument: ParameterArgument<*, *>) : RuntimeException("${argument::class.simpleName} is unsupported by Stellar! Check if the argument you are using supports this Minecraft server version.")