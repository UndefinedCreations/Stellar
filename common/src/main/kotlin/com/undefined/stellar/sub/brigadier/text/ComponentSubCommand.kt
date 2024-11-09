package com.undefined.stellar.sub.brigadier.text

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class ComponentSubCommand(parent: BaseStellarCommand<*>, name: String) : BrigadierTypeSubCommand<ComponentSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addComponentExecution(noinline execution: T.(Component) -> Unit): ComponentSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunComponent(noinline execution: T.(Component) -> Boolean): ComponentSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}