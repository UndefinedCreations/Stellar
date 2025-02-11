package com.undefined.stellar

import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.argument.basic.StringType
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class Main : JavaPlugin() {

    override fun onEnable() {
        val list: MutableList<UUID> = mutableListOf()
        for (i in 0..100)
            list.add(UUID.randomUUID())
        val main = StellarCommand("test", "t")
        main.addListArgument("uuid", list, {
            println(sender.name)
            it.toString().replace('-', '_')
        }, { UUID.fromString(it.replace('_', '-')) })
            .addAsyncExecution<CommandSender> {
                sender.sendMessage(Thread.currentThread().name)
            }
            .register(this)
    }

}