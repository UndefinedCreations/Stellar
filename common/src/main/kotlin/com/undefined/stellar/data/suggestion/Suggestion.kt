package com.undefined.stellar.data.suggestion

/**
 * A data class representing a suggestion in a command.
 *
 * @param text The main text of the suggestion.
 * @param tooltip The tooltip of the suggestion, the suggestion will not include a tooltip if it is null.
 */
data class Suggestion(val text: String, val tooltip: String? = null) {
    companion object {
        /**
         * Returns a [Suggestion] with the text given.
         */
        fun withText(text: String): Suggestion = Suggestion(text)

        /**
         * Returns an empty [Suggestion] that will not show up.
         */
        fun empty(): Suggestion = Suggestion("")

        /**
         * Returns a [Suggestion] with the specified [text] and [tooltip].
         *
         * @param text The main text of the suggestion.
         * @param tooltip The tooltip of the suggestion, the suggestion will not include a tooltip if it is null.
         */
        fun create(text: String, tooltip: String? = null): Suggestion = Suggestion(text, tooltip)
    }
}

/**
 * Returns a [Suggestion] with the title from the [String].
 */
fun String.toSuggestion(): Suggestion = Suggestion.withText(this)