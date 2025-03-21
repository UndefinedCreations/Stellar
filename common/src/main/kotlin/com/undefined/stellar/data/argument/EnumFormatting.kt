package com.undefined.stellar.data.argument

/**
 * Dictates of an enum should be formatted when turned into a string.
 *
 * @param action A function that provides the initial enum name, and returns the modified name.
 */
enum class EnumFormatting(val action: (String) -> String) {
    /**
     * Alters the provided enum name into its [lowercase] version.
     */
    LOWERCASE({ it.lowercase() }),

    /**
     * Alters the provided enum name into its [uppercase] version.
     */
    UPPERCASE({ it.uppercase() }),

    /**
     * Alters the provided enum name into its capitalized version, where the first character is [uppercase] and the rest is [lowercase].
     */
    CAPITALIZED({ it.lowercase().replaceFirstChar { char -> char.uppercase() } }),

    /**
     * Doesn't alter the provided enum name.
     */
    NONE({ it }),
}