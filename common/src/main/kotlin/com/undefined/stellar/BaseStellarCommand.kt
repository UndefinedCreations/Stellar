package com.undefined.stellar

import com.undefined.stellar.data.requirement.PermissionStellarRequirement
import com.undefined.stellar.data.execution.StellarExecution
import com.undefined.stellar.data.requirement.StellarRequirement
import com.undefined.stellar.sub.SubCommandHandler
import org.bukkit.command.CommandSender

@Suppress("UNCHECKED_CAST")
abstract class BaseStellarCommand<T>(val name: String) : SubCommandHandler() {

    override fun getThis(): BaseStellarCommand<T> = this

    val aliases: MutableList<String> = mutableListOf()
    val requirements: MutableList<StellarRequirement<*>> = mutableListOf()
    val permissionRequirements: MutableList<PermissionStellarRequirement> = mutableListOf()
    val executions: MutableList<StellarExecution<*>> = mutableListOf()

    fun addAlias(name: String): T {
        aliases.add(name)
        return this as T
    }

    fun addRequirement(permission: String): T {
        permissionRequirements.add(PermissionStellarRequirement(1, permission))
        return this as T
    }

    fun addRequirement(permissionLevel: Int): T {
        permissionRequirements.add(PermissionStellarRequirement(permissionLevel))
        return this as T
    }

    inline fun <reified C : CommandSender> addRequirement(noinline requirement: C.() -> Boolean): T {
        requirements.add(StellarRequirement(C::class, requirement))
        return this as T
    }

    inline fun <reified C : CommandSender> addExecution(noinline execution: C.() -> Unit): T {
        executions.add(StellarExecution(C::class, execution))
        return this as T
    }

    abstract fun register()

}