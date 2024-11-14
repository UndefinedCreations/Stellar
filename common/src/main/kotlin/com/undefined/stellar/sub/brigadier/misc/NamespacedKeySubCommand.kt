package com.undefined.stellar.sub.brigadier.misc

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.NamespacedKey
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class NamespacedKeySubCommand(parent: AbstractStellarCommand<*>, name: String) : BrigadierTypeSubCommand<NamespacedKeySubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addNamespacedKeyExecution(noinline execution: T.(NamespacedKey) -> Unit): NamespacedKeySubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunNamespacedKey(noinline execution: T.(NamespacedKey) -> Boolean): NamespacedKeySubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}