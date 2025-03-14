package com.undefined.stellar.argument.list

import com.undefined.stellar.AbstractStellarArgument
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.exception.UnsupportedArgumentTypeException
import com.undefined.stellar.data.suggestion.ExecutableSuggestion
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

open class ListArgument<T, R>(
    val base: AbstractStellarArgument<*, R>,
    val list: CommandContext<CommandSender>.() -> Iterable<T>,
    val converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
    val parse: CommandSender.(R) -> T?,
    val async: Boolean = false
) : AbstractStellarArgument<ListArgument<T, R>, R>(base.name, base.argumentType) {

    constructor(
        type: AbstractStellarArgument<*, R>,
        list: Iterable<T>,
        converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
        parse: CommandSender.(R) -> T?,
        async: Boolean = true
    ) : this(type, { list }, converter, parse, async)

    init {
        if (base is ListArgument<*, *>) throw UnsupportedArgumentTypeException(base)
    }

    @Suppress("UNCHECKED_CAST")
    @ApiStatus.Internal fun parseInternal(sender: CommandSender, value: Any): T? = parse(sender, value as R)

    override val suggestions: MutableSet<ExecutableSuggestion<*>>
        get() = (super.suggestions + ExecutableSuggestion(CommandSender::class) { context, input ->
            if (async) CompletableFuture.supplyAsync { getSuggestionList(context, input) }
            else CompletableFuture.completedFuture(getSuggestionList(context, input))
        }).toMutableSet()

    open fun getSuggestionList(context: CommandContext<CommandSender>, input: String): List<Suggestion> =
        list(context).mapNotNull { converter(context.sender, it) }.filter { it.text.startsWith(input, true) }

}