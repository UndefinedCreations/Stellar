package com.undefined.stellar.sub.arguments.primitive

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.sub.AbstractStellarSubCommand

class StringSubCommand(parent: AbstractStellarCommand<*>, name: String, val type: StringType) : AbstractStellarSubCommand<StringSubCommand>(parent, name)

enum class StringType(vararg val examples: String) {
    SINGLE_WORD(*arrayOf("word", "words_with_underscores")),
    QUOTABLE_PHRASE(*arrayOf("\"quoted phrase\"", "word", "\"\"")),
    GREEDY_PHRASE(*arrayOf("word", "words with spaces", "\"and symbols\""));
}