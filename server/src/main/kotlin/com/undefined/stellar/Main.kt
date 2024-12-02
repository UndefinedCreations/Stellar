package com.undefined.stellar

import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.LinkedList

typealias CommandNode = LinkedList<Any?>

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("test", description = "this is a description", "alias")
            .addAlias("othertest")
            .setDescription("This is a description")
            .setUsageText("/test <other>")
            .addStringSubCommand("test")
            .addStringSubCommand("test")
            .addSuggestions(Suggestion("suggestion", "t"))
            .alwaysRun<Player> {
                sendMessage("This will always run no matter what")
                true
            }
            .addExecution<Player> { sendMessage("Execution") }
            .addSubCommandExecution<Player> { context ->
                sendMessage(context.getSubCommand<String>(1))
            }
            .addFailureExecution<Player> { sendMessage("Failure!") }
            .addFailureExecution<Player> { sendMessage("Incorrect!") }
            .hideDefaultFailureMessages()
            .register(this)

//        val main = StellarCommand("test")
//        main.addOnlinePlayersSubCommand("onlinePlayers")
//            .addExecution<Player> { context ->
//                context.input
//                context.<String>get(1)
//                context<String>[1]
//            }
    }

}