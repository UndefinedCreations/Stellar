package com.undefined.stellar.data.events

import com.undefined.stellar.StellarCommand
import org.bukkit.plugin.java.JavaPlugin

class StellarCommandRegisterEvent(val command: StellarCommand, val plugin: JavaPlugin) : AbstractStellarEvent()
