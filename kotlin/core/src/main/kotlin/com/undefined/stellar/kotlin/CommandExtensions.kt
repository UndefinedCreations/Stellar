package com.undefined.stellar.kotlin

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.ParameterArgument
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.Suggestion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext

/**
 * Adds an execution to the command.
 *
 * @param C The type of CommandSender.
 * @param context The [CoroutineContext] the execution will be run in (default: [Dispatchers.Default]).
 * @param execution The execution block.
 * @return The modified command object.
 */
inline fun <reified C : CommandSender> AbstractStellarCommand<*>.execution(
    context: CoroutineContext = Dispatchers.Default,
    noinline execution: suspend CommandContext<C>.(CoroutineScope) -> Unit,
): AbstractStellarCommand<*> = addExecution<C> {
    CoroutineScope(context).future {
        execution(this)
    }.get()
}

/**
 * Adds an async execution to the command with the use of [CompletableFuture].
 *
 * @param C The type of CommandSender.
 * @param execution The execution block.
 */
inline fun <reified C : CommandSender> AbstractStellarCommand<*>.asyncExecution(noinline execution: CommandContext<C>.() -> Unit) = addAsyncExecution(execution)

/**
 * Adds a runnable to the command.
 *
 * @param C The type of CommandSender.
 * @param alwaysApplicable Whether it should always run or only when an execution is already present for the last argument.
 * @param context The [CoroutineContext] the runnable will be run in (default: [Dispatchers.Default]).
 * @param runnable The execution block.
 * @return The modified command object.
 */
inline fun <reified C : CommandSender> AbstractStellarCommand<*>.runnable(
    alwaysApplicable: Boolean = false,
    context: CoroutineContext = Dispatchers.Default,
    noinline runnable: suspend CommandContext<C>.(CoroutineScope) -> Boolean,
): AbstractStellarCommand<*> = addRunnable<C>(alwaysApplicable = alwaysApplicable) {
    CoroutineScope(context).future {
        runnable(this)
    }.get()
}

/**
 * Adds an async runnable to the command with the use of [CompletableFuture].
 *
 * @param C The type of CommandSender.
 * @param alwaysApplicable Whether it should always run or only when an execution is already present for the last argument.
 * @param execution The execution block.
 */
inline fun <reified C : CommandSender> AbstractStellarCommand<*>.asyncRunnable(
    alwaysApplicable: Boolean = false,
    noinline execution: CommandContext<C>.() -> Boolean,
) {
    addAsyncRunnable(alwaysApplicable, execution)
}

/**
 * Adds a failure execution to the command to be displayed when the command fails.
 *
 * @param C The type of CommandSender.
 * @param context The [CoroutineContext] the execution will be run in (default: [Dispatchers.Default]).
 * @param execution The execution block.
 * @return The modified command object.
 */
inline fun <reified C : CommandSender> AbstractStellarCommand<*>.failureExecution(
    context: CoroutineContext = Dispatchers.Default,
    noinline execution: suspend CommandContext<C>.(CoroutineScope) -> Unit,
): AbstractStellarCommand<*> = addFailureExecution<C> {
    CoroutineScope(context).future {
        execution(this)
    }.get()
}

/**
 * Adds a requirement that must be met for the command to be available to the player.
 *
 * @param C The type of CommandSender.
 * @param context The [CoroutineContext] the requirement will be run in (default: [Dispatchers.Default]).
 * @param requirement The condition that must be met.
 * @return The modified command object.
 */
inline fun <reified C : CommandSender> AbstractStellarCommand<*>.requires(
    context: CoroutineContext = Dispatchers.Default,
    noinline requirement: suspend C.(CoroutineScope) -> Boolean,
): AbstractStellarCommand<*> = addRequirement<C> {
    CoroutineScope(context).future {
        requirement(this)
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
 * Adds a function that returns a list of [Suggestion] on top of the current suggestions. Only works in Kotlin.
 *
 * @param C The type of CommandSender.
 * @param context The [CoroutineContext] the requirement will be run in (default: [Dispatchers.Default]).
 * @return The modified [ParameterArgument].
 */
inline fun <reified C : CommandSender> ParameterArgument<*, *>.suggests(
    context: CoroutineContext = Dispatchers.Default,
    noinline suggestion: suspend CommandContext<C>.(input: String) -> List<Suggestion>,
) = addFutureSuggestion<C> { input ->
    CoroutineScope(context).future {
        suggestion(input)
    }
}