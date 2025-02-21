package com.undefined.stellar.exception

/**
 * This exception is thrown when there are multiple arguments with the same name.
 * This is not possible as specifying the name of an argument would get messy.
 */
class DuplicateArgumentNameException : RuntimeException("A command cannot have multiple arguments with the same name!")