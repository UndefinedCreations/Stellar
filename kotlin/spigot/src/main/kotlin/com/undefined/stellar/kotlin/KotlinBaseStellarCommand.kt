package com.undefined.stellar.kotlin

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.StellarCommandImpl
import org.jetbrains.annotations.ApiStatus

/**
 * An extension of [BaseStellarCommand] improved for Kotlin DSL.
 */
@ApiStatus.Experimental
abstract class KotlinBaseStellarCommand(name: String, permission: String = "", aliases: List<String> = listOf()) : BaseStellarCommand(name, permission, aliases) {

    /**
     * Creates a new [StellarCommandImpl] instance with the given configuration block.
     *
     * Usually, this is used along with the [setup] method as such:
     * ```kotlin
     * override fun setup() = kotlinCommand {
     *   // code logic
     * }
     * ```
     *
     * @param init A lambda to configure the command instance.
     * @return The configured [StellarCommandImpl].
     */
    fun kotlinCommand(init: KotlinStellarCommandImpl.() -> Unit): KotlinStellarCommandImpl = command(name) {
        aliases += this@KotlinBaseStellarCommand.aliases
        requires(permission)
        init()
    }

}