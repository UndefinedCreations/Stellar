package com.undefined.stellar.kotlin

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.StellarArgument
import com.undefined.stellar.StellarCommand
import org.jetbrains.annotations.ApiStatus

/**
 * An extension of [BaseStellarCommand] that improves Kotlin DSL.
 */
@ApiStatus.Experimental
abstract class DSLBaseStellarCommand(name: String, permission: String = "", aliases: List<String> = listOf()) : BaseStellarCommand(name, permission, aliases) {

    /**
     * Creates a new [StellarCommand] instance with the given configuration block.
     *
     * Usually, this is used along with the [setup] method as such:
     * ```kotlin
     * override fun setup() = kotlinCommand {
     *   // code logic
     * }
     * ```
     *
     * @param init A lambda to configure the command instance.
     * @return The configured [StellarCommand].
     */
    fun kotlinCommand(init: DSLStellarCommand.() -> Unit): DSLStellarCommand = command(name) {
        aliases += this@DSLBaseStellarCommand.aliases
        requires(permission)
        init()
    }

}