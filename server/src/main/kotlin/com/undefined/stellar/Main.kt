package com.undefined.stellar

import com.undefined.stellar.util.command
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        command("test", listOf("test")) {
            addStringArgument("test")
                .addExecution<Player> {
                    sender.sendMessage(getArgument<String>("test"))
                }
        }.register(this)
    }

}