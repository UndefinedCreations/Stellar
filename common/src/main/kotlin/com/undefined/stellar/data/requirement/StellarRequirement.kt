package com.undefined.stellar.data.requirement

import org.bukkit.command.CommandSender

/**
 * Represents a functional interface used for command requirements.
 */
fun interface StellarRequirement<C : CommandSender> {
    operator fun invoke(context: C): Boolean
}