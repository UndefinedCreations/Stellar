package com.undefined.stellar

import com.undefined.stellar.argument.misc.addRegistryArgument
import com.undefined.stellar.data.suggestion.Suggestion
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

object EnchantCommand : BaseStellarCommand("enchant", "mcp.kit.enchant") {

    override fun setup(): StellarCommand = createCommand {
        addRegistryArgument("enchantment", RegistryKey.ENCHANTMENT)
            .addSuggestion<Player> { input ->
                var isEmpty: Boolean
                RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT)
                    .filter { it.canEnchantItem(sender.inventory.itemInMainHand) }
                    .map { Suggestion.withText(it.key.toString()) }
                    .also { isEmpty = it.isEmpty() }
                    .filter { it.text.substringAfter(':').startsWith(input, true) }
                    .takeIf { !isEmpty } ?: listOf(Suggestion.withText("Can't apply any enchantments to the item in main hand."))
            }
            .addIntegerArgument("level", 0, 255)
            .addExecution<Player> {
                val enchantment = getArgument<Enchantment>("enchantment")
                val level = getArgument<Int>("level")
                sender.inventory.itemInMainHand.addUnsafeEnchantment(enchantment, level)
            }
    }
}