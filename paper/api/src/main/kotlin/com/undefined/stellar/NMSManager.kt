package com.undefined.stellar

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.suggestion.Suggestions
import com.undefined.stellar.argument.LiteralArgument
import com.undefined.stellar.argument.list.ListArgument
import com.undefined.stellar.data.argument.ArgumentHelper
import com.undefined.stellar.data.argument.MojangAdapter
import com.undefined.stellar.data.exception.UnsupportedVersionException
import com.undefined.stellar.data.help.CommandAliasHelpTopic
import com.undefined.stellar.data.help.StellarCommandHelpTopic
import com.undefined.stellar.data.suggestion.Suggestion
import com.undefined.stellar.internal.*
import com.undefined.stellar.listener.StellarListener
import com.undefined.stellar.nms.NMS
import com.undefined.stellar.nms.NMSHelper
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.CompletableFuture
import com.mojang.brigadier.suggestion.Suggestion as BrigadierSuggestion

object NMSManager {

    val nms: NMS by lazy { versions[version] ?: throw UnsupportedVersionException(versions.keys) }

    private val version by lazy { Bukkit.getBukkitVersion().split("-")[0] }
    private val versions: Map<String, NMS> = mapOf(
        "1.21.4" to NMS1_21_4,
        "1.21.3" to NMS1_21_3,
        "1.21.2" to NMS1_21_3,
        "1.21.1" to NMS1_21_1,
        "1.21" to NMS1_21,
        "1.20.6" to NMS1_20_6,
        "1.20.5" to NMS1_20_6,
    )

    fun unregister(name: String) {
        val knownCommands: HashMap<String, Command> = Bukkit.getServer().commandMap.knownCommands as HashMap<String, Command>
        for ((key, value) in knownCommands) if (key == name) value.unregister(Bukkit.getServer().commandMap)
        knownCommands.remove(name)
    }

    fun register(command: StellarCommand, plugin: JavaPlugin) {
        if (!StellarListener.hasBeenInitialized) Bukkit.getPluginManager().registerEvents(StellarListener, plugin).also { StellarListener.hasBeenInitialized = true }

        Stellar.commands.add(command)
        val builder = getLiteralArgumentBuilder(command, plugin)
        val dispatcher = nms.getCommandDispatcher()
        val mainNode = dispatcher.register(builder)
        Bukkit.getServer().helpMap.addTopic(StellarCommandHelpTopic(command.name, command.information["Description"] ?: "", command.information.reversed().entries.associateBy({ it.key }) { it.value }) {
            command.requirements.all { it(this) }
        })


        // Register command name with plugin name, e.g. minecraft:ban
        dispatcher.register(LiteralArgumentBuilder.literal<Any>("${plugin.pluginMeta.name.lowercase()}:${command.name}").redirect(mainNode))
        Bukkit.getServer().helpMap.addTopic(StellarCommandHelpTopic("${plugin.pluginMeta.name.lowercase()}:${command.name}", command.information["Description"] ?: "", command.information.reversed().entries.associateBy({ it.key }) { it.value }) {
            command.requirements.all { it(this) }
        })

        for (name in command.aliases) {
            dispatcher.register(LiteralArgumentBuilder.literal<Any>(name).redirect(mainNode))
            Bukkit.getServer().helpMap.addTopic(CommandAliasHelpTopic(name, command.name, Bukkit.getServer().helpMap))
        }
    }

    private fun getLiteralArgumentBuilder(command: AbstractStellarCommand<*>, plugin: JavaPlugin, name: String = command.name): LiteralArgumentBuilder<Any> {
        command.nms = nms
        val builder: LiteralArgumentBuilder<Any> = LiteralArgumentBuilder.literal(name)
        handleArguments(command, builder, plugin)
        handleCommandFunctions(command, builder, plugin)
        return builder
    }

    private fun getRequiredArgumentBuilder(argument: AbstractStellarArgument<*, *>, plugin: JavaPlugin): RequiredArgumentBuilder<Any, *> {
        argument.nms = nms
        val builder: RequiredArgumentBuilder<Any, *> = RequiredArgumentBuilder.argument(argument.name, argument.argumentType ?: nms.getArgumentType(if (argument is ListArgument<*, *>) argument.base else argument, plugin))
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
        handleRequirements(command, builder)
    }

    private fun handleExecutions(command: AbstractStellarCommand<*>, builder: ArgumentBuilder<Any, *>, plugin: JavaPlugin) {
        if (command.executions.isEmpty()) return
        builder.executes { context ->
            val stellarContext = MojangAdapter.getStellarCommandContext(context)
            val rootNodeName = context.rootNode.name.takeIf { it.isNotBlank() }
            val baseCommand = Stellar.getStellarCommand(context.input.split(' ').first()) ?: throw IllegalStateException("Cannot get root command.")

            for (runnable in command.runnables.filter { it.async }) runnable(stellarContext)
            val arguments = ArgumentHelper.getArguments(baseCommand, context, if (rootNodeName != null) 0 else 1)
            for (runnable in baseCommand.runnables.filter { it.async }) if (!runnable(stellarContext)) return@executes 1
            for (argument in arguments + command) for (runnable in argument.runnables.filter { it.async }) if (!runnable(stellarContext)) return@executes 1
            for (execution in command.executions.filter { it.async }) execution(stellarContext)

            Bukkit.getScheduler().runTask(plugin, Runnable {
                for (runnable in baseCommand.runnables.filter { !it.async }) if (!runnable(stellarContext)) return@Runnable
                for (runnable in command.runnables.filter { !it.async }) if (!runnable(stellarContext)) return@Runnable
                for (argument in arguments) for (runnable in argument.runnables.filter { !it.async }) if (!runnable(stellarContext)) return@Runnable
                for (execution in command.executions.filter { !it.async }) execution(stellarContext)
            })
            1
        }
    }

    private fun handleSuggestions(argument: AbstractStellarCommand<*>, argumentBuilder: ArgumentBuilder<Any, *>) {
        if (argument !is AbstractStellarArgument<*, *> || argument.suggestions.isEmpty() || argumentBuilder !is RequiredArgumentBuilder<Any, *>) return
        argumentBuilder.suggests { context, builder ->
            val stellarContext = MojangAdapter.getStellarCommandContext(context)
            val range = StringRange.between(builder.start + argument.suggestionOffset, builder.input.length)

            CompletableFuture.supplyAsync {
                val suggestions: MutableList<Suggestion> = mutableListOf()
                for (suggestion in argument.suggestions) suggestions.addAll(suggestion.get(stellarContext, builder.remaining).get())

                Suggestions(range, suggestions.map { suggestion ->
                    if (suggestion.tooltip == null || suggestion.tooltip!!.isBlank()) BrigadierSuggestion(range, suggestion.text)
                    else BrigadierSuggestion(range, suggestion.text) { suggestion.tooltip }
                })
            }
        }
    }

    private fun handleRequirements(command: AbstractStellarCommand<*>, builder: ArgumentBuilder<Any, *>) {
        builder.requires { source ->
            val sender = NMSHelper.getBukkitSender(source)
            command.requirements.all { it(sender) }
        }
    }

}