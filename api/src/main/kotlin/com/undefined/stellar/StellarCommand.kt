package com.undefined.stellar

import com.undefined.stellar.exception.UnsupportedVersionException
import com.undefined.stellar.listener.StellarListener
import com.undefined.stellar.util.NMSVersion
import org.bukkit.Bukkit
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.plugin.java.JavaPlugin

val registrars = mapOf(
    "1.20.6" to com.undefined.stellar.v1_20_6.BrigadierCommandRegistrar
)

class StellarCommand(name: String, vararg aliases: String) : AbstractStellarCommand<StellarCommand>(name) {
    init {
        this.aliases.addAll(aliases)
    }

    override fun register(plugin: JavaPlugin) {
        val registrar = registrars[NMSVersion.version] ?: throw UnsupportedVersionException()
        registrar.register(this)
        Bukkit.getPluginManager().registerEvents(StellarListener, plugin)
        StellarCommands.initialize(plugin)
        StellarCommands.commands.add(this)
    }

    companion object {
        fun parseAndReturnCancelled(event: PlayerCommandPreprocessEvent): Boolean {
            val registrar = registrars[NMSVersion.version] ?: throw UnsupportedVersionException()
            return registrar.parseAndReturnCancelled(event)
        }
    }
}