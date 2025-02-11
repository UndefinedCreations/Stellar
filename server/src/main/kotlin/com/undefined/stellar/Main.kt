package com.undefined.stellar

import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val list: MutableList<String> = mutableListOf("test", "hidden", "visible")
        StellarCommand("test", "t")
            .addStringListArgument("name", list)
            .addPhraseArgument("test")
            .addWordExecution<CommandSender>(0) {
                println("happened!")
                sender.sendMessage(getGlobalArgument<String>("name").toString())
            }
            .register(this)
    }

}