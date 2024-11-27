package com.undefined.stellar.sub.brigadier.world

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.arguments.ParticleData
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.BaseStellarSubCommand
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
class ParticleSubCommand(parent: AbstractStellarCommand<*>, name: String) : BaseStellarSubCommand<ParticleSubCommand>(parent, name) {
    inline fun <reified T : CommandSender> addParticleExecution(noinline execution: T.(ParticleData<*>) -> Unit): ParticleSubCommand {
        customExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any?>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunParticle(noinline execution: T.(ParticleData<*>) -> Boolean): ParticleSubCommand {
        customRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any?>)
        return this
    }
}