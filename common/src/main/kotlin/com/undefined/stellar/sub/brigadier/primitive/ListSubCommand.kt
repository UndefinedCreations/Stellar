package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.command.CommandSender

class ListSubCommand<T>(
    parent: BaseStellarCommand,
    name: String,
    val list: List<T>,
    val serialize: T.() -> String = { this.toString() },
    val deserialize: String.(T) -> T = { this as T }
) : NativeTypeSubCommand(parent, name) {
    inline fun <reified T : CommandSender> addListExecute(noinline execution: T.(Boolean) -> Unit): BaseStellarCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}
