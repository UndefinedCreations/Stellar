package com.undefined.stellar

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralArgument
import com.undefined.stellar.command.AbstractStellarCommand
import com.undefined.stellar.data.argument.ArgumentHelper
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.argument.MojangAdapter
import com.undefined.stellar.data.execution.StellarExecution
import com.undefined.stellar.exception.UnsupportedVersionException
import com.undefined.stellar.v1_21_4.NMS1_21_4
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

object NMSManager {

    val nms: NMS by lazy { versions[version] ?: throw UnsupportedVersionException(versions.keys) }

    val commands: MutableList<AbstractStellarCommand<*>> = mutableListOf()
    private val version by lazy { Bukkit.getBukkitVersion().split("-")[0] }
    private val versions: Map<String, NMS> = mapOf(
        "1.21.4" to NMS1_21_4
    )

    fun register(command: StellarCommand, plugin: JavaPlugin) {
        Stellar.commands.add(command)
        val builder = getLiteralArgumentBuilder(command, plugin)
        nms.register(builder)
    }

    fun getStellarCommand(command: String): AbstractStellarCommand<*>? = commands.firstOrNull { it.name == command } // TODO add aliases

    private fun getLiteralArgumentBuilder(command: AbstractStellarCommand<*>, plugin: JavaPlugin): LiteralArgumentBuilder<Any> {
        val builder: LiteralArgumentBuilder<Any> = LiteralArgumentBuilder.literal(command.name)
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
            if (argument is LiteralArgument) builder.then(getLiteralArgumentBuilder(argument, plugin)) else builder.then(getRequiredArgumentBuilder(argument, plugin))
    }

    private fun handleCommandFunctions(command: AbstractStellarCommand<*>, builder: ArgumentBuilder<Any, *>, plugin: JavaPlugin) {
        builder.executes { context ->
            val stellarContext = MojangAdapter.getStellarCommandContext(context)

            for (runnable in command.runnables.filter { it.async }) runnable(stellarContext)
            val arguments = ArgumentHelper.getArguments(command, context)
            for (argument in arguments) {
                println("argument: ${argument.name}")
                for (runnable in argument.runnables.filter { it.async }) runnable(stellarContext)
            }

            for (execution in command.executions.filter { it.async }) execution(stellarContext)

            Bukkit.getScheduler().runTask(plugin, Runnable {
                for (runnable in command.runnables.filter { !it.async }) runnable(stellarContext)
                for (argument in arguments) {
                    println("argument: ${argument.name}")
                    for (runnable in argument.runnables.filter { !it.async }) runnable(stellarContext)
                }

                for (execution in command.executions.filter { !it.async }) execution(stellarContext)
            })
            1
        }
    }

}