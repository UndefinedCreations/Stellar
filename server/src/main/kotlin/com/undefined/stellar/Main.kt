package com.undefined.stellar

import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.full.declaredFunctions

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarConfig.setPlugin(this)

        StellarCommand("menu")
            .then("triumph") {
                addStringArgument("name")
                    .addSuggestions(*TriumphGUIs::class.declaredFunctions.map { it.name }.toTypedArray())
            }
            .register(this)
    }

}