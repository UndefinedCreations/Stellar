package com.undefined.stellar.sub.brigadier.entity

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.data.execution.CustomStellarExecution
import com.undefined.stellar.data.execution.CustomStellarRunnable
import com.undefined.stellar.sub.BaseStellarSubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
@Suppress("UNCHECKED_CAST")
class EntitySubCommand(parent: AbstractStellarCommand<*>, name: String, val type: EntityDisplayType) : BaseStellarSubCommand<EntitySubCommand>(parent, name) {
    val pluralEntitiesExecutions: MutableList<CustomStellarExecution<*, Any>> = mutableListOf()
    val singularEntityExecutions: MutableList<CustomStellarExecution<*, Any>> = mutableListOf()
    val pluralEntitiesRunnables: MutableList<CustomStellarRunnable<*, Any>> = mutableListOf()
    val singularEntityRunnables: MutableList<CustomStellarRunnable<*, Any>> = mutableListOf()

    inline fun <reified T : CommandSender> addEntitiesSubCommand(noinline execution: T.(List<Entity>) -> Unit): EntitySubCommand {
        pluralEntitiesExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> addSingularEntitySubCommand(noinline execution: T.(Entity) -> Unit): EntitySubCommand {
        singularEntityExecutions.add(CustomStellarExecution(T::class, execution) as CustomStellarExecution<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunEntities(noinline execution: T.(List<Entity>) -> Boolean): EntitySubCommand {
        pluralEntitiesRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }

    inline fun <reified T : CommandSender> alwaysRunSingularEntity(noinline execution: T.(Entity) -> Boolean): EntitySubCommand {
        singularEntityRunnables.add(CustomStellarRunnable(T::class, execution) as CustomStellarRunnable<*, Any>)
        return this
    }
}

enum class EntityDisplayType {
    ENTITY,
    ENTITIES,
    PLAYER,
    PLAYERS
}