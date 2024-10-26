package com.undefined.stellar

import com.undefined.stellar.sub.BaseStellarSubCommand
import org.bukkit.command.CommandSender

abstract class BaseStellarCommand(val name: String) {

    val aliases: MutableList<String> = mutableListOf()
    val requirements: MutableList<StellarRequirement<*>> = mutableListOf()
    val subCommands: MutableList<BaseStellarSubCommand> = mutableListOf()
    val executions: MutableList<StellarExecution<*>> = mutableListOf()

    fun addAlias(name: String): BaseStellarCommand {
        aliases.add(name)
        return this
    }

    inline fun <reified T : CommandSender> addRequirement(noinline requirement: T.() -> Boolean): BaseStellarCommand {
        requirements.add(StellarRequirement(T::class, requirement))
        return this
    }

    fun addSubCommand(name: String): BaseStellarCommand {
        val subCommand = BaseStellarSubCommand(this, name)
        subCommands.add(subCommand)
        return subCommand
    }

    inline fun <reified T : CommandSender> addExecute(noinline execution: T.() -> Unit): BaseStellarCommand {
        executions.add(StellarExecution(T::class, execution))
        return this
    }

    abstract fun register()

}