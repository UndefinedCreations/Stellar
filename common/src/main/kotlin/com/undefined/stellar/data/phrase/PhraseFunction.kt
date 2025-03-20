package com.undefined.stellar.data.phrase

import com.undefined.stellar.argument.phrase.WordArgument

fun interface PhraseFunction {
	operator fun invoke(argument: WordArgument)
}