package com.undefined.stellar

import com.undefined.stellar.argument.types.primitive.StringType
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {

    override fun onEnable() {
        val main = StellarCommand("test", description = "this is a description", "othertest")
        main.addArgument("sub")
            .addRequirement(3)
            .addGlobalFailureMessage("Hi!")
            .addExecution<Player> {
                source.sendMessage("Hi!")
                source.sendMessage(getArgument<String>(0))
            }
        main.addArgument("one")
            .addStringArgument("string", StringType.SINGLE_WORD)
            .addRunnable<Player> {
                source.sendMessage("This will always run!")
                true
            }
            .addExecution<Player> {
                source.sendMessage("String!")
                source.sendMessage(getArgument<String>(0))
            }
            .addFailureMessage("<red>This is a message!")
            .addFailureExecution<Player> {
                source.sendMessage("failure execution")
            }
            .addIntegerArgument("test")
            .addExecution<Player> {
                source.sendMessage("String!")
                source.sendMessage(getArgument<Int>(1).toDouble().toString())
            }
            .register(this)
    }

}