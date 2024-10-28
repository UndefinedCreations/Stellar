package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.command.CommandSender
import java.lang.Enum.valueOf
import kotlin.reflect.KClass

class EnumSubCommand<T : Enum<T>>(
    parent: BaseStellarCommand<*>,
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
) : NativeTypeSubCommand<EnumSubCommand<T>>(parent, name) {
    fun getStringList(): List<String> = enum.java.enumConstants.map(stringifier)
    fun valueOf(name: String): Enum<T>? =
        try {
            valueOf(enum.java, name) as Enum<T>
        } catch (e: IllegalArgumentException) {
            null
        }

    inline fun <reified C : CommandSender> addEnumExecution(noinline execution: C.(T) -> Unit): EnumSubCommand<T> {
        customExecutions.add(CustomStellarExecution(C::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}
