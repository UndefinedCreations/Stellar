package com.undefined.stellar.data.requirement

import org.bukkit.command.CommandSender

fun interface StellarRequirement<C : CommandSender> {
    operator fun invoke(context: C): Boolean
}