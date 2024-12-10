package com.undefined.stellar.argument.types.primitive

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument

class StringArgument(parent: AbstractStellarCommand<*>, name: String, val type: StringType) : AbstractStellarArgument<StringArgument>(parent, name)

enum class StringType(vararg val examples: String) {
    WORD(*arrayOf("word", "words_with_underscores")),
    QUOTABLE_PHRASE(*arrayOf("\"quoted phrase\"", "word", "\"\"")),
    PHRASE(*arrayOf("word", "words with spaces", "\"and symbols\""));
}