package com.undefined.stellar.data.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

abstract class AbstractStellarEvent : Event() {
    override fun getHandlers(): HandlerList {
        return HANDLERS_LIST
    }

    companion object {
        private val HANDLERS_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS_LIST
        }
    }
}