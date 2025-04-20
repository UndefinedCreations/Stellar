package com.undefined.stellar.util

import com.undefined.stellar.NMSManager
import com.undefined.stellar.Stellar
import com.undefined.stellar.StellarCommand
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.SimpleCommandMap
import org.bukkit.help.HelpTopic
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.util.*
import kotlin.collections.HashMap

object CommandUtil {
    /**
     * Unregisters a command and removes their respective [HelpTopic]. It does this within a [BukkitTask].
     *
     * @param name The command names to be unregistered.
     * @param plugin The [JavaPlugin] instance to be used to run the [BukkitTask].
     */
    @JvmOverloads
    fun unregisterCommands(name: String, plugin: JavaPlugin = Stellar.plugin ?: throw IllegalArgumentException("Plugin cannot be null!")) {
        Bukkit.getScheduler().runTask(plugin, Runnable {
            val dispatcher = NMSManager.nms.getCommandDispatcher()
            val commandMap = Bukkit.getServer().javaClass.getDeclaredField("commandMap").apply { isAccessible = true }[Bukkit.getServer()] as SimpleCommandMap
            val knownCommands: HashMap<String, Command> = SimpleCommandMap::class.java.getDeclaredField("knownCommands").apply { isAccessible = true }[commandMap] as HashMap<String, Command>
            val helpTopics: TreeMap<String, HelpTopic> = Bukkit.getHelpMap()::class.java.getDeclaredField("helpTopics").apply { isAccessible = true }[Bukkit.getHelpMap()] as TreeMap<String, HelpTopic>

            dispatcher.root.children.remove(dispatcher.root.getChild(name))
            knownCommands[name]?.unregister(commandMap)
            helpTopics.remove("/$name")
            for (player in Bukkit.getOnlinePlayers()) player.updateCommands()
        })
    }
}

fun command(name: String, description: String, permissions: List<String>, aliases: List<String>, builder: StellarCommand.() -> Unit): StellarCommand {
    val command = StellarCommand(name, permissions, aliases)
    command.setDescription(description)
    command.builder()
    return command
}

fun command(name: String, description: String, builder: StellarCommand.() -> Unit): StellarCommand = command(name, description, listOf(), listOf(), builder)
fun command(name: String, description: String, permissions: List<String>, builder: StellarCommand.() -> Unit): StellarCommand = command(name, description, permissions, listOf(), builder)
fun command(name: String, permissions: List<String>, builder: StellarCommand.() -> Unit): StellarCommand = command(name, "", permissions, listOf(), builder)
fun command(name: String, permissions: List<String>, aliases: List<String>, builder: StellarCommand.() -> Unit): StellarCommand = command(name, "", permissions, aliases, builder)
fun command(name: String, builder: StellarCommand.() -> Unit): StellarCommand = command(name, "", builder)

fun command(name: String, description: String, permissions: List<String>, aliases: List<String>): StellarCommand {
    val command = StellarCommand(name, permissions, aliases)
    command.setDescription(description)
    return command
}

fun command(name: String, description: String): StellarCommand = command(name, description, listOf(), listOf())
fun command(name: String, description: String, permissions: List<String>): StellarCommand = command(name, description, permissions, listOf())
fun command(name: String, permissions: List<String>): StellarCommand = command(name, "", permissions, listOf())
fun command(name: String, permissions: List<String>, aliases: List<String>): StellarCommand = command(name, "", permissions, aliases)
fun command(name: String): StellarCommand = command(name, "")