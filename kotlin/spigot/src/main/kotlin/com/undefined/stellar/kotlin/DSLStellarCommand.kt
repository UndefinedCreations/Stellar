package com.undefined.stellar.kotlin

import com.undefined.stellar.StellarCommand
import com.undefined.stellar.argument.LiteralArgument

/**
 * An extension of [StellarCommand] that improves Kotlin DSL.
 */
class DSLStellarCommand(name: String) : StellarCommand(name) {

    /**
     * The description of this command, stored in [StellarCommand.information]. This is mostly used by the command help topic.
     */
    var description: String?
        get() = information["Description"]
        set(value) {
            information["Description"] = value
        }

    /**
     * The usage of this command, stored in [StellarCommand.information]. This is mostly used by the command help topic.
     */
    var usage: String?
        get() = information["Usage"]
        set(value) {
            information["Usage"] = value
        }

    /**
     * Creates a [LiteralArgument] with [String].
     *
     * @return The created [LiteralArgument].
     */
    operator fun String.invoke(block: LiteralArgument.() -> Unit): LiteralArgument = addLiteralArgument(name).apply(block)

}