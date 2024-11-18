package com.undefined.stellar.listener

import com.undefined.stellar.StellarCommand
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class CommandRegisterEvent(val command: StellarCommand, val plugin: JavaPlugin) : Event() {

    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS
        }
    }

}