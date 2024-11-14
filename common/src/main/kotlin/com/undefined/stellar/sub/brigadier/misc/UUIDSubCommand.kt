package com.undefined.stellar.sub.brigadier.misc

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender
import java.util.*

@Suppress("UNCHECKED_CAST")
class UUIDSubCommand(parent: AbstractStellarCommand<*>, name: String) : BrigadierTypeSubCommand<UUIDSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addUUIDExecution(noinline execution: T.(UUID) -> Unit): UUIDSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunUUID(noinline execution: T.(UUID) -> Boolean): UUIDSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}