package com.undefined.stellar

import com.undefined.stellar.argument.LiteralArgument
import com.undefined.stellar.argument.basic.StringArgument
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val cmd = StellarCommand("test")
            .addArgument(StringArgument("string"))
            .addAsyncRunnable<Player> {
                sender.sendMessage("runnable: ${Thread.currentThread().name}")
                true
            }
            .addArgument(LiteralArgument("a"))
            .addExecution<Player> {
                sender.sendMessage("execution: ${Thread.currentThread().name}")
            }
            .register(this)
    }

}