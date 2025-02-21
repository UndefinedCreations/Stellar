package com.undefined.stellar.exception

/**
 * This exception is thrown whenever an attempt is made to retrieve a value from a literal argument.
 */
class LiteralArgumentMismatchException : RuntimeException("This argument is of type Literal, thus you cannot get it's value!")