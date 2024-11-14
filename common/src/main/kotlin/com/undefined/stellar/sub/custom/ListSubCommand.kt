package com.undefined.stellar.sub.custom

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
open class ListSubCommand<T>(
    parent: AbstractStellarCommand<*>,
    name: String,
    val list: () -> List<T>,
    val stringifier: (T) -> String = { it.toString() },
    val parse: (String) -> T?
) : BrigadierTypeSubCommand<ListSubCommand<T>>(parent, name) {

    constructor(parent: AbstractStellarCommand<*>,
                name: String,
                list: List<T>,
                stringifier: (T) -> String = { it.toString() },
                parse: (String) -> T?) : this(parent, name, { list }, stringifier, parse)

    fun getStringList(): List<String> = list().map(stringifier)

    inline fun <reified C : CommandSender> addListExecution(noinline execution: C.(T?) -> Unit): ListSubCommand<T> {
        customExecutions.add(CustomStellarExecution(C::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified C : CommandSender> alwaysRunList(noinline execution: C.(T?) -> Boolean): ListSubCommand<T> {
        customRunnables.add(CustomStellarRunnable(C::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}
