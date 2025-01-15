package com.undefined.stellar

import com.undefined.stellar.argument.custom.EnumFormatting
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.CompletableFuture

enum class EntityType {
    ACACIA_BOAT,
    ACACIA_CHEST_BOAT,
    ALLAY,
    AREA_EFFECT_CLOUD,
    ARMADILLO,
    ARMOR_STAND,
    ARROW,
    AXOLOTL,
    BAT,
    BEE,
    BIRCH_BOAT,
    BIRCH_CHEST_BOAT,
    BLAZE,
    BLOCK_DISPLAY,
    BOGGED,
    BREEZE,
    BREEZE_WIND_CHARGE,
    CAMEL,
    CAT,
    CAVE_SPIDER,
    CHERRY_BOAT,
    CHERRY_CHEST_BOAT,
    CHEST_MINECART,
    CHICKEN,
    COD,
    COMMAND_BLOCK_MINECART,
    COW,
    CREAKING,
    SPIDER,
    MINECART
}

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test", "t")
            .addEnumArgument<EntityType>("type", EnumFormatting.LOWERCASE)
            .addExecution<Player> {
                val type = getArgument<EntityType>("type")
                sender.sendMessage(type.name)
            }
            .addStringArgument("test")
            .addFutureSuggestion<Player> {
                CompletableFuture.supplyAsync {
                    return@supplyAsync listOf(Suggestion.withText("test"))
                }
            }
            .addSuggestion("a")
            .register(this)
    }

}