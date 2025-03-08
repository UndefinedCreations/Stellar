package com.undefined.stellar.data.suggestion

data class Suggestion(val text: String, val tooltip: String? = null) {
    companion object {
        fun withText(text: String): Suggestion = Suggestion(text)
        fun empty(): Suggestion = Suggestion("")
        fun create(text: String, tooltip: String? = null): Suggestion = Suggestion(text, tooltip)
    }
}

fun String.toSuggestion(): Suggestion = Suggestion.withText(this)