package com.undefined.stellar.argument.basic

import com.mojang.brigadier.arguments.StringArgumentType
import com.undefined.stellar.AbstractStellarArgument

/**
 * An argument that allows you to type a string, as allowed by its [StringType], and returns that [String].
 * @since 1.13
 */
open class StringArgument(name: String, type: StringType = StringType.WORD) : AbstractStellarArgument<StringArgument, String>(name, type.argumentType)

/**
 * Dictates what type of string the player is allowed to input.
 */
enum class StringType(internal val argumentType: StringArgumentType?, vararg val examples: String) {
    /**
     * Allows you to type _one_ word, and only allows you to use characters from the english alphabet, and numbers from 0 through 9.
     */
    ALPHANUMERIC_WORD(StringArgumentType.word(), *arrayOf("word", "words_with_underscores")),
    /**
     * Allows you to type _one_ word, with no limits on what you can input.
     */
    WORD(null, *arrayOf("word!?", "words_with_underscores", "#f2f2f2")),
    /**
     * The same as [StringType.WORD], only when in parentheses, it doesn't have _any_ limits.
     */
    QUOTABLE_PHRASE(StringArgumentType.string(), *arrayOf("\"quoted phrase\"", "word", "\"\"")),
    /**
     * Allows you to type _anything_ you wish, including spaces, hence the phrase. Any arguments after this will **not** work.
     */
    PHRASE(StringArgumentType.greedyString(), *arrayOf("word", "words with spaces", "\"and symbols\""));
}