package com.undefined.stellar.data.argument

import org.bukkit.command.CommandSender

typealias CommandNode = HashMap<String, (CommandContext<CommandSender>) -> Any?>