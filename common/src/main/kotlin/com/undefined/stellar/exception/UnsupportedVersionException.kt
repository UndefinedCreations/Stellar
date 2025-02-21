package com.undefined.stellar.exception

/**
 * This exception is thrown whenever this API is used on a version that is unsupported.
 */
class UnsupportedVersionException : RuntimeException("This version of Minecraft is unsupported by the Stellar API!")