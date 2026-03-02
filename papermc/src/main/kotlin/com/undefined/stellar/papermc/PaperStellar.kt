package com.undefined.stellar.papermc

import com.undefined.stellar.command.Command
import com.undefined.stellar.papermc.command.PaperCommand
import com.undefined.stellar.papermc.context.PaperCommandContext
import org.bukkit.plugin.java.JavaPlugin

interface PaperStellar {

    fun plugin(): JavaPlugin
    fun register(command: PaperCommand)

    companion object {
        fun builder(plugin: JavaPlugin): Builder = Builder(plugin)
    }

    class Builder internal constructor(private var plugin: JavaPlugin) {
        fun setPlugin(plugin: JavaPlugin): Builder = apply {
            this.plugin = plugin
        }

        fun build(): PaperStellar = PaperStellarImpl(plugin)
    }

}