package com.undefined.stellar.sub

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.StellarSuggestion
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

abstract class AbstractStellarSubCommand<T>(val parent: AbstractStellarCommand<*>, name: String) : AbstractStellarCommand<AbstractStellarSubCommand<*>>(name) {

    val suggestions: MutableList<StellarSuggestion<*>> = mutableListOf()

    fun addSuggestion(list: List<String>): T {
        suggestions.add(StellarSuggestion(CommandSender::class) { list })
        return this as T
    }

    fun addSuggestion(vararg list: String): T {
        suggestions.add(StellarSuggestion(CommandSender::class) { list.toList() })
        return this as T
    }

    inline fun <reified C : CommandSender> addSuggestion(noinline suggestion: C.(input: String) -> List<String>): T {
        suggestions.add(StellarSuggestion(C::class, suggestion))
        return this as T
    }

    override fun getBase(): AbstractStellarCommand<*> = parent.getBase()
    override fun register(plugin: JavaPlugin) = parent.register(plugin)

}