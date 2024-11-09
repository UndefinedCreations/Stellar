package com.undefined.stellar.sub.brigadier.text

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import net.kyori.adventure.text.format.Style
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class ColorSubCommand(parent: BaseStellarCommand<*>, name: String) : BrigadierTypeSubCommand<ColorSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addColorExecution(noinline execution: T.(Style) -> Unit): ColorSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunColor(noinline execution: T.(Style) -> Boolean): ColorSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}