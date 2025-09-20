package com.undefined.stellar.argument.list

import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.argument.basic.StringType
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import java.lang.Enum.valueOf

/**
 * An extension of [ListArgument], using a [StringArgument] as a base, that creates a list of suggestions based on an enum.
 *
 * @param name The name of the argument.
 * @param enum A [Class] of the [Enum] used.
 * @param converter A function providing a [CommandSender] and an [Enum] instance from the [enum], returning the [Suggestion] sent to the player.
 * If the [Suggestion] is null, then it will be filtered out (default: uses the `name` property.
 * This is useful when you wish to get the argument input and process the information yourself.
 * @param parse A function providing a [CommandSender] and the argument input, returning the parsed [Enum] (default: `enum.valueOf(input.uppercase())`).
 *
 * @since 1.13
 */
@Suppress("UNCHECKED_CAST")
class EnumArgument<T : Enum<T>>(
	name: String,
	val enum: Class<out Enum<T>>,
	converter: CommandSender.(Enum<T>) -> Suggestion? = { Suggestion.withText(it.name) },
	parse: CommandSender.(String) -> Enum<T>? = { input ->
		try {
			valueOf(enum as Class<out Enum<*>>, input.uppercase()) as Enum<T>
		} catch (e: IllegalArgumentException) {
			null
		}
	},
) : ListArgument<Enum<T>, String>(
	StringArgument(name, StringType.WORD),
	enum.enumConstants.toList(),
	converter,
	parse,
) {

	/**
	 * An extension of [ListArgument], using a [StringArgument] as a base, that creates a list of suggestions based on an enum.
	 *
	 * @param name The name of the argument.
	 * @param enum A [Class] of the [Enum] used.
	 * @param converter A function providing a [CommandSender] and an [Enum] instance from the [enum], returning the [Suggestion] sent to the player.
	 * If the [Suggestion] is null, then it will be filtered out (default: uses the `name` property.
	 * This is useful when you wish to get the argument input and process the information yourself.
	 * @param parse A function providing a [CommandSender] and the argument input, returning the parsed [Enum] (default: `enum.valueOf(input.uppercase())`).
	 * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
	 *
	 * @since 1.13
	 */
	@JvmOverloads
	@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
	@Deprecated("The async parameter should be omitted")
	constructor(
		name: String,
		enum: Class<out Enum<T>>,
		converter: CommandSender.(Enum<T>) -> Suggestion? = { Suggestion.withText(it.name) },
		parse: CommandSender.(String) -> Enum<T>? = { input ->
			try {
				valueOf(enum as Class<out Enum<*>>, input.uppercase()) as Enum<T>
			} catch (e: IllegalArgumentException) {
				null
			}
		},
		async: Boolean = true,
	) : this(name, enum, converter, parse)

}