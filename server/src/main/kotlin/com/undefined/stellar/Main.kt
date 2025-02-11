package com.undefined.stellar

import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val list: MutableList<String> = mutableListOf("test", "hidden", "visible")
        val main = StellarCommand("test", "t")
        main.addListArgument("uuid", list, {
            if (it == "hidden") null else it
        }, { it.replace('_', '-') })
            .addAsyncExecution<CommandSender> {
                sender.sendMessage(Thread.currentThread().name)
            }
            .register(this)
    }

}