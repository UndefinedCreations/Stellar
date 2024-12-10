package com.undefined.stellar

import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test", "othertest")
            .addFailureMessages("")
            .register(this)
    }

}