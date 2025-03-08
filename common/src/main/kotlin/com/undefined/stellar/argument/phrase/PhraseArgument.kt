package com.undefined.stellar.argument.phrase

import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.argument.basic.StringType
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.phrase.PhraseCommandContext
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender

class PhraseArgument(name: String) : StringArgument(name, StringType.PHRASE) {

    val words: HashMap<Int, WordArgument> = hashMapOf()

    init {
        addExecution<CommandSender> {
            val greedyContext = getPhraseCommandContext(this)

            for (i in greedyContext.arguments.indices) {
                val word = words[i] ?: continue

                for (runnable in word.runnables) runnable(greedyContext)
                if (i == greedyContext.arguments.lastIndex)
                    for (execution in word.executions) execution(greedyContext)
            }
        }
        addSuggestion<CommandSender> {
            val greedyContext = getPhraseCommandContext(this)
            var prevChar = ' '
            val amountOfSpaces: Int = if (greedyContext.phraseInput.isEmpty()) 0 else greedyContext.phraseInput.count {
                if (prevChar == ' ' && it == ' ') return@count false
                prevChar = it
                it == ' '
            }
            sender.sendMessage("input: ${greedyContext.phraseInput}")
            sender.sendMessage("amount of spaces: $amountOfSpaces")
            sender.sendMessage("offset: ${greedyContext.phraseInput.length}")

            setSuggestionOffset(greedyContext.phraseInput.length)
            val suggestions: MutableList<Suggestion> = mutableListOf()
            val word = words[amountOfSpaces] ?: return@addSuggestion emptyList()
            for (stellarSuggestion in word.suggestions)
                for (suggestion in stellarSuggestion.get(greedyContext)) suggestions.add(Suggestion.create(suggestion.text, suggestion.tooltip))
            suggestions
        }
    }

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

    inline fun <reified C : CommandSender> addAsyncWordExecution(index: Int, noinline execution: PhraseCommandContext<C>.() -> Unit): PhraseArgument =
        getOrCreateWord(index) { addAsyncExecution(execution) }

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

    private fun getPhraseCommandContext(context: CommandContext<CommandSender>): PhraseCommandContext<CommandSender> {
        val input = context.input.removePrefix("/")
        val words = input.split(' ').toMutableList()

        val totalOtherArguments = context.arguments.size - 1
        for (i in (0..totalOtherArguments)) words.removeFirst()

        return PhraseCommandContext(context, words, context.sender, input.substring(input.indexOf(' ')), input)
    }

}
