package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val main = StellarCommand("test", description = "this is a description", "othertest")
        main.addIntegerArgument("int")
            .addGreedyStringArgument("args")
            .onWord(0) {
                addExecution<Player> {
                    source.sendMessage(getArgument(0))
                }
                addRunnable<Player> {
                    source.sendMessage("runnable")
                    true
                }
            }
            .addWordExecution<Player>(1) {
                source.sendMessage("Execution")
                println("execution")
            }
            .register(this)
    }

}