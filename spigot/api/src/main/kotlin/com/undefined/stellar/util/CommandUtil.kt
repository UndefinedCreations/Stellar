@file:JvmName("CommandUtil")

package com.undefined.stellar.util

import com.mojang.brigadier.tree.CommandNode
import com.undefined.stellar.NMSManager
import com.undefined.stellar.StellarCommandImpl
import com.undefined.stellar.StellarConfig
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.SimpleCommandMap
import org.bukkit.help.HelpTopic
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * Unregisters a command and removes itself from the /help command, which it does within a [BukkitTask].
 *
 * @param name The command names to be unregistered.
 * @param plugin The [JavaPlugin] instance to be used to run the [BukkitTask].
 */
@Suppress("UNCHECKED_CAST")
@JvmOverloads
fun unregisterCommand(name: String, plugin: JavaPlugin = StellarConfig.plugin ?: throw IllegalArgumentException("Plugin cannot be null!")) {
    val dispatcher = NMSManager.nms.getCommandDispatcher()
    val commandMap = Bukkit.getServer().javaClass.getDeclaredField("commandMap").apply { isAccessible = true }[Bukkit.getServer()] as SimpleCommandMap
    val knownCommands: HashMap<String, Command> = SimpleCommandMap::class.java.getDeclaredField("knownCommands").apply { isAccessible = true }[commandMap] as HashMap<String, Command>
    val helpTopics: TreeMap<String, HelpTopic> = Bukkit.getHelpMap()::class.java.getDeclaredField("helpTopics").apply { isAccessible = true }[Bukkit.getHelpMap()] as TreeMap<String, HelpTopic>

    val children: MutableMap<String, CommandNode<Any>> = CommandNode::class.java.getDeclaredField("children").apply { isAccessible = true }.get(dispatcher.root) as MutableMap<String, CommandNode<Any>>
    val literals: MutableMap<String, CommandNode<Any>> = CommandNode::class.java.getDeclaredField("literals").apply { isAccessible = true }.get(dispatcher.root) as MutableMap<String, CommandNode<Any>>
    val arguments: MutableMap<String, CommandNode<Any>> = CommandNode::class.java.getDeclaredField("arguments").apply { isAccessible = true }.get(dispatcher.root) as MutableMap<String, CommandNode<Any>>

    children.remove(name)
    literals.remove(name)
    arguments.remove(name)
    Bukkit.getScheduler().runTask(plugin, Runnable {
        knownCommands[name]?.unregister(commandMap)
        helpTopics.remove("/$name")
    })
    for (player in Bukkit.getOnlinePlayers()) player.updateCommands()
}

@Deprecated("Use the kotlin module")
@ApiStatus.ScheduledForRemoval()
fun command(name: String, description: String, permissions: List<String>, aliases: List<String>, builder: StellarCommandImpl.() -> Unit): StellarCommandImpl {
    val command = StellarCommandImpl(name, permissions, aliases)
    command.setDescription(description)
    command.builder()
    return command
}

@Deprecated("Use the kotlin module")
@ApiStatus.ScheduledForRemoval()
fun command(name: String, description: String, builder: StellarCommandImpl.() -> Unit): StellarCommandImpl = command(name, description, listOf(), listOf(), builder)

@Deprecated("Use the kotlin module")
@ApiStatus.ScheduledForRemoval()
fun command(name: String, description: String, permissions: List<String>, builder: StellarCommandImpl.() -> Unit): StellarCommandImpl = command(name, description, permissions, listOf(), builder)

@Deprecated("Use the kotlin module")
@ApiStatus.ScheduledForRemoval()
fun command(name: String, permissions: List<String>, builder: StellarCommandImpl.() -> Unit): StellarCommandImpl = command(name, "", permissions, listOf(), builder)

@Deprecated("Use the kotlin module")
@ApiStatus.ScheduledForRemoval()
fun command(name: String, permissions: List<String>, aliases: List<String>, builder: StellarCommandImpl.() -> Unit): StellarCommandImpl = command(name, "", permissions, aliases, builder)

@Deprecated("Use the kotlin module")
@ApiStatus.ScheduledForRemoval()
fun command(name: String, builder: StellarCommandImpl.() -> Unit): StellarCommandImpl = command(name, "", builder)

@Deprecated("Use the kotlin module")
@ApiStatus.ScheduledForRemoval()
fun command(name: String, description: String, permissions: List<String>, aliases: List<String>): StellarCommandImpl {
    val command = StellarCommandImpl(name, permissions, aliases)
    command.setDescription(description)
    return command
}

@Deprecated("Use the kotlin module")
@ApiStatus.ScheduledForRemoval()
fun command(name: String, description: String): StellarCommandImpl = command(name, description, listOf(), listOf())

@Deprecated("Use the kotlin module")
@ApiStatus.ScheduledForRemoval()
fun command(name: String, description: String, permissions: List<String>): StellarCommandImpl = command(name, description, permissions, listOf())

@Deprecated("Use the kotlin module")
@ApiStatus.ScheduledForRemoval()
fun command(name: String, permissions: List<String>): StellarCommandImpl = command(name, "", permissions, listOf())

@Deprecated("Use the kotlin module")
@ApiStatus.ScheduledForRemoval()
fun command(name: String, permissions: List<String>, aliases: List<String>): StellarCommandImpl = command(name, "", permissions, aliases)

@Deprecated("Use the kotlin module")
@ApiStatus.ScheduledForRemoval()
fun command(name: String): StellarCommandImpl = command(name, "")