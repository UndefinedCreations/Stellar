package com.undefined.stellar.sub.custom

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender
import java.lang.Enum.valueOf
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
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
) : BrigadierTypeSubCommand<EnumSubCommand<T>>(parent, name) {
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

    inline fun <reified C : CommandSender> alwaysRunEnum(noinline execution: C.(T) -> Boolean): EnumSubCommand<T> {
        customRunnables.add(CustomStellarRunnable(C::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}
