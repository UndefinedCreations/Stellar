package com.undefined.stellar.argument.types.custom

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.data.suggestion.StellarSuggestion
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender

open class ListArgument<T>(
    parent: AbstractStellarCommand<*>,
    val type: AbstractStellarArgument<*>,
    val list: () -> List<T>,
    val converter: (T) -> Suggestion = { Suggestion.withText(it.toString()) },
    val parse: (Any?) -> T?
) : AbstractStellarArgument<ListArgument<T>>(parent, type.name) {

    constructor(parent: AbstractStellarCommand<*>,
                type: AbstractStellarArgument<*>,
                list: List<T>,
                converter: (T) -> Suggestion = { Suggestion.withText(it.toString()) },
                parse: (Any?) -> T?) : this(parent, type, { list }, converter, parse)

    override val suggestions: MutableList<StellarSuggestion<*>>
        get() = (super.suggestions + StellarSuggestion(CommandSender::class) { getSuggestionList() }).toMutableList()

    fun getSuggestionList(): List<Suggestion> = list().map(converter)

}
