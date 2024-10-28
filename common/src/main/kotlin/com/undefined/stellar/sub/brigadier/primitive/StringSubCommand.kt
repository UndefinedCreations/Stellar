package com.undefined.stellar.sub.brigadier.primitive

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import org.bukkit.command.CommandSender

class StringSubCommand(parent: BaseStellarCommand<*>, name: String, val type: StringType) : NativeTypeSubCommand<StringSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addStringExecution(noinline execution: T.(String) -> Unit): StringSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }
}

enum class StringType(vararg examples: String) {
    SINGLE_WORD(*arrayOf("word", "words_with_underscores")),
    QUOTABLE_PHRASE(*arrayOf("\"quoted phrase\"", "word", "\"\"")),
    GREEDY_PHRASE(*arrayOf("word", "words with spaces", "\"and symbols\""));

    val examples: Collection<String> = listOf(*examples)
}