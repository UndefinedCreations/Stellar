package com.undefined.template

import com.undefined.api.UndefinedAPI
import org.bukkit.plugin.java.JavaPlugin

class Template : JavaPlugin() {

    lateinit var undefinedAPI: UndefinedAPI

    override fun onEnable() {
        // Plugin startup logic
        undefinedAPI = UndefinedAPI(this)

    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
