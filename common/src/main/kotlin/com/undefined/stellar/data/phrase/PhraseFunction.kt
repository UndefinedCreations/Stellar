package com.undefined.stellar.data.phrase

import com.undefined.stellar.argument.phrase.WordArgument

/**
 * Represents a functional interface used when creating a [WordArgument].
 */
fun interface PhraseFunction {
	operator fun invoke(argument: WordArgument)
}