package com.undefined.stellar

/**
 * An abstract wrapper class used to create class argument along with [BaseStellarCommand].
 *
 * @property base The base argument type being wrapped.
 * @property permissions A vararg of [String] of required permissions to execute this command (optional).
 */
abstract class StellarArgument(val base: ParameterArgument<*, *>, vararg val permissions: String) {

    internal val fullArgument: ParameterArgument<*, *> by lazy {
        setup().apply {
            addRequirements(*permissions)
            for (argument in arguments()) addArgument(argument.fullArgument)
        }
    }

    /**
     * Sets up and returns the core [ParameterArgument] instance for this argument.
     * Called once during command initialization.
     */
    abstract fun setup(): ParameterArgument<*, *>

    /**
     * Optionally provides additional [StellarArgument]s to be added to the command.
     *
     * @return A list of additional [StellarArgument]s.
     */
    open fun arguments(): List<StellarArgument> = listOf()

    /**
     * Creates a new [ParameterArgument] instance with the given configuration block.
     *
     * Usually, this is used along with the [setup] method as such:
     * ```kotlin
     * override fun setup(): AbstractStellarArgument<*, *> = createArgument {
     *   // code logic
     * }
     * ```
     *
     * @param init A lambda to configure the argument instance.
     * @return The configured [StellarCommand].
     */
    fun createArgument(init: ParameterArgument<*, *>.() -> Unit): ParameterArgument<*, *> = base.apply { init() }

}