package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import java.util.concurrent.CompletableFuture

//class ListArgumentType<T> private constructor(val list: List<T>) : ArgumentType<List<*>?> {
//
//    @Throws(CommandSyntaxException::class)
//    override fun parse(reader: StringReader): List<T> {
//        return reader.readBoolean()
//    }
//
//    override fun <S> listSuggestions(
//        context: CommandContext<S>,
//        builder: SuggestionsBuilder
//    ): CompletableFuture<Suggestions> {
//        if ("true".startsWith(builder.remainingLowerCase)) {
//            builder.suggest("true")
//        }
//
//        if ("false".startsWith(builder.remainingLowerCase)) {
//            builder.suggest("false")
//        }
//
//        return builder.buildFuture()
//    }
//
//    override fun getExamples(): Collection<String> {
//        return EXAMPLES
//    }
//
//    companion object {
//        private val EXAMPLES: Collection<String> = mutableListOf("true", "false")
//
//        fun <T> list(list: List<T>): ListArgumentType {
//            return ListArgumentType()
//        }
//
//        fun getList(context: CommandContext<*>, name: String?): List<T> {
//            return context.getArgument(name, List::class.java) as List<T>
//        }
//    }
//}