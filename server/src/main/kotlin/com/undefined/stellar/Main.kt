package com.undefined.stellar

import com.undefined.stellar.argument.basic.CustomArgument
import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.argument.basic.StringType
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import java.util.concurrent.CompletableFuture

class TestArgument(parent: AbstractStellarCommand<*>) : CustomArgument<String, String>(StringArgument(parent, "test", StringType.WORD)) {
    override fun parse(context: CommandContext<CommandSender>, value: String): String = "$value!!"

    override fun listSuggestions(context: CommandContext<CommandSender>): CompletableFuture<Collection<Suggestion>> = CompletableFuture.completedFuture(
        listOf(Suggestion.create("test", "tooltip")))

    override fun <T> execution(context: CommandContext<CommandSender>, value: T) {
        context.sender.sendMessage("hi1!!1")
    }
}

class Main : JavaPlugin() {

    override fun onEnable() {
        val list: MutableList<UUID> = mutableListOf()
        for (i in 0..100)
            list.add(UUID.randomUUID())
        StellarCommand("test", "t")
            .addListArgument("test", list, {
                it.toString().replace('-', '_')
            }, {
                println(Thread.currentThread().name)
                UUID.fromString(it.replace('_', '-'))
            }, async = false)
            .addExecution<CommandSender> {
                sender.sendMessage(getArgument<UUID>("test"))
            }
            .register(this)
    }

}