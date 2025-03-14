package com.undefined.stellar.data.argument

enum class EnumFormatting(val action: (String) -> String) {
    LOWERCASE({ it.lowercase() }),
    UPPERCASE({ it.uppercase() }),
    CAPITALIZED({ it.lowercase().replaceFirstChar { char -> char.uppercase() } }),
    NONE({ it }),
}