package com.undefined.stellar.exception

/**
 * This exception is thrown whenever the server is not `CraftServer`, which is obviously unsupported.
 */
class ServerTypeMismatchException : Exception("This server is not a CraftServer!")