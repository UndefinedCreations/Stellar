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
    val converter: CommandContext<CommandSender>.(T) -> Suggestion = { Suggestion.withText(it.toString()) },
    val parse: CommandContext<CommandSender>.(R) -> T?,
    val async: Boolean = false
) : AbstractStellarArgument<ListArgument<T, R>, T>(type.parent, type.name) {

    constructor(type: AbstractStellarArgument<*, R>,
                list: Collection<T>,
                converter: CommandContext<CommandSender>.(T) -> Suggestion = { Suggestion.withText(it.toString()) },
                parse: CommandContext<CommandSender>.(R) -> T?,
                async: Boolean = true) : this(type, { list }, converter, parse, async)

    init {
        if (type is ListArgument<*, *>) throw UnsupportedArgumentTypeException(type)
    }

    @Suppress("UNCHECKED_CAST")
    @ApiStatus.Internal fun parseInternal(context: CommandContext<CommandSender>, value: Any?): T? = parse.invoke(context, value as R)

    override val suggestions: MutableList<StellarSuggestion<*>>
        get() = (super.suggestions + StellarSuggestion(CommandSender::class) { input ->
            if (async)
                CompletableFuture.supplyAsync { getSuggestionList(this).filter { it.text.startsWith(input, true) } }
            else
                CompletableFuture.completedFuture(getSuggestionList(this).filter { it.text.startsWith(input, true) })
        }).toMutableList()

    fun getSuggestionList(context: CommandContext<CommandSender>): List<Suggestion> = list(context).map { converter(context, it) }

}
