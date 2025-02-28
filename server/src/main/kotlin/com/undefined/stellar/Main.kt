package com.undefined.stellar

import com.undefined.stellar.data.suggestion.Suggestion
import com.undefined.stellar.util.command
import com.undefined.stellar.util.unregisterCommand
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class EnchantCommand : BaseStellarCommand("enchant", "mcp.kit.enchant") {
    override fun setup(): StellarCommand = createCommand {
        addRegistryArgument("enchantment", Registry.ENCHANTMENT, { enchantment ->
            val player: Player = sender as? Player ?: return@addRegistryArgument ""
            if (enchantment.canEnchantItem(player.inventory.itemInMainHand)) enchantment.key.toString() else ""
        })
            .addIntegerArgument("level")
            .addExecution<Player> {
                val enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).getOrThrow(getArgument<NamespacedKey>("enchantment"))
                val level = getArgument<Int>("level")
                sender.inventory.itemInMainHand.addUnsafeEnchantment(enchantment, level)
            }
    }
}

class Main : JavaPlugin() {

    override fun onEnable() {
        EnchantCommand().register(this)
    }

}