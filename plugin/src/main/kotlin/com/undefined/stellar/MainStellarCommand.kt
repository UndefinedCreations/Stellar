package com.undefined.stellar

import com.undefined.stellar.argument.basic.StringType
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

class MainStellarCommand : BaseStellarCommand("stellar", "com.undefined.stellar") {

    override fun setup(): StellarCommand = createCommand {
        setDescription("Manages any stellar commands, and plugin configurations.")

        addArgument("list")
            .addExecution<CommandSender> {
                val commands = StellarConfig.commands.joinToString { it.name }
                sender.sendMessage("${ChatColor.GRAY}Here is a list of Stellar commands: $commands.")
            }

        addArgument("blacklisted-commands")
            .addExecution<CommandSender> {
                val blacklistedCommands = Config.blacklistedCommand.joinToString()
                if (Config.blacklistedCommand.isEmpty()) return@addExecution sender.sendMessage("${ChatColor.GRAY}There are no blacklisted commands.")
                sender.sendMessage("${ChatColor.GRAY}Here is the list of blacklisted commands: $blacklistedCommands")
            }
            .then("add") {
                addStringArgument("name")
                    .addExecution<CommandSender> {
                        val name: String by args

                        Config.blacklistedCommand = Config.blacklistedCommand.apply { add(name) }
                        Config.save()

                        sender.sendMessage("${ChatColor.GRAY}Successfully added '$name' to blacklisted commands. Restart the server to commence updates.")
                    }
            }
            .then("remove") {
                addStringArgument("name")
                    .addExecution<CommandSender> {
                        val name: String by args
                        if (!Config.blacklistedCommand.remove(name)) return@addExecution sender.sendMessage("${ChatColor.RED}Could not find a command with the name '$name'.")
                        sender.sendMessage("${ChatColor.GRAY}Successfully remove command with the name '$name'.")
                    }
            }
            .then("list") {
                addExecution<CommandSender> {
                    val blacklistedCommands = Config.blacklistedCommand.joinToString()
                    if (Config.blacklistedCommand.isEmpty()) return@addExecution sender.sendMessage("${ChatColor.GRAY}There are no blacklisted commands.")
                    sender.sendMessage("${ChatColor.GRAY}Here is the list of blacklisted commands: $blacklistedCommands")
                }
            }

        addArgument("prefix")
            .addExecution<CommandSender> {
                if (StellarConfig.prefix.isBlank())
                    return@addExecution sender.sendMessage("${ChatColor.GRAY}The current prefix is not set.")
                sender.sendMessage("${ChatColor.GRAY}The current prefix is '${StellarConfig.prefix}'.")
            }
            .then("set") {
                addStringArgument("newValue", StringType.ALPHANUMERIC_WORD)
                    .addExecution<CommandSender> {
                        val newValue: String by args
                        StellarConfig.setPrefix(newValue)
                        Config.prefix = newValue
                        Config.save()
                        sender.sendMessage("${ChatColor.GRAY}Successfully set prefix to '${newValue}'.")
                    }
            }
            .then("clear") {
                addExecution<CommandSender> {
                    Config.prefix = ""
                    Config.save()
                    sender.sendMessage("${ChatColor.GRAY}Successfully cleared the default prefix.")
                }
            }
            .then("get") {
                addExecution<CommandSender> {
                    if (Config.prefix.isBlank())
                        return@addExecution sender.sendMessage("${ChatColor.GRAY}The current prefix is not set.")
                    sender.sendMessage("${ChatColor.GRAY}The current prefix is '${Config.prefix}'.")
                }
            }
    }

}