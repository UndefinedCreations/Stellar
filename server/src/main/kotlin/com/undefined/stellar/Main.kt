package com.undefined.stellar

import com.undefined.stellar.argument.types.misc.UUIDArgument
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class TestCommand : BaseStellarCommand("test", "description") {
    override fun setup() = createCommand {
        addExecution<Player> {
            sender.sendMessage("hi!")
        }
    }

    override fun arguments(): List<StellarArgument> = listOf(TestArgument(this.command))
}

class TestArgument(parent: AbstractStellarCommand<*>) : StellarArgument(UUIDArgument(parent, "sub")) {
    override fun setup() = createArgument {
        addExecution<Player> {
            sender.sendMessage("Hello there! Argument.")
        }
    }
}

class Main : JavaPlugin() {

    override fun onEnable() {
        val command = TestCommand()
        command.register(this)
        println(command.name)
    }

}