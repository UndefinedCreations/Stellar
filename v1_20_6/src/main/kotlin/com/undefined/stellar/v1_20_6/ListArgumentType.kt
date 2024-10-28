package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import java.util.concurrent.CompletableFuture

class ListArgumentType<T> private constructor(private val elementType: ArgumentType<out T>) : ArgumentType<List<T>?> {

    @Throws(CommandSyntaxException::class)
    override fun parse(reader: StringReader): List<T> {
        val result: MutableList<T> = mutableListOf()
        while (reader.canRead()) {
            val type = elementType
            val element = type.parse(reader)
            result.add(element)
            while (reader.canRead() && Character.isWhitespace(reader.peek())) reader.read()
        }
        return result
    }

    override fun getExamples(): Collection<String> {
        return elementType.examples
    }

    override fun <S> listSuggestions(
        context: CommandContext<S>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        return elementType.listSuggestions(context, builder)
    }

    companion object {
        fun <E> list(expectedElementType: ArgumentType<out E>): ListArgumentType<E> {
            return ListArgumentType(expectedElementType)
        }

        fun <T, S> getList(context: CommandContext<S>, name: String): List<T> {
            return context.getArgument(name, List::class.java) as List<T>
        }
    }

}