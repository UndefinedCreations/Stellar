package com.undefined.stellar.data.suggestion

data class Suggestion(val text: String, val tooltip: String) {

    companion object {
        fun withText(text: String): Suggestion = Suggestion(text, "")
    }

}