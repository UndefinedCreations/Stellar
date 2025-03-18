package com.undefined.stellar.argument.list

import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.argument.basic.StringType
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import java.lang.Enum.valueOf
import kotlin.reflect.KClass

/**
 * An extension of [ListArgument], using a [StringArgument] as a base, that creates a list of suggestions based on an enum.
 *
 * @param name The name of the argument.
 * @param enum A [KClass] of the [Enum] used.
 * @param converter A function providing a [CommandSender] and an [Enum] instance from the [enum], returning the [Suggestion] sent to the player.
 * If the [Suggestion] is null, then it will be filtered out (default: uses the `name` property.
 * This is useful when you wish to get the argument input and process the information yourself.
 * @param parse A function providing a [CommandSender] and the argument input, returning the parsed [Enum] (default: `enum.valueOf(input.uppercase())`).
 * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
 */
@Suppress("UNCHECKED_CAST")
class EnumArgument<T : Enum<T>>(
	name: String,
	val enum: KClass<out Enum<T>>,
	converter: CommandSender.(Enum<T>) -> Suggestion? = { Suggestion.withText(it.name) },
	parse: CommandSender.(String) -> Enum<T>? = { input ->
		try {
			valueOf(enum.java as Class<out Enum<*>>, input.uppercase()) as Enum<T>
		} catch (e: IllegalArgumentException) {
			null
		}
	},
	async: Boolean = true,
) : ListArgument<Enum<T>, String>(
	StringArgument(name, StringType.WORD),
	enum.java.enumConstants.toList(),
	converter,
	parse,
	async
)