package com.undefined.stellar

import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: Main
    }

    override fun onEnable() {
        INSTANCE = this

        StellarCommand("test")
            .setDescription("This is a description")
            .setUsageText("/test <string>")
            .addStringSubCommand("test")
            .register(this)
    }

}