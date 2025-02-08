package com.undefined.stellar.argument.basic

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.data.argument.PhraseCommandContext
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender

class PhraseArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<PhraseArgument, String>(parent, name) {

    val words: HashMap<Int, WordArgument> = hashMapOf()

    fun onWord(index: Int, init: WordArgument.() -> Unit): PhraseArgument {
        val word = WordArgument()
        word.init()
        words[index] = word
        return this
    }

    inline fun <reified C : CommandSender> setWordExecution(index: Int, noinline execution: PhraseCommandContext<C>.() -> Unit): PhraseArgument =
        getOrCreateWord(index) {
            setExecution(execution)
        }

    inline fun <reified C : CommandSender> addWordExecution(index: Int, noinline execution: PhraseCommandContext<C>.() -> Unit): PhraseArgument =
        getOrCreateWord(index) { addExecution(execution) }

    inline fun <reified C : CommandSender> setWordRunnable(index: Int, noinline runnable: PhraseCommandContext<C>.() -> Boolean): PhraseArgument =
        getOrCreateWord(index) {
            setRunnable(runnable)
        }

    inline fun <reified C : CommandSender> addWordRunnable(index: Int, noinline runnable: PhraseCommandContext<C>.() -> Boolean): PhraseArgument =
        getOrCreateWord(index) { addRunnable(runnable) }

    fun setWordSuggestions(index: Int, vararg suggestions: Suggestion): PhraseArgument =
        getOrCreateWord(index) { setSuggestions(*suggestions) }

    fun addWordSuggestions(index: Int, vararg suggestions: Suggestion): PhraseArgument =
        getOrCreateWord(index) { addSuggestions(*suggestions) }

    fun addWordSuggestions(index: Int, vararg suggestions: String): PhraseArgument =
        getOrCreateWord(index) { addSuggestions(*suggestions) }

    fun addWordSuggestions(index: Int, suggestions: List<Suggestion>): PhraseArgument =
        getOrCreateWord(index) { addSuggestions(suggestions) }

    fun addWordSuggestionsWithoutTooltip(index: Int, suggestions: List<String>): PhraseArgument =
        getOrCreateWord(index) { addSuggestionsWithoutWordArgumentTooltip(suggestions) }

    inline fun getOrCreateWord(index: Int, function: WordArgument.() -> Unit): PhraseArgument {
        words[index]?.apply { function() } ?: run {
            words[index] = WordArgument().apply { function() }
        }
        return this
    }

}
