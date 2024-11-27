package com.undefined.stellar

import com.undefined.stellar.sub.BaseStellarSubCommand
import com.undefined.stellar.sub.brigadier.misc.UUIDSubCommand
import com.undefined.stellar.sub.brigadier.primitive.StringSubCommand
import com.undefined.stellar.sub.brigadier.primitive.StringType
import com.undefined.stellar.sub.custom.CustomSubCommand
import com.undefined.stellar.sub.custom.CustomSubCommandInfo
import com.undefined.stellar.sub.custom.OnlinePlayersSubCommand
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class CustomSubCommandTest(parent: AbstractStellarCommand<*>, name: String) : CustomSubCommand<String>(parent, name, StringSubCommand(parent, name, StringType.SINGLE_WORD)) {
    override fun getSubCommandsList(): List<BaseStellarSubCommand<*>> = listOf(OnlinePlayersSubCommand(parent, name) { Bukkit.getOnlinePlayers().toList() })

    override fun parse(sender: CommandSender, input: String): String {
        println("parse!")
        return input.removePrefix("#")
    }

    override fun <T> execution(info: CustomSubCommandInfo<T>) {
        info.sender.sendMessage("execution: ${info.input}, ${info.value}")
        println("execution!")
    }

    override fun <T> failureExecution(info: CustomSubCommandInfo<T>) {
        info.sender.sendMessage("failure: ${info.input}, ${info.value}")
        println("failure!")
    }

    override fun requirement(): Boolean {
        return true
    }

    override fun listSuggestions(sender: CommandSender): List<String> = listOf(sender.name, "red", "blue", "green", "#a")
}

class Main : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: Main
    }

    override fun onEnable() {
        INSTANCE = this

        val main = StellarCommand("test")
        main.addCustomSubCommand(CustomSubCommandTest(main.getBase(), "test"))
            .register(this)

//        val context = CommandContext()

//        val main = StellarCommand("test")
//        main.addSubCommand("sub")
//            .addStringSubCommand("string")
//            .addStringExecution<Player> {
//
//            }
//        main.register(this)
    }

}