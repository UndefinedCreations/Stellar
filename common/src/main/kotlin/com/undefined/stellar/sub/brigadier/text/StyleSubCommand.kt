package com.undefined.stellar.sub.brigadier.text

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.BaseStellarSubCommand
import net.kyori.adventure.text.format.Style
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class StyleSubCommand(parent: AbstractStellarCommand<*>, name: String) : BaseStellarSubCommand<StyleSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addStyleExecution(noinline execution: T.(Style) -> Unit): StyleSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any?>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunStyle(noinline execution: T.(Style) -> Boolean): StyleSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any?>)
        return this
    }
}