package com.undefined.stellar.kotlin

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.ParameterArgument
import com.undefined.stellar.StellarConfig
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.Suggestion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture

/**
 * Adds an execution to the command.
 *
 * @param C The type of CommandSender.
 * @param scope The [CoroutineScope] used to create
 * @param execution The execution block.
 * @return The modified command object.
 */
inline fun <reified C : CommandSender> AbstractStellarCommand<*>.execution(
    scope: CoroutineScope = StellarConfig.scope,
    noinline execution: suspend CommandContext<C>.() -> Unit,
): AbstractStellarCommand<*> = addExecution<C> {
    scope.launch {
        execution()
    }
}

/**
 * Adds an async execution to the command using [StellarConfig.asyncScope].
 *
 * @param C The type of CommandSender.
 * @param execution The execution block.
 */
inline fun <reified C : CommandSender> AbstractStellarCommand<*>.asyncExecution(
    scope: CoroutineScope = StellarConfig.asyncScope,
    noinline execution: suspend CommandContext<C>.() -> Unit,
): AbstractStellarCommand<*> = addAsyncExecution<C> {
    scope.launch {
        execution()
    }
}

/**
 * Adds a runnable to the command.
 *
 * @param C The type of CommandSender.
 * @param alwaysApplicable Whether it should always run or only when an execution is already present for the last argument.
 * @param scope The [CoroutineScope] used to create
 * @param runnable The execution block.
 * @return The modified command object.
 */
inline fun <reified C : CommandSender> AbstractStellarCommand<*>.runnable(
    alwaysApplicable: Boolean = false,
    scope: CoroutineScope = StellarConfig.scope,
    noinline runnable: suspend CommandContext<C>.() -> Boolean,
): AbstractStellarCommand<*> = addRunnable<C>(alwaysApplicable = alwaysApplicable) {
    scope.future {
        runnable()
    }.get()
}

/**
 * Adds an async runnable to the command using [StellarConfig.asyncScope].
 *
 * @param C The type of CommandSender.
 * @param alwaysApplicable Whether it should always run or only when an execution is already present for the last argument.
 * @param execution The execution block.
 */
inline fun <reified C : CommandSender> AbstractStellarCommand<*>.asyncRunnable(
    alwaysApplicable: Boolean = false,
    scope: CoroutineScope = StellarConfig.asyncScope,
    noinline execution: suspend CommandContext<C>.() -> Unit,
): AbstractStellarCommand<*> = addAsyncRunnable<C>(alwaysApplicable) {
    scope.launch {
        execution()
    }
    true
}

/**
 * Adds a failure execution to the command to be displayed when the command fails.
 *
 * @param C The type of CommandSender.
 * @param scope The [CoroutineScope] used to run the [execution] block (default: [StellarConfig.scope]).
 * @param execution The execution block.
 * @return The modified command object.
 */
inline fun <reified C : CommandSender> AbstractStellarCommand<*>.failureExecution(
    scope: CoroutineScope = StellarConfig.scope,
    noinline execution: suspend CommandContext<C>.() -> Unit,
): AbstractStellarCommand<*> = addFailureExecution<C> {
    scope.launch {
        execution()
    }
}

/**
 * Adds a requirement that must be met for the command to be available to the player.
 *
 * NOTE: the [requirement] block blocks the current thread.
 *
 * @param C The type of CommandSender.
 * @param scope The [CoroutineScope] used to run the [requirement] block (default: [StellarConfig.scope]).
 * @param requirement The condition that must be met.
 * @return The modified command object.
 */
inline fun <reified C : CommandSender> AbstractStellarCommand<*>.requires(
    scope: CoroutineScope = StellarConfig.scope,
    noinline requirement: suspend C.() -> Boolean,
): AbstractStellarCommand<*> = addRequirement<C> {
    scope.future {
        requirement()
    }.get()
}

/**
 * Adds a level requirement for the command to be available to the player.
 *
 * **Note: this only applies to players, not all command senders.**
 *
 * @param level The required permission level.
 */
fun AbstractStellarCommand<*>.requires(level: Int) = addRequirement(1)

/**
 * Adds a Bukkit permission requirement for the command to be available to the player.
 *
 * @param permission The permission string.
 */
fun AbstractStellarCommand<*>.requires(permission: String) = addRequirement<CommandSender> { hasPermission(permission) }

/**
 * Adds any number of [Suggestion] on top of the current suggestions.
 *
 * @return The modified [ParameterArgument].
 */
fun ParameterArgument<*, *>.suggests(vararg suggestions: Suggestion) = addSuggestions(suggestions.toString())

/**
 * Adds a [Suggestion] with the given title and tooltip on top of the current suggestions.
 *
 * @return The modified [ParameterArgument].
 */
fun ParameterArgument<*, *>.suggests(title: String, tooltip: String? = null) = addSuggestion(title, tooltip)

/**
 * Adds a function that returns a list of [Suggestion] on top of the current suggestions.
 *
 * @param C The type of CommandSender.
 * @param scope The [CoroutineScope] used to run the [suggestion] block (default: [StellarConfig.scope]).
 * @return The modified [ParameterArgument].
 */
inline fun <reified C : CommandSender> ParameterArgument<*, *>.suggests(
    scope: CoroutineScope = StellarConfig.scope,
    noinline suggestion: suspend CommandContext<C>.(input: String) -> List<Suggestion>,
) = addFutureSuggestion<C> { input ->
    scope.future {
        suggestion(input)
    }
}

/**
 * Adds a function that returns a list of [Suggestion] on top of the current suggestions.
 *
 * @param C The type of CommandSender.
 * @param scope The [CoroutineScope] used to run the [suggestion] block (default: [StellarConfig.asyncScope]).
 * @return The modified [ParameterArgument].
 */
inline fun <reified C : CommandSender> ParameterArgument<*, *>.asyncSuggests(
    scope: CoroutineScope = StellarConfig.asyncScope,
    noinline suggestion: suspend CommandContext<C>.(input: String) -> List<Suggestion>,
) = addFutureSuggestion<C> { input ->
    scope.future {
        suggestion(input)
    }
}