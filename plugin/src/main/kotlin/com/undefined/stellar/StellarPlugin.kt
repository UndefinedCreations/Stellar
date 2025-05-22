package com.undefined.stellar

import org.bukkit.plugin.java.JavaPlugin

class StellarPlugin : JavaPlugin() {

    override fun onEnable() {
        INSTANCE = this

        Config.init()
        MainStellarCommand().register(this)
    }

    companion object {
        lateinit var INSTANCE: StellarPlugin
    }

}