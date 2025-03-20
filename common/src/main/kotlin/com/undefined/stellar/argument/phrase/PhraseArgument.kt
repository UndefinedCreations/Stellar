package com.undefined.stellar.argument.phrase

import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.argument.basic.StringType
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.phrase.PhraseCommandContext
import com.undefined.stellar.data.phrase.PhraseExecution
import com.undefined.stellar.data.phrase.PhraseFunction
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus

/**
 * An extension of [StringArgument] with [StringType.PHRASE] which allows you to input a phrase and handle each word independently.
 * This allows you to add word suggestions, word executions and word runnables.
 */
class PhraseArgument(name: String) : StringArgument(name, StringType.PHRASE) {

    @ApiStatus.Internal
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

            setSuggestionOffset(greedyContext.phraseInput.length)
            val suggestions: MutableList<Suggestion> = mutableListOf()
            val word = words[amountOfSpaces] ?: return@addSuggestion emptyList()
            for (stellarSuggestion in word.suggestions)
                for (suggestion in stellarSuggestion.get(greedyContext)) suggestions.add(Suggestion.create(suggestion.text, suggestion.tooltip))
            suggestions
        }
    }

    /**
     * Creates a [WordArgument] at the [index], that you can configure with [init]. Only works in Kotlin.
     * @return The modified [PhraseArgument] instance.
     */
	inline fun addWordArgument(index: Int, init: WordArgument.() -> Unit = {}): PhraseArgument {
        val word = WordArgument()
        word.init()
        words[index] = word
        return this
    }

    /**
     * Creates a [WordArgument] at the [index], that you can configure with [init].
     * @return The modified [PhraseArgument] instance.
     */
    @JvmOverloads
    fun addWordArgument(index: Int, init: PhraseFunction = PhraseFunction {}): PhraseArgument {
        val word = WordArgument()
        init(word)
        words[index] = word
        return this
    }

    /**
     * Gets the [WordArgument] with the specified [index] if it exists, otherwise creates one.
     * You can then configure the [WordArgument] with [function].
     *
     * This only works in Kotlin.
     *
     * @return The modified [PhraseArgument] instance.
     */
    inline fun getOrCreateWordArgument(index: Int, function: WordArgument.() -> Unit = {}): PhraseArgument {
        words[index]?.apply { function() } ?: run {
            words[index] = WordArgument().apply { function() }
        }
        return this
    }

    /**
     * Gets the [WordArgument] with the specified [index] if it exists, otherwise creates one.
     * You can then configure the [WordArgument] with [function].
     *
     * @return The modified [PhraseArgument] instance.
     */
    fun getOrCreateWordArgument(index: Int, function: PhraseFunction = PhraseFunction {}): PhraseArgument {
        words[index]?.apply { function(this) } ?: run {
            words[index] = WordArgument().apply { function(this) }
        }
        return this
    }

    /**
     * Sets the execution for the [WordArgument] at [index]. Only works in Kotlin.
     *
     * @return The modified [PhraseArgument] instance.
     */
    inline fun <reified C : CommandSender> setWordExecution(index: Int, noinline execution: PhraseCommandContext<C>.() -> Unit): PhraseArgument =
        getOrCreateWordArgument(index) {
            setExecution(execution)
        }

    /**
     * Sets the execution for the [WordArgument] at [index].
     *
     * @return The modified [PhraseArgument] instance.
     */
    fun setWordExecution(index: Int, execution: PhraseExecution<CommandSender>): PhraseArgument =
        getOrCreateWordArgument(index) {
            setExecution(execution)
        }

    /**
     * Adds an execution to the [WordArgument] at [index]. Only works in Kotlin.
     *
     * @return The modified [PhraseArgument] instance.
     */
    inline fun <reified C : CommandSender> addWordExecution(index: Int, noinline execution: PhraseCommandContext<C>.() -> Unit): PhraseArgument =
        getOrCreateWordArgument(index) { addExecution(execution) }

    /**
     * Adds an execution to the [WordArgument] at [index].
     *
     * @return The modified [PhraseArgument] instance.
     */
    fun addWordExecution(index: Int, execution: PhraseExecution<CommandSender>): PhraseArgument =
        getOrCreateWordArgument(index) { addExecution(execution) }

    /**
     * Adds an async execution to the [WordArgument] at [index]. Only works in Kotlin.
     *
     * @return The modified [PhraseArgument] instance.
     */
    inline fun <reified C : CommandSender> addAsyncWordExecution(index: Int, noinline execution: PhraseCommandContext<C>.() -> Unit): PhraseArgument =
        getOrCreateWordArgument(index) { addAsyncExecution(execution) }

    /**
     * Adds an async execution to the [WordArgument] at [index].
     *
     * @return The modified [PhraseArgument] instance.
     */
    fun addAsyncWordExecution(index: Int, execution: PhraseExecution<CommandSender>): PhraseArgument =
        getOrCreateWordArgument(index) { addAsyncExecution(execution) }

    /**
     * Sets the runnable for the [WordArgument] at [index]. Only works in Kotlin.
     *
     * @return The modified [PhraseArgument] instance.
     */
    inline fun <reified C : CommandSender> setWordRunnable(index: Int, noinline runnable: PhraseCommandContext<C>.() -> Boolean): PhraseArgument =
        getOrCreateWordArgument(index) {
            setRunnable(runnable)
        }

    /**
     * Sets the runnable for the [WordArgument] at [index].
     *
     * @return The modified [PhraseArgument] instance.
     */
    fun setWordRunnable(index: Int, runnable: PhraseCommandContext<CommandSender>.() -> Boolean): PhraseArgument =
        getOrCreateWordArgument(index) {
            setRunnable(runnable)
        }

    /**
     * Adds a runnable to the [WordArgument] at [index]. Only works in Kotlin.
     *
     * @return The modified [PhraseArgument] instance.
     */
    inline fun <reified C : CommandSender> addWordRunnable(index: Int, noinline runnable: PhraseCommandContext<C>.() -> Boolean): PhraseArgument =
        getOrCreateWordArgument(index) { addRunnable(runnable) }

    /**
     * Adds a runnable to the [WordArgument] at [index].
     *
     * @return The modified [PhraseArgument] instance.
     */
    fun addWordRunnable(index: Int, runnable: PhraseCommandContext<CommandSender>.() -> Boolean): PhraseArgument =
        getOrCreateWordArgument(index) { addRunnable(runnable) }

    /**
     * Sets the word suggestions for the [WordArgument] at [index].
     *
     * @return The modified [PhraseArgument] instance.
     */
    fun setWordSuggestions(index: Int, vararg suggestions: Suggestion): PhraseArgument =
        getOrCreateWordArgument(index) { setSuggestions(*suggestions) }

    /**
     * Adds word suggestions to the [WordArgument] at [index].
     *
     * @return The modified [PhraseArgument] instance.
     */
    fun addWordSuggestions(index: Int, vararg suggestions: Suggestion): PhraseArgument =
        getOrCreateWordArgument(index) { addSuggestions(*suggestions) }

    /**
     * Adds word suggestions to the [WordArgument] at [index].
     *
     * @return The modified [PhraseArgument] instance.
     */
    fun addWordSuggestions(index: Int, vararg suggestions: String): PhraseArgument =
        getOrCreateWordArgument(index) { addSuggestions(*suggestions) }

    /**
     * Adds a list of word suggestions to the [WordArgument] at [index].
     *
     * @return The modified [PhraseArgument] instance.
     */
    fun addWordSuggestions(index: Int, suggestions: List<Suggestion>): PhraseArgument =
        getOrCreateWordArgument(index) { addSuggestions(suggestions) }

    /**
     * Adds a list of word suggestions to the [WordArgument] at [index] solely through a list of [String] that represent the titles.
     *
     * @return The modified [PhraseArgument] instance.
     */
    fun addWordSuggestionsWithoutTooltip(index: Int, suggestions: List<String>): PhraseArgument =
        getOrCreateWordArgument(index) { addSuggestionsWithoutWordArgumentTooltip(suggestions) }

    private fun getPhraseCommandContext(context: CommandContext<CommandSender>): PhraseCommandContext<CommandSender> {
        val input = context.input.removePrefix("/")
        val words = input.split(' ').toMutableList()

        val totalOtherArguments = context.arguments.size - 1
        for (i in (0..totalOtherArguments)) words.removeFirst()

        return PhraseCommandContext(context, words, context.sender, input.substring(input.indexOf(' ')), input)
    }

}
