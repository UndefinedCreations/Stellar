package com.undefined.stellar

import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class StellarCommand(name: String) : AbstractStellarCommand<StellarCommand>(name) {

    val information: SortedMap<String, String> = sortedMapOf()

    init {
        setDescription("A command made with Stellar.")
    }

    override fun setInformation(name: String, text: String): StellarCommand = apply { information[name] = text }
    override fun setDescription(text: String): StellarCommand = setInformation("Description", text)
    override fun setUsageText(text: String): StellarCommand = setInformation("Usage", text)
    override fun clearInformation(): StellarCommand = apply { aliases.clear() }

    override fun register(plugin: JavaPlugin) = apply {
        if (aliases.isNotEmpty()) setInformation("Aliases", aliases.joinToString())
        NMSManager.register(this, plugin)
    }

}