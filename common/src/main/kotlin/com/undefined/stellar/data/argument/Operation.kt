package com.undefined.stellar.data.argument

import kotlin.math.max
import kotlin.math.min

enum class Operation(val string: String, private val application: (a: Float, b: Float) -> Float) {
    ADD("+=", { a, b -> a + b }),
    SUBTRACT("-=", { a, b -> a - b }),
    MULTIPLY("*=", { a, b -> a * b }),
    DIVIDE("/=", { a, b -> a / b }),
    MODULO("%=", { a, b -> a % b }),
    ASSIGN("=", { _, b -> b }),
    LOWEST_VALUE("<", { a, b -> min(a, b) }),
    MAXIMUM_VALUE(">", { a, b -> max(a, b)}),
    SWAP("><", { _, b -> b });

    companion object {
        fun getOperation(string: String): Operation? = Operation.entries.firstOrNull { it.string == string }
    }

    fun apply(a: Float, b: Float): Float = application(a, b)
    fun apply(a: Int, b: Int): Float = apply(a.toFloat(), b.toFloat())
}