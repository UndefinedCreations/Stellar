package com.undefined.stellar

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.suggestion.Suggestion as BrigadierSuggestion
import com.mojang.brigadier.suggestion.Suggestions
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralArgument
import com.undefined.stellar.command.AbstractStellarCommand
import com.undefined.stellar.data.argument.ArgumentHelper
import com.undefined.stellar.data.argument.MojangAdapter
import com.undefined.stellar.data.suggestion.Suggestion
import com.undefined.stellar.exception.UnsupportedVersionException
import com.undefined.stellar.v1_21_4.NMS1_21_4
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.CompletableFuture

object NMSManager {

    val nms: NMS by lazy { versions[version] ?: throw UnsupportedVersionException(versions.keys) }

    private val version by lazy { Bukkit.getBukkitVersion().split("-")[0] }
    private val versions: Map<String, NMS> = mapOf(
        "1.21.4" to NMS1_21_4
    )

    fun register(command: StellarCommand, plugin: JavaPlugin) {
        Stellar.commands.add(command)
        val builder = getLiteralArgumentBuilder(command, plugin)
        val dispatcher = nms.getCommandDispatcher()
        val mainNode = dispatcher.register(builder)

        for (name in command.aliases + "${plugin.description.name}:${command.name}")
            dispatcher.register(LiteralArgumentBuilder.literal<Any>(name).redirect(mainNode))
    }

    private fun getLiteralArgumentBuilder(command: AbstractStellarCommand<*>, plugin: JavaPlugin, name: String = command.name): LiteralArgumentBuilder<Any> {
        val builder: LiteralArgumentBuilder<Any> = LiteralArgumentBuilder.literal(name)
        handleArguments(command, builder, plugin)
        handleCommandFunctions(command, builder, plugin)
        return builder
    }

    private fun getRequiredArgumentBuilder(argument: AbstractStellarArgument<*, *>, plugin: JavaPlugin): RequiredArgumentBuilder<Any, *> {
        val builder: RequiredArgumentBuilder<Any, *> = RequiredArgumentBuilder.argument(argument.name, argument.argumentType ?: nms.getArgumentType(argument))
        handleArguments(argument, builder, plugin)
        handleCommandFunctions(argument, builder, plugin)
        return builder
    }

    private fun handleArguments(command: AbstractStellarCommand<*>, builder: ArgumentBuilder<Any, *>, plugin: JavaPlugin) {
        for (argument in command.arguments)
            if (argument is LiteralArgument) for (name in argument.aliases + argument.name) builder.then(getLiteralArgumentBuilder(argument, plugin, name))
            else builder.then(getRequiredArgumentBuilder(argument, plugin))
    }

    private fun handleCommandFunctions(command: AbstractStellarCommand<*>, builder: ArgumentBuilder<Any, *>, plugin: JavaPlugin) {
        handleExecutions(command, builder, plugin)
        handleSuggestions(command, builder)
    }

    private fun handleExecutions(command: AbstractStellarCommand<*>, builder: ArgumentBuilder<Any, *>, plugin: JavaPlugin) {
        if (command.executions.isEmpty() && command.runnables.isEmpty()) return
        builder.executes { context ->
            val stellarContext = MojangAdapter.getStellarCommandContext(context)
            val rootNodeName = context.rootNode.name.takeIf { it.isNotBlank() }

            for (runnable in command.runnables.filter { it.async }) runnable(stellarContext)
            val arguments = ArgumentHelper.getArguments(command, context, if (rootNodeName != null) 0 else 1)
            for (argument in arguments) for (runnable in argument.runnables.filter { it.async }) runnable(stellarContext)
            for (execution in command.executions.filter { it.async }) execution(stellarContext)

            Bukkit.getScheduler().runTask(plugin, Runnable {
                for (runnable in command.runnables.filter { !it.async }) runnable(stellarContext)
                for (argument in arguments) for (runnable in argument.runnables.filter { !it.async }) runnable(stellarContext)
                for (execution in command.executions.filter { !it.async }) execution(stellarContext)
            })
            1
        }
    }

    private fun handleSuggestions(command: AbstractStellarCommand<*>, argumentBuilder: ArgumentBuilder<Any, *>) {
        if (command !is AbstractStellarArgument<*, *> || command.suggestions.isEmpty() || argumentBuilder !is RequiredArgumentBuilder<Any, *>) return
        argumentBuilder.suggests { context, builder ->
            val stellarContext = MojangAdapter.getStellarCommandContext(context)
            val range = StringRange.between(builder.start, builder.input.length)

            CompletableFuture.supplyAsync {
                val suggestions: MutableList<Suggestion> = mutableListOf()
                for (suggestion in command.suggestions) suggestions.addAll(suggestion.get(stellarContext, builder.remaining).get())
                Suggestions(range, suggestions.map { suggestion ->
                    if (suggestion.tooltip == null || suggestion.tooltip!!.isBlank()) BrigadierSuggestion(range, suggestion.text)
                    else BrigadierSuggestion(range, suggestion.text) { suggestion.tooltip }
                })
            }
        }
    }

}