package com.undefined.stellar

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralArgument
import com.undefined.stellar.command.AbstractStellarCommand
import com.undefined.stellar.data.argument.CommandContextAdapter
import com.undefined.stellar.exception.UnsupportedVersionException
import com.undefined.stellar.v1_21_4.NMS1_21_4
import org.bukkit.Bukkit
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
        val builder = getLiteralArgumentBuilder(command)
        nms.register(builder)
    }

    fun getStellarCommand(command: String): AbstractStellarCommand<*>? = commands.firstOrNull { it.name == command } // TODO add aliases

    private fun getLiteralArgumentBuilder(command: AbstractStellarCommand<*>): LiteralArgumentBuilder<Any> {
        val builder: LiteralArgumentBuilder<Any> = LiteralArgumentBuilder.literal(command.name)
        handleArguments(command, builder)
        handleCommandFunctions(command, builder)
        return builder
    }

    private fun getRequiredArgumentBuilder(argument: AbstractStellarArgument<*, *>): RequiredArgumentBuilder<Any, *> {
        val builder: RequiredArgumentBuilder<Any, *> = RequiredArgumentBuilder.argument(argument.name, argument.argumentType ?: nms.getArgumentType(argument))
        handleArguments(argument, builder)
        handleCommandFunctions(argument, builder)
        return builder
    }

    private fun handleArguments(command: AbstractStellarCommand<*>, builder: ArgumentBuilder<Any, *>) {
        for (argument in command.arguments)
            if (argument is LiteralArgument) builder.then(getLiteralArgumentBuilder(argument)) else builder.then(getRequiredArgumentBuilder(argument))
    }

    private fun handleCommandFunctions(command: AbstractStellarCommand<*>, builder: ArgumentBuilder<Any, *>) {
        builder.executes { context ->
            for (execution in command.executions) execution(CommandContextAdapter.getStellarCommandContext(context))
            1
        }
    }

}