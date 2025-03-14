package com.undefined.stellar

import com.undefined.stellar.argument.misc.addRegistryArgument
import com.undefined.stellar.data.suggestion.Suggestion
import com.undefined.stellar.util.unregisterCommand
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import org.bukkit.Registry
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        unregisterCommand("enchant")
        StellarCommand("test")
            .addListArgument("test", listOf())
    }

}