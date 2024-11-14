package com.undefined.stellar.sub.brigadier.text

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class MessageSubCommand(parent: AbstractStellarCommand<*>, name: String) : BrigadierTypeSubCommand<MessageSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addMessageExecution(noinline execution: T.(Component) -> Unit): MessageSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunMessage(noinline execution: T.(Component) -> Boolean): MessageSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}