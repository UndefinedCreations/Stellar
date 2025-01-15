package com.undefined.stellar.argument.custom

import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.StellarSuggestion
import com.undefined.stellar.data.suggestion.Suggestion
import com.undefined.stellar.exception.UnsupportedArgumentTypeException
import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture

open class ListArgument<T>(
    val type: AbstractStellarArgument<*>,
    val list: CommandContext<CommandSender>.() -> List<T>,
    val converter: (T) -> Suggestion = { Suggestion.withText(it.toString()) },
    val parse: (Any?) -> T?
) : AbstractStellarArgument<ListArgument<T>>(type.parent, type.name) {

    constructor(type: AbstractStellarArgument<*>,
                list: List<T>,
                converter: (T) -> Suggestion = { Suggestion.withText(it.toString()) },
                parse: (Any?) -> T?) : this(type, { list }, converter, parse)

    init {
        if (type is ListArgument<*>) throw UnsupportedArgumentTypeException(type)
    }

    override val suggestions: MutableList<StellarSuggestion<*>>
        get() = (super.suggestions + StellarSuggestion(CommandSender::class) { input ->
            CompletableFuture.completedFuture(getSuggestionList(this).filter {
                println(input)
                it.text.startsWith(input, true)
            })
        }).toMutableList()

    fun getSuggestionList(context: CommandContext<CommandSender>): List<Suggestion> = list(context).map(converter)

}
