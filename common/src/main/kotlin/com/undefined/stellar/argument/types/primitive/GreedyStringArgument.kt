package com.undefined.stellar.argument.types.primitive

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.data.argument.GreedyCommandContext
import org.bukkit.command.CommandSender

class GreedyStringArgument(parent: AbstractStellarCommand<*>, name: String) : AbstractStellarArgument<GreedyStringArgument>(parent, name) {

    val words: HashMap<Int, GreedyWordArgument> = hashMapOf()

    fun onWord(index: Int, init: GreedyWordArgument.() -> Unit): GreedyStringArgument {
        val word = GreedyWordArgument()
        word.init()
        words[index] = word
        return this
    }

    inline fun <reified C : CommandSender> setWordExecution(index: Int, noinline execution: GreedyCommandContext<C>.() -> Unit): GreedyStringArgument =
        getOrCreateWord(index) {
            executions.clear()
            addWordExecution<C>(index, execution)
        }

    inline fun <reified C : CommandSender> addWordExecution(index: Int, noinline execution: GreedyCommandContext<C>.() -> Unit): GreedyStringArgument =
        getOrCreateWord(index) { addExecution(execution) }

    inline fun <reified C : CommandSender> setWordRunnable(index: Int, noinline runnable: GreedyCommandContext<C>.() -> Boolean): GreedyStringArgument =
        getOrCreateWord(index) {
            runnables.clear()
            addWordRunnable<C>(index, runnable)
        }

    inline fun <reified C : CommandSender> addWordRunnable(index: Int, noinline runnable: GreedyCommandContext<C>.() -> Boolean): GreedyStringArgument =
        getOrCreateWord(index) { addRunnable(runnable) }

    inline fun getOrCreateWord(index: Int, function: GreedyWordArgument.() -> Unit): GreedyStringArgument {
        words[index]?.apply { function() } ?: run {
            words[index] = GreedyWordArgument().apply { function() }
        }
        return this
    }

}
