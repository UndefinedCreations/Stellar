package com.undefined.stellar.argument.list

import com.undefined.stellar.ParameterArgument
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.exception.UnsupportedArgumentTypeException
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.ApiStatus.OverrideOnly
import java.util.concurrent.CompletableFuture

/**
 * An argument that wraps around an [ParameterArgument] and adds a suggestion based on the list provided.
 *
 * @param base The [ParameterArgument] it wraps around.
 * @param list A function providing a [CommandContext] and returning the list used in the argument in a [CompletableFuture].
 * @param converter A function providing a [CommandSender] and a value from the list, returning the [Suggestion] shown in-game. This is used to convert each element in the list ot a suggestion viewable in-game (default: uses `toString()`).
 * @param parse A function providing a [CommandSender] and the value returned from the [ParameterArgument], returning the argument parsed from the [base] argument.
 */
open class ListArgument<T, R> @JvmOverloads constructor(
	val base: ParameterArgument<*, R>,
	val list: CommandContext<CommandSender>.() -> CompletableFuture<Iterable<T>>,
	val converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
	val parse: CommandSender.(R) -> T?,
) : ParameterArgument<ListArgument<T, R>, R>(base.name, base.argumentType) {

	/**
	 * An argument that wraps around an [ParameterArgument] and adds a suggestion based on the list provided.
	 *
	 * @param base The [ParameterArgument] it wraps around.
	 * @param list The list used in the argument.
	 * @param converter A function providing a [CommandSender] and a value from the list, returning the [Suggestion] shown in-game. This is used to convert each element in the list ot a suggestion viewable in-game (default: uses `toString()`).
	 * @param parse A function providing a [CommandSender] and the value returned from the [ParameterArgument], returning the argument parsed from the [base] argument.
	 */
	@JvmOverloads
	constructor(
		base: ParameterArgument<*, R>,
		list: Iterable<T>,
		converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
		parse: CommandSender.(R) -> T?,
	) : this(base, { CompletableFuture.completedFuture(list) }, converter, parse)

	/**
	 * An argument that wraps around an [ParameterArgument] and adds a suggestion based on the list provided.
	 *
	 * @param base The [ParameterArgument] it wraps around.
	 * @param list The list used in the argument.
	 * @param converter A function providing a [CommandSender] and a value from the list, returning the [Suggestion] shown in-game. This is used to convert each element in the list ot a suggestion viewable in-game (default: uses `toString()`).
	 * @param parse A function providing a [CommandSender] and the value returned from the [ParameterArgument], returning the argument parsed from the [base] argument.
	 * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
	 */
	@JvmOverloads
	@Deprecated("This constructor is deprecated! To use the newer method, omit the async variable.")
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	constructor(
		base: ParameterArgument<*, R>,
		list: Iterable<T>,
		converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
		parse: CommandSender.(R) -> T?,
		async: Boolean,
	) : this(base, {
		if (async) CompletableFuture.supplyAsync { list }
		else CompletableFuture.completedFuture(list)
	}, converter, parse)

	init {
		if (base is ListArgument<*, *>) throw UnsupportedArgumentTypeException(base)
		addFutureSuggestion<CommandSender> { input ->
			getSuggestionList(this, input)
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
	open fun getSuggestionList(context: CommandContext<CommandSender>, input: String): CompletableFuture<Iterable<Suggestion>> =
		list(context).thenApply { list ->
			println("ListArgument.getSuggestionList")
			println("list: ${list}")
			println("list: ${list.mapNotNull {
				converter(context.sender, it)
			}}")
			list.mapNotNull {
				converter(context.sender, it).takeIf { it?.text?.isNotBlank() == true }
			}.filter { it.text.startsWith(input, true) && it.text != input }
		}

	companion object {
		/**
		 * Creates an instance of [ListArgument].
		 *
		 * @param base The [ParameterArgument] it wraps around.
		 * @param list A function providing a [CommandContext] and returning the list used in the argument.
		 * @param converter A function providing a [CommandSender] and a value from the list, returning the [Suggestion] shown in-game. This is used to convert each element in the list ot a suggestion viewable in-game (default: uses `toString()`).
		 * @param parse A function providing a [CommandSender] and the value returned from the [ParameterArgument], returning the argument parsed from the [base] argument.
		 * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
		 *
		 * @return The created [ListArgument].
		 */
		@JvmOverloads
		fun <T, R> create(
			base: ParameterArgument<*, R>,
			list: CommandContext<CommandSender>.() -> Iterable<T>,
			converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
			parse: CommandSender.(R) -> T?,
			async: Boolean = false,
		) = ListArgument(base, {
			if (async) CompletableFuture.supplyAsync { list(this) }
			else CompletableFuture.completedFuture(list(this))
		}, converter, parse)
	}

}