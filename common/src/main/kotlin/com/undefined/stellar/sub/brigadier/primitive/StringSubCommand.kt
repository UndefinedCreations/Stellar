package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class StringSubCommand(parent: BaseStellarCommand<*>, name: String, val type: StringType) : BrigadierTypeSubCommand<StringSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addStringExecution(noinline execution: T.(String) -> Unit): StringSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunString(noinline execution: T.(String) -> Boolean): StringSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}

enum class StringType(vararg examples: String) {
    SINGLE_WORD(*arrayOf("word", "words_with_underscores")),
    QUOTABLE_PHRASE(*arrayOf("\"quoted phrase\"", "word", "\"\"")),
    GREEDY_PHRASE(*arrayOf("word", "words with spaces", "\"and symbols\""));

    val examples: Collection<String> = listOf(*examples)
}