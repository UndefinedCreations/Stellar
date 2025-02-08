package com.undefined.stellar.argument.basic

import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.StellarSuggestion
import com.undefined.stellar.data.suggestion.Suggestion
import com.undefined.stellar.exception.UnsupportedArgumentTypeException
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

open class ListArgument<T, R>(
    val type: AbstractStellarArgument<*, R>,
    val list: CommandContext<CommandSender>.() -> Collection<T>,
    val converter: (T) -> Suggestion = { Suggestion.withText(it.toString()) },
    val parse: (R) -> T?
) : AbstractStellarArgument<ListArgument<T, R>, T>(type.parent, type.name) {

    constructor(type: AbstractStellarArgument<*, R>,
                list: List<T>,
                converter: (T) -> Suggestion = { Suggestion.withText(it.toString()) },
                parse: (R) -> T?) : this(type, { list }, converter, parse)

    init {
        if (type is ListArgument<*, *>) throw UnsupportedArgumentTypeException(type)
    }

    @ApiStatus.Internal fun parse(value: Any?): T? = parse(value as R)

    override val suggestions: MutableList<StellarSuggestion<*>>
        get() = (super.suggestions + StellarSuggestion(CommandSender::class) { input ->
            CompletableFuture.completedFuture(getSuggestionList(this).filter { it.text.startsWith(input, true) })
        }).toMutableList()

    fun getSuggestionList(context: CommandContext<CommandSender>): List<Suggestion> = list(context).map(converter)

}
