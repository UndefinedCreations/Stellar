package com.undefined.stellar

import com.undefined.stellar.data.suggestion.Suggestion
import com.undefined.stellar.util.unregisterCommand
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        unregisterCommand("enchant", this)
        StellarCommand("enchant")
            .addAdvancedRegistryArgument("enchantment", org.bukkit.Registry.ENCHANTMENT, { enchantment ->
                val player: Player = sender as? Player ?: return@addAdvancedRegistryArgument Suggestion.empty()
                if (enchantment.canEnchantItem(player.inventory.itemInMainHand)) Suggestion.withText(enchantment.key.toString()) else Suggestion.empty()
            })
            .addIntegerArgument("level", 0, 255)
            .addExecution<Player> {
                val enchantment = getArgument<Enchantment>("enchantment")
                val level = getArgument<Int>("level")
                sender.inventory.itemInMainHand.addUnsafeEnchantment(enchantment, level)
            }
            .register(this)

        StellarCommand("test")
            .addNamespacedKeyArgument("loc")
            .addSuggestion("ho")
            .addStringArgument("te")
            .register(this)
    }

}