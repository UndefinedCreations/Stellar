package com.undefined.stellar.argument.basic

import com.mojang.brigadier.arguments.StringArgumentType
import com.undefined.stellar.AbstractStellarArgument

open class StringArgument(name: String, type: StringType = StringType.WORD) : AbstractStellarArgument<StringArgument, String>(name, type.argumentType)

enum class StringType(internal val argumentType: StringArgumentType, vararg val examples: String) {
    WORD(StringArgumentType.word(), *arrayOf("word", "words_with_underscores")),
    QUOTABLE_PHRASE(StringArgumentType.string(), *arrayOf("\"quoted phrase\"", "word", "\"\"")),
    PHRASE(StringArgumentType.greedyString(), *arrayOf("word", "words with spaces", "\"and symbols\""));
}