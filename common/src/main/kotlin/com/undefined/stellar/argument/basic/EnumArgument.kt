package com.undefined.stellar.argument.basic

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import java.lang.Enum.valueOf
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class EnumArgument<T : Enum<T>>(
    parent: AbstractStellarCommand<*>,
    name: String,
    val enum: KClass<out Enum<*>>,
    converter: CommandContext<CommandSender>.(Enum<*>) -> Suggestion? = { Suggestion.withText(it.name) },
    parse: CommandContext<CommandSender>.(String) -> Enum<T>? = {
        try {
            valueOf(enum.java, it.uppercase()) as Enum<T>
        } catch (e: IllegalArgumentException) {
            null
        }
    },
    async: Boolean = true
) : ListArgument<Enum<*>, String>(StringArgument(parent, name, StringType.WORD), enum.java.enumConstants.toList(), converter, parse, async) {
    fun valueOf(name: String): Enum<T>? =
        try {
            valueOf(enum.java, name) as Enum<T>
        } catch (e: IllegalArgumentException) {
            null
        }
}

enum class EnumFormatting(val action: (String) -> String) {
    LOWERCASE({ it.lowercase() }),
    UPPERCASE({ it.uppercase() }),
    CAPITALIZED({ it.lowercase().replaceFirstChar { char -> char.uppercase() } }),
    NONE({ it }),
}
