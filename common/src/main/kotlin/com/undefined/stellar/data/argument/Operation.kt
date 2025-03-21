package com.undefined.stellar.data.argument

import kotlin.math.max
import kotlin.math.min

/**
 * Represents an operation (`+=`, `-=`), and allows you to calculate the result from two `float`s.
 *
 * @param string Represents the operation in string form, e.g. `+=`, `/=`, `>`, etc.
 */
enum class Operation(val string: String, private val application: (a: Float, b: Float) -> Float) {

    /**
     * Represents the addition operation: `a + b`.
     */
    ADD("+=", { a, b -> a + b }),

    /**
     * Represents the subtraction operation: `a - b`.
     */
    SUBTRACT("-=", { a, b -> a - b }),

    /**
     * Represents the multiplication operation: `a * b`.
     */
    MULTIPLY("*=", { a, b -> a * b }),

    /**
     * Represents the division operation, `a / b`.
     */
    DIVIDE("/=", { a, b -> a / b }),

    /**
     * Represents the modulo operation: `a % b`.
     */
    MODULO("%=", { a, b -> a % b }),

    /**
     * Represents the assignment operation, which in this case is the same as [SWAP]: `a = b`.
     */
    ASSIGN("=", { _, b -> b }),

    /**
     * Returns the lowest value of the two: `min(a, b)`.
     */
    LOWEST_VALUE("<", { a, b -> min(a, b) }),

    /**
     * Returns the highest value of the two: `max(a, b)`.
     */
    MAXIMUM_VALUE(">", { a, b -> max(a, b)}),

    /**
     * Represents the swapping operation, which in this case is the same as [ASSIGN]: `a = b`.
     */
    SWAP("><", { _, b -> b });

    /**
     * Returns the calculated number respective to the operation from two `float`s: [a] and [b].
     */
    fun apply(a: Float, b: Float): Float = application(a, b)

    /**
     * Returns the calculated number respective to the operation from two `int`s: [a] and [b].
     */
    fun apply(a: Int, b: Int): Float = apply(a.toFloat(), b.toFloat())

    companion object {
        /**
         * Gets the respective [Operation] from its string version, and returns null if not found.
         */
        fun getOperation(string: String): Operation? = Operation.entries.firstOrNull { it.string == string }
    }

}