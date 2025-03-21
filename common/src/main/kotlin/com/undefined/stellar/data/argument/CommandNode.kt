package com.undefined.stellar.data.argument

import org.bukkit.command.CommandSender

/**
 * Represents a list of argument names associated with a function returning their return value.
 */
typealias CommandNode = HashMap<String, (CommandContext<CommandSender>) -> Any?>