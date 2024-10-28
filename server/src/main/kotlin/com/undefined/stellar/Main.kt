package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

enum class Status {
    SUCCESS,
    FAILURE,
    PENDING
}

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addEnumSubCommand<Status>("enum", { enum -> enum.name.lowercase().replaceFirstChar { char -> char.uppercase() } }, { Status.valueOf(it.uppercase()) })
            .addSuggestion<Player> {
                return@addSuggestion listOf("fdafsdaafs", "Fdafs")
            }
            .addEnumExecution<Player> { enum ->
                sendMessage(enum.name)
            }
            .addStringListSubCommand("list", listOf("test", "othertest"))
            .addListExecution<Player> {
                sendMessage(it)
            }
            .addSubCommand("test")
            .addSubCommand("othertest")
            .register()
    }

}