package com.undefined.stellar.argument.types.custom

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import java.lang.Enum.valueOf
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class EnumArgument<T : Enum<T>>(
    parent: AbstractStellarCommand<*>,
    name: String,
    val enum: KClass<out Enum<*>>,
    val stringifier: (Enum<*>) -> String = { it.name },
    val parse: (String) -> Enum<T>? = {
        try {
            valueOf(enum.java, name) as Enum<T>
        } catch (e: IllegalArgumentException) {
            null
        }
    }
) : AbstractStellarArgument<EnumArgument<T>>(parent, name) {
    fun getStringList(): List<String> = enum.java.enumConstants.map(stringifier)
    fun valueOf(name: String): Enum<T>? =
        try {
            valueOf(enum.java, name) as Enum<T>
        } catch (e: IllegalArgumentException) {
            null
        }

}
