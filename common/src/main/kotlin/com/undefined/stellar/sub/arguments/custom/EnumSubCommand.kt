package com.undefined.stellar.sub.arguments.custom

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.sub.AbstractStellarSubCommand
import java.lang.Enum.valueOf
import kotlin.reflect.KClass

class EnumSubCommand<T : Enum<T>>(
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
) : AbstractStellarSubCommand<EnumSubCommand<T>>(parent, name) {
    fun getStringList(): List<String> = enum.java.enumConstants.map(stringifier)
    fun valueOf(name: String): Enum<T>? =
        try {
            valueOf(enum.java, name) as Enum<T>
        } catch (e: IllegalArgumentException) {
            null
        }

}
