package com.undefined.stellar.argument.list

import com.undefined.stellar.AbstractStellarArgument
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.exception.UnsupportedArgumentTypeException
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.ApiStatus.OverrideOnly
import java.util.concurrent.CompletableFuture

/**
 * An argument that wraps around an [AbstractStellarArgument] and adds a suggestion based on the list provided.
 *
 * @param base The [AbstractStellarArgument] it wraps around.
 * @param list A function providing a [CommandContext] and returning the list used in the argument.
 * @param converter A function providing a [CommandSender] and a value from the list, returning the [Suggestion] shown in-game. This is used to convert each element in the list ot a suggestion viewable in-game (default: uses `toString()`).
 * @param parse A function providing a [CommandSender] and the value returned from the [AbstractStellarArgument], returning the argument parsed from the [base] argument.
 * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
 */
open class ListArgument<T, R> @JvmOverloads constructor(
	val base: AbstractStellarArgument<*, R>,
	val list: CommandContext<CommandSender>.() -> Iterable<T>,
	val converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
	val parse: CommandSender.(R) -> T?,
	val async: Boolean = false,
) : AbstractStellarArgument<ListArgument<T, R>, R>(base.name, base.argumentType) {

	/**
	 * An argument that wraps around an [AbstractStellarArgument] and adds a suggestion based on the list provided.
	 *
	 * @param base The [AbstractStellarArgument] it wraps around.
	 * @param list The list used in the argument.
	 * @param converter A function providing a [CommandSender] and a value from the list, returning the [Suggestion] shown in-game. This is used to convert each element in the list ot a suggestion viewable in-game (default: uses `toString()`).
	 * @param parse A function providing a [CommandSender] and the value returned from the [AbstractStellarArgument], returning the argument parsed from the [base] argument.
	 * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
	 */
	@JvmOverloads
	constructor(
		base: AbstractStellarArgument<*, R>,
		list: Iterable<T>,
		converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
		parse: CommandSender.(R) -> T?,
		async: Boolean = true,
	) : this(base, { list }, converter, parse, async)

	init {
		if (base is ListArgument<*, *>) throw UnsupportedArgumentTypeException(base)
		addFutureSuggestion { context, input ->
			if (async) CompletableFuture.supplyAsync { getSuggestionList(context, input) }
			else CompletableFuture.completedFuture(getSuggestionList(context, input))
		}
	}

	@Suppress("UNCHECKED_CAST")
	@ApiStatus.Internal
	fun parseInternal(sender: CommandSender, value: Any): T? = parse(sender, value as R)

	/**
	 * An open function that returns the list of suggestions used in the argument.
	 *
	 * Note: This method should only be overridden! This is not meant to be called from the end user.
	 */
	@OverrideOnly
	open fun getSuggestionList(context: CommandContext<CommandSender>, input: String): List<Suggestion> =
		list(context).mapNotNull { converter(context.sender, it) }.filter { it.text.startsWith(input, true) }

}