package com.undefined.stellar

import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val main = StellarCommand("test")
        main.addGreedyStringArgument("args")
            .addWordSuggestions(1, "test")
            .register(this)
    }

}