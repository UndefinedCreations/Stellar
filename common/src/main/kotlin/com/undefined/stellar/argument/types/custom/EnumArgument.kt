package com.undefined.stellar.argument.types.custom

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.data.suggestion.Suggestion
import java.lang.Enum.valueOf
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class EnumArgument<T : Enum<T>>(
    parent: AbstractStellarCommand<*>,
    type: AbstractStellarArgument<*>,
    val enum: KClass<out Enum<*>>,
    converter: (Enum<*>?) -> Suggestion = { Suggestion.withText(it?.name ?: "") },
    parse: (Any?) -> Enum<T>? = {
        try {
            valueOf(enum.java, type.name) as Enum<T>
        } catch (e: IllegalArgumentException) {
            null
        }
    }
) : ListArgument<Enum<*>?>(parent, type, enum.java.enumConstants.toList(), converter, parse) {

    fun valueOf(name: String): Enum<T>? =
        try {
            valueOf(enum.java, name) as Enum<T>
        } catch (e: IllegalArgumentException) {
            null
        }

}
