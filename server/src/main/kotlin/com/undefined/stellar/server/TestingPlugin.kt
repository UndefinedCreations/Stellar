package com.undefined.stellar.server

import com.undefined.stellar.papermc.PaperStellar
import com.undefined.stellar.papermc.command.PaperCommand
import org.bukkit.plugin.java.JavaPlugin

open class TestingPlugin : JavaPlugin() {
    override fun onEnable() {
        val stellar = PaperStellar.builder(this)
            .build()

        stellar.register(PaperCommand.create("main"))
    }
}