package com.undefined.stellar.nms

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.BukkitCtx
import com.undefined.stellar.StellarConfig
import com.undefined.stellar.data.argument.ArgumentHelper
import org.bukkit.command.CommandSender
import java.util.concurrent.CompletableFuture

abstract class AbstractNMSManager {

    protected fun handleExecutions(
        command: AbstractStellarCommand<*>,
        stellarContext: com.undefined.stellar.data.argument.CommandContext<CommandSender>,
        context: CommandContext<Any>,
        subArguments: List<AbstractStellarCommand<*>>
    ): Int {
        if (subArguments.any { it.runnables.any { it.alwaysApplicable } }) {
            for (argument in subArguments) for (runnable in argument.runnables) runnable(stellarContext)
            return Command.SINGLE_SUCCESS
        }

        val rootNodeName = context.rootNode.name.takeIf { it.isNotBlank() }
        val baseCommand = StellarConfig.getStellarCommand(context.input.split(' ').first())
            ?: throw IllegalStateException("Cannot get root command.")

        val arguments = ArgumentHelper.getArguments(baseCommand, context, if (rootNodeName != null) 0 else 1)

        var cmdFuture: CompletableFuture<Boolean>? = null
        for (runnable in baseCommand.runnables) {
            cmdFuture = if (cmdFuture == null) runnable(stellarContext)
            else cmdFuture.thenCompose { res ->
                if (!res) return@thenCompose CompletableFuture.completedFuture(false)
                runnable(stellarContext)
            }
        }
        if (cmdFuture == null) cmdFuture = CompletableFuture.completedFuture(true)

        cmdFuture!!.thenCompose { res ->
            if (!res) return@thenCompose CompletableFuture.completedFuture(res)
            val actualArguments = arguments.filter { it.runnables.isNotEmpty() && it != command } + command

            var future: CompletableFuture<Boolean>? = null
            for (argument in actualArguments) {
                for (runnable in argument.runnables) {
                    future = if (future == null) runnable(stellarContext)
                    else future.thenCompose { res ->
                        if (!res) return@thenCompose CompletableFuture.completedFuture(res)
                        runnable(stellarContext)
                    }
                }
            }
            future
        }.thenAccept { res ->
            if (!res) return@thenAccept
            for (execution in command.executions) {
                if (!execution.async) {
                    BukkitCtx {
                        execution(stellarContext)
                    }
                } else {
                    execution(stellarContext)
                }
            }
        }

        /*
        for (runnable in baseCommand.runnables.filter { it.async }) if (!runnable(stellarContext)) return@executes 1
        for (argument in arguments.filter { it != command } + command) for (runnable in argument.runnables.filter { it.async }) if (!runnable(
                stellarContext
            )
        ) return@executes 1
        for (execution in command.executions.filter { it.async }) execution(stellarContext)

        Bukkit.getScheduler().runTask(plugin, Runnable {
            for (runnable in baseCommand.runnables.filter { !it.async }) if (!runnable(stellarContext)) return@Runnable
            for (runnable in command.runnables.filter { !it.async }) if (!runnable(stellarContext)) return@Runnable
            for (argument in arguments) for (runnable in argument.runnables.filter { !it.async }) if (!runnable(
                    stellarContext
                )
            ) return@Runnable
            for (execution in command.executions.filter { !it.async }) execution(stellarContext)
        })
         */
        return 1
    }

}