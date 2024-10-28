package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.command.CommandSender

class ListSubCommand<T>(
    parent: BaseStellarCommand<*>,
    name: String,
    val list: List<T>,
    val stringifier: (T) -> String = { it.toString() },
    val parse: (String) -> T
) : NativeTypeSubCommand<ListSubCommand<T>>(parent, name) {
    fun getStringList(): List<String> = list.map(stringifier)

    inline fun <reified C : CommandSender> addListExecution(noinline execution: C.(T) -> Unit): ListSubCommand<T> {
        customExecutions.add(CustomStellarExecution(C::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}
