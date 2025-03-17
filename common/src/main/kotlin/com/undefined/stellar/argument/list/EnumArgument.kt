package com.undefined.stellar.argument.list

import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.argument.basic.StringType
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import java.lang.Enum.valueOf
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class EnumArgument<T : Enum<T>>(
    name: String,
    val enum: KClass<out Enum<T>>,
    converter: CommandSender.(Enum<T>) -> Suggestion? = { Suggestion.withText(it.name) },
    async: Boolean = true,
) : ListArgument<Enum<T>, String>(StringArgument(name, StringType.WORD), enum.java.enumConstants.toList(), converter, { input ->
    try {
        valueOf(enum.java as Class<out Enum<*>>, input.uppercase()) as Enum<T>
    } catch (e: IllegalArgumentException) {
        null
    }
}, async)