package com.undefined.stellar

import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.lang.IllegalArgumentException

enum class Status {
    SUCCESS,
    FAILURE,
    PENDING
}

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test")
            .addEnumSubCommand<Status>("enum", { enum -> enum.name.lowercase().replaceFirstChar { char -> char.uppercase() } }, {
                try {
                    Status.valueOf(it.uppercase())
                } catch (e: IllegalArgumentException) {
                    null
                }
            })
            .addSuggestion("fdafsdaafs", "Fdafs")
            .addSuggestion("dsada", "dsadsa")
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