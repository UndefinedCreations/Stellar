package com.undefined.stellar

import com.undefined.stellar.argument.LiteralArgument
import com.undefined.stellar.argument.basic.*
import com.undefined.stellar.argument.block.BlockDataArgument
import com.undefined.stellar.argument.block.BlockPredicateArgument
import com.undefined.stellar.argument.entity.EntityAnchorArgument
import com.undefined.stellar.argument.entity.EntityArgument
import com.undefined.stellar.argument.entity.EntityDisplayType
import com.undefined.stellar.argument.item.ItemSlotArgument
import com.undefined.stellar.argument.item.ItemStackArgument
import com.undefined.stellar.argument.item.ItemStackPredicateArgument
import com.undefined.stellar.argument.list.EnumArgument
import com.undefined.stellar.argument.list.ListArgument
import com.undefined.stellar.argument.list.OnlinePlayersArgument
import com.undefined.stellar.argument.math.*
import com.undefined.stellar.argument.misc.NamespacedKeyArgument
import com.undefined.stellar.argument.misc.UUIDArgument
import com.undefined.stellar.argument.phrase.PhraseArgument
import com.undefined.stellar.argument.player.GameModeArgument
import com.undefined.stellar.argument.player.GameProfileArgument
import com.undefined.stellar.argument.scoreboard.*
import com.undefined.stellar.argument.structure.LootTableArgument
import com.undefined.stellar.argument.structure.MirrorArgument
import com.undefined.stellar.argument.structure.StructureRotationArgument
import com.undefined.stellar.argument.text.ColorArgument
import com.undefined.stellar.argument.text.ComponentArgument
import com.undefined.stellar.argument.text.MessageArgument
import com.undefined.stellar.argument.text.StyleArgument
import com.undefined.stellar.argument.world.*
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.argument.EnumFormatting
import com.undefined.stellar.data.execution.ExecutableExecution
import com.undefined.stellar.data.execution.ExecutableRunnable
import com.undefined.stellar.data.execution.StellarExecution
import com.undefined.stellar.data.execution.StellarRunnable
import com.undefined.stellar.data.failure.HideDefaultFailureMessages
import com.undefined.stellar.data.requirement.ExecutableRequirement
import com.undefined.stellar.data.requirement.StellarRequirement
import com.undefined.stellar.data.suggestion.Suggestion
import com.undefined.stellar.nms.NMS
import com.undefined.stellar.nms.NMSHelper
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import java.lang.Enum.valueOf
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * This is the base of any command, whether it's an argument or a root command.
 *
 * @param name The command name.
 */
@Suppress("UNCHECKED_CAST")
abstract class AbstractStellarCommand<T : AbstractStellarCommand<T>>(val name: String) {

    @ApiStatus.Internal
    lateinit var nms: NMS

    @ApiStatus.Internal
    val aliases: MutableSet<String> = mutableSetOf()
    @ApiStatus.Internal
    val requirements: MutableList<ExecutableRequirement<*>> = mutableListOf()
    @ApiStatus.Internal
    val arguments: MutableSet<AbstractStellarArgument<*, *>> = mutableSetOf()
    @ApiStatus.Internal
    val lastExecutions = HashMap<UUID, Long>()
    @ApiStatus.Internal
    val executions: MutableSet<ExecutableExecution<*>> = mutableSetOf()
    @ApiStatus.Internal
    val runnables: MutableSet<ExecutableRunnable<*>> = mutableSetOf()
    @ApiStatus.Internal
    val failureExecutions: MutableSet<ExecutableExecution<*>> = mutableSetOf()
    @ApiStatus.Internal
    open val globalFailureExecutions: MutableSet<ExecutableExecution<*>> = mutableSetOf()
    @ApiStatus.Internal
    var hideDefaultFailureMessages: HideDefaultFailureMessages = HideDefaultFailureMessages(hide = false, global = false)

    /**
     * Add a command alias in addition to the existing command aliases.
     */
    fun addAlias(alias: String): T = apply { aliases.add(alias) } as T

    /**
     * Adds command aliases in addition to the existing command aliases.
     */
    fun addAliases(vararg aliases: String): T = apply { this.aliases.addAll(aliases) } as T

    /**
     * Clears all command aliases.
     */
    fun clearAliases(): T = apply { aliases.clear() } as T

    /**
     * Adds a requirement that must be met for the command to be available to the player. Only works in Kotlin.
     *
     * @param C The type of CommandSender.
     * @param requirement The condition that must be met.
     * @return The modified command object.
     */
    inline fun <reified C : CommandSender> addRequirement(noinline requirement: C.() -> Boolean): T = apply {
        requirements.add(ExecutableRequirement(C::class, requirement))
    } as T

    /**
     * Adds a requirement that must be met for the command to be available to the player. Also works in Java.
     *
     * @param requirement The condition that must be met.
     * @return The modified command object.
     */
    fun addRequirement(requirement: StellarRequirement<CommandSender>): T = apply {
        requirements.add(ExecutableRequirement(CommandSender::class, requirement))
    } as T

    /**
     * Adds a Bukkit permission requirement for the command to be available to the player.
     *
     * @param permission The permission string.
     * @return The modified command object.
     */
    fun addRequirement(permission: String): T = addRequirement<CommandSender> { hasPermission(permission) }

    /**
     * Adds a level requirement for the command to be available to the player.
     *
     * Note: this only applies to players, not all command senders.
     *
     * @param level The required permission level.
     * @return The modified command object.
     */
    fun addRequirement(level: Int): T = addRequirement<Player> { NMSHelper.hasPermission(this, level) }

    /**
     * Adds multiple Bukkit permission requirements for the command to be available to the player.
     *
     * @param permissions The permission strings.
     * @return The modified command object.
     */
    fun addRequirements(vararg permissions: String): T = addRequirement<CommandSender> { permissions.all { hasPermission(it) } }

    /**
     * Adds multiple level requirements for the command to be available to the player.
     *
     * Note: this only applies to players, not all command senders.
     *
     * @param levels The required permission levels.
     * @return The modified command object.
     */
    fun addRequirements(vararg levels: Int): T = addRequirement<Player> { levels.all { NMSHelper.hasPermission(this, it) } }

    /**
     * Adds a cooldown to the command for each player.
     *
     * Prevents a player from re-executing the command until the specified duration has passed
     * since their last successful execution. If the player is still on cooldown, the [block] function is run.
     *
     * @param duration The cooldown duration in milliseconds.
     * @param block A function providing a [CommandContext] and the remaining time in milliseconds which executes anytime a player is on cooldown.
     * @return The modified command object.
     */
    fun addCooldown(
        duration: Long,
        block: CommandContext<Player>.(remaining: Long) -> Unit = { remaining ->
            sender.sendMessage("${ChatColor.RED}Please wait ${TimeUnit.MILLISECONDS.toSeconds(remaining)} more seconds!")
        },
    ): T = apply {
        addRunnable<Player> {
            val currentTime = System.currentTimeMillis()

            if (sender.uniqueId in lastExecutions) {
                val cooldownEnd = lastExecutions[sender.uniqueId]!!
                val remaining = cooldownEnd - currentTime

                if (remaining > 0) {
                    block(remaining)
                    return@addRunnable false
                }
            }

            lastExecutions[sender.uniqueId] = currentTime + duration
            return@addRunnable true
        }
    } as T

    /**
     * Adds a cooldown to the command for each player.
     *
     * Prevents a player from re-executing the command until the specified duration has passed
     * since their last successful execution. If the player is still on cooldown, the [block] function is run.
     *
     * @param duration The cooldown duration in whatever is specified in [unit].
     * @param unit A [TimeUnit] that determines what time unit the [duration] will be counted in.
     * @param block A function providing a [CommandContext] and the remaining time in milliseconds which executes anytime a player is on cooldown.
     * @return The modified command object.
     */
    fun addCooldown(
        duration: Long,
        unit: TimeUnit,
        block: CommandContext<Player>.(remaining: Long) -> Unit = { remaining ->
            sender.sendMessage("${ChatColor.RED}Please wait ${TimeUnit.MILLISECONDS.toSeconds(remaining)} more seconds!")
        },
    ): T = addCooldown(TimeUnit.MILLISECONDS.convert(duration, unit), block)

    /**
     * Adds a cooldown to the command for each player.
     *
     * Prevents a player from re-executing the command until the specified duration has passed
     * since their last successful execution. If the player is still on cooldown, the [message] function is run.
     *
     * @param duration The cooldown duration in milliseconds.
     * @param message A function providing a [CommandContext] and the remaining time in milliseconds returning a [Component] which is sent to the player when they are on cooldown.
     * @return The modified command object.
     */
    fun addComponentMessageCooldown(
        duration: Long,
        message: CommandContext<Player>.(remaining: Long) -> Component = { remaining ->
            Component.text("Please wait ${TimeUnit.MILLISECONDS.toSeconds(remaining)} more seconds!", NamedTextColor.RED)
        },
    ): T = addCooldown(duration) { remaining ->
        sender.sendMessage(LegacyComponentSerializer.legacySection().serialize(message(remaining)))
    }

    /**
     * Adds a cooldown to the command for each player.
     *
     * Prevents a player from re-executing the command until the specified duration has passed
     * since their last successful execution. If the player is still on cooldown, the [message] function is run.
     *
     * @param duration The cooldown duration in whatever is specified in [unit].
     * @param unit A [TimeUnit] that determines what time unit the [duration] will be counted in.
     * @param message A function providing a [CommandContext] and the remaining time in milliseconds returning a [Component] which is sent to the player when they are on cooldown.
     * @return The modified command object.
     */
    fun addComponentMessageCooldown(
        duration: Long, unit: TimeUnit,
        message: CommandContext<Player>.(remaining: Long) -> Component = { remaining ->
            Component.text("Please wait ${TimeUnit.MILLISECONDS.toSeconds(remaining)} more seconds!", NamedTextColor.RED)
        },
    ): T = addComponentMessageCooldown(TimeUnit.MILLISECONDS.convert(duration, unit), message)

    /**
     * Adds a cooldown to the command for each player.
     *
     * Prevents a player from re-executing the command until the specified duration has passed
     * since their last successful execution. If the player is still on cooldown, the [message] function is run.
     *
     * @param duration The cooldown duration in milliseconds.
     * @param message A function providing a [CommandContext] and the remaining time in milliseconds returning a [String] which is parsed with [MiniMessage] and sent to the player when they are on cooldown.
     * @return The modified command object.
     */
    fun addMessageCooldown(
        duration: Long,
        message: CommandContext<Player>.(remaining: Long) -> String = { remaining ->
            "<red>Please wait ${TimeUnit.MILLISECONDS.toSeconds(remaining)} more seconds!"
        },
    ): T = addCooldown(duration) { remaining ->
        sender.sendMessage(LegacyComponentSerializer.legacySection().serialize(Stellar.miniMessage.deserialize(message(remaining))))
    }

    /**
     * Adds a cooldown to the command for each player.
     *
     * Prevents a player from re-executing the command until the specified duration has passed
     * since their last successful execution. If the player is still on cooldown, the [message] function is run.
     *
     * @param duration The cooldown duration in whatever is specified in [unit].
     * @param unit A [TimeUnit] that determines what time unit the [duration] will be counted in.
     * @param message A function providing a [CommandContext] and the remaining time in milliseconds returning a [String] which is parsed with [MiniMessage] which is sent to the player when they are on cooldown.
     * @return The modified command object.
     */
    fun addMessageCooldown(
        duration: Long, unit: TimeUnit,
        message: CommandContext<Player>.(remaining: Long) -> String = { remaining ->
            "<red>Please wait ${TimeUnit.MILLISECONDS.toSeconds(remaining)} more seconds!"
        },
    ): T = addMessageCooldown(TimeUnit.MILLISECONDS.convert(duration, unit), message)

    /**
     * Adds a cooldown to the command for each player.
     *
     * Prevents a player from re-executing the command until the specified duration has passed
     * since their last successful execution. If the player is still on cooldown, the [message] function is run.
     *
     * @param duration The cooldown duration in milliseconds.
     * @param message A function providing a [CommandContext] and the remaining time in milliseconds returning a [String] and sent to the player when they are on cooldown. The [String] is not modified in the slightest.
     * @return The modified command object.
     */
    fun addRawMessageCooldown(
        duration: Long,
        message: CommandContext<Player>.(remaining: Long) -> String = { remaining ->
            "${ChatColor.RED}Please wait ${TimeUnit.MILLISECONDS.toSeconds(remaining)} more seconds!"
        },
    ): T = addCooldown(duration) { remaining ->
        sender.sendMessage(LegacyComponentSerializer.legacySection().serialize(Stellar.miniMessage.deserialize(message(remaining))))
    }

    /**
     * Adds a cooldown to the command for each player.
     *
     * Prevents a player from re-executing the command until the specified duration has passed
     * since their last successful execution. If the player is still on cooldown, the [message] function is run.
     *
     * @param duration The cooldown duration in whatever is specified in [unit].
     * @param unit A [TimeUnit] that determines what time unit the [duration] will be counted in.
     * @param message A function providing a [CommandContext] and the remaining time in milliseconds returning a [String] which is sent to the player when they are on cooldown. The [String] is not modified in the slightest.
     * @return The modified command object.
     */
    fun addRawMessageCooldown(
        duration: Long, unit: TimeUnit,
        message: CommandContext<Player>.(remaining: Long) -> String = { remaining ->
            "${ChatColor.RED}Please wait ${TimeUnit.MILLISECONDS.toSeconds(remaining)} more seconds!"
        },
    ): T = addRawMessageCooldown(TimeUnit.MILLISECONDS.convert(duration, unit), message)

    /**
     * Adds an execution to the command. Only works in Kotlin.
     *
     * @param C The type of CommandSender.
     * @param execution The execution block.
     * @return The modified command object.
     */
    inline fun <reified C : CommandSender> addExecution(noinline execution: CommandContext<C>.() -> Unit): T = apply {
        executions.add(ExecutableExecution(C::class, execution, false))
    } as T

    /**
     * Adds an execution to the command. Also works in Java.
     *
     * @param execution The execution block.
     * @return The modified command object.
     */
    fun addExecution(execution: StellarExecution<CommandSender>): T = apply {
        executions.add(ExecutableExecution(CommandSender::class, execution, false))
    } as T

    /**
     * Adds an async execution to the command. Only works in Kotlin.
     *
     * @param C The type of CommandSender.
     * @param execution The execution block.
     * @return The modified command object.
     */
    inline fun <reified C : CommandSender> addAsyncExecution(noinline execution: CommandContext<C>.() -> Unit): T = apply {
        executions.add(ExecutableExecution(C::class, execution, true))
    } as T

    /**
     * Adds an async execution to the command. Also works in Java.
     *
     * @param execution The execution block.
     * @return The modified command object.
     */
    fun addAsyncExecution(execution: StellarExecution<CommandSender>): T = apply {
        executions.add(ExecutableExecution(CommandSender::class, execution, true))
    } as T

    /**
     * Adds a runnable to the command. Only works in Kotlin.
     *
     * @param C The type of CommandSender.
     * @param runnable The execution block.
     * @return The modified command object.
     */
    inline fun <reified C : CommandSender> addRunnable(noinline runnable: CommandContext<C>.() -> Boolean): T = apply {
        runnables.add(ExecutableRunnable(C::class, runnable, false))
    } as T

    /**
     * Adds a runnable to the command. Also works in Java.
     *
     * @param runnable The execution block.
     * @return The modified command object.
     */
    fun addRunnable(runnable: StellarRunnable<CommandSender>): T = apply {
        runnables.add(ExecutableRunnable(CommandSender::class, runnable, false))
    } as T

    /**
     * Adds an async runnable to the command. Only works in Kotlin.
     *
     * @param C The type of CommandSender.
     * @param runnable The execution block.
     * @return The modified command object.
     */
    inline fun <reified C : CommandSender> addAsyncRunnable(noinline runnable: CommandContext<C>.() -> Boolean): T = apply {
        runnables.add(ExecutableRunnable(C::class, runnable, true))
    } as T

    /**
     * Adds an async runnable to the command. Also works in Java.
     *
     * @param runnable The execution block.
     * @return The modified command object.
     */
    fun addAsyncRunnable(runnable: StellarRunnable<CommandSender>): T = apply {
        runnables.add(ExecutableRunnable(CommandSender::class, runnable, true))
    } as T

    /**
     * Adds a failure execution to the command to be displayed when the command fails. Only works in Kotlin.
     *
     * @param C The type of CommandSender.
     * @param execution The execution block.
     * @return The modified command object.
     */
    inline fun <reified C : CommandSender> addFailureExecution(noinline execution: CommandContext<C>.() -> Unit): T = apply {
        failureExecutions.add(ExecutableExecution(C::class, execution, false))
    } as T

    /**
     * Adds a failure execution to the command to be displayed when the command fails. Also works in Java.
     *
     * @param execution The execution block.
     * @return The modified command object.
     */
    fun addFailureExecution(execution: StellarExecution<CommandSender>): T = apply {
        failureExecutions.add(ExecutableExecution(CommandSender::class, execution, false))
    } as T

    /**
     * Adds a failure execution to the _root_ command to be displayed when the command fails. Only works in Kotlin.
     *
     * @param C The type of CommandSender.
     * @param execution The execution block.
     * @return The modified command object.
     */
    inline fun <reified C : CommandSender> addGlobalFailureExecution(noinline execution: CommandContext<C>.() -> Unit): T = apply {
        globalFailureExecutions.add(ExecutableExecution(C::class, execution, false))
    } as T

    /**
     * Adds a failure execution to the _root_ command to be displayed when the command fails. Also works in Java.
     *
     * @param execution The execution block.
     * @return The modified command object.
     */
    fun addGlobalFailureExecution(execution: StellarExecution<CommandSender>): T = apply {
        globalFailureExecutions.add(ExecutableExecution(CommandSender::class, execution, false))
    } as T

    /**
     * Adds a failure message to the command to be displayed when the command fails.
     *
     * @param message The message component to be sent.
     * @return The modified command object.
     */
    fun addFailureMessage(message: Component): T = apply {
        failureExecutions.add(ExecutableExecution(CommandSender::class, { it.sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(message)) }, false))
    } as T

    /**
     * Adds a failure message to the _root_ command to be displayed when the command fails.
     *
     * @param message The message component to be sent.
     * @return The modified command object.
     */
    fun addGlobalFailureMessage(message: Component): T = apply {
        globalFailureExecutions.add(ExecutableExecution(CommandSender::class, { it.sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(message)) }, false))
    } as T

    /**
     * Adds a failure message to the command to be displayed when the command fails.
     *
     * @param message The message to be sent.
     * @return The modified command object.
     */
    fun addFailureMessage(message: String): T = apply {
        val component = Stellar.miniMessage.deserialize(message)
        failureExecutions.add(ExecutableExecution(CommandSender::class, { it.sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(component)) }, false))
    } as T

    /**
     * Adds a failure message to the _root_ command to be displayed when the command fails.
     *
     * @param message The message to be sent.
     * @return The modified command object.
     */
    fun addGlobalFailureMessage(message: String): T = apply {
        val component = Stellar.miniMessage.deserialize(message)
        globalFailureExecutions.add(ExecutableExecution(CommandSender::class, { it.sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(component)) }, false))
    } as T

    /**
     * Hides the default Minecraft failure messages for the command.
     *
     * @param hide Whether to hide the messages.
     * @param global Whether to apply it on the root command.
     * @return The modified command object.
     */
    @JvmOverloads
    fun hideDefaultFailureMessages(hide: Boolean = true, global: Boolean = false): T = apply {
        hideDefaultFailureMessages = HideDefaultFailureMessages(hide, global)
    } as T

    @ApiStatus.Internal
    open fun hasGlobalHiddenDefaultFailureMessages(): Boolean = hideDefaultFailureMessages.hide && hideDefaultFailureMessages.global

    // Arguments
    /**
     * Adds the given argument to the command and return the argument.
     */
    fun <T : AbstractStellarArgument<*, *>> addArgument(argument: T): T = argument.apply {
        argument.parent = this@AbstractStellarCommand
        this@AbstractStellarCommand.arguments.add(argument)
    }

    /**
     * Adds a [LiteralArgument] to the command with the given name and aliases.
     * @return The created [BooleanArgument].
     */
    fun addArgument(name: String, vararg aliases: String): LiteralArgument = addArgument(LiteralArgument(name).apply { addAliases(*aliases) })

    /**
     * Adds a [LiteralArgument] to the command with the given name and aliases.
     * @return The created [BooleanArgument].
     */
    fun addLiteralArgument(name: String, vararg aliases: String): LiteralArgument = addArgument(LiteralArgument(name).apply { addAliases(*aliases) })

    // Basic
    /**
     * Adds a [BooleanArgument] to the command with the given name.
     * @return The created [BooleanArgument].
     */
    fun addBooleanArgument(name: String): BooleanArgument = addArgument(BooleanArgument(name))

    /**
     * Adds a [DoubleArgument] to the command with the given name.
     *
     * @param minimum The minimum allowed value (default: [Double.MIN_VALUE]).
     * @param maximum The maximum allowed value (default: [Double.MAX_VALUE]).
     * @return The created [DoubleArgument].
     */
    @JvmOverloads
    fun addDoubleArgument(name: String, minimum: Double = Double.MIN_VALUE, maximum: Double = Double.MAX_VALUE): DoubleArgument = addArgument(DoubleArgument(name, minimum, maximum))

    /**
     * Adds a [FloatArgument] to the command with the given name.
     *
     * @param minimum The minimum allowed value (default: [Float.MIN_VALUE]).
     * @param maximum The maximum allowed value (default: [Float.MAX_VALUE]).
     * @return The created [FloatArgument].
     */
    @JvmOverloads
    fun addFloatArgument(name: String, minimum: Float = Float.MIN_VALUE, maximum: Float = Float.MAX_VALUE): FloatArgument = addArgument(FloatArgument(name, minimum, maximum))

    /**
     * Adds an [IntegerArgument] to the command with the given name.
     *
     * @param minimum The minimum allowed value (default: [Int.MIN_VALUE]).
     * @param maximum The maximum allowed value (default: [Int.MAX_VALUE]).
     * @return The created [IntegerArgument].
     */
    @JvmOverloads
    fun addIntegerArgument(name: String, minimum: Int = Int.MIN_VALUE, maximum: Int = Int.MAX_VALUE): IntegerArgument = addArgument(IntegerArgument(name, minimum, maximum))

    /**
     * Adds a [LongArgument] to the command with the given name.
     *
     * @param minimum The minimum allowed value (default: [Long.MIN_VALUE]).
     * @param maximum The maximum allowed value (default: [Long.MAX_VALUE]).
     * @return The created [LongArgument].
     */
    @JvmOverloads
    fun addLongArgument(name: String, minimum: Long = Long.MIN_VALUE, maximum: Long = Long.MAX_VALUE): LongArgument = addArgument(LongArgument(name, minimum, maximum))

    /**
     * Adds a [StringArgument] to the command with the given name.
     * @return The created [StringArgument].
     */
    @JvmOverloads
    fun addStringArgument(name: String, type: StringType = StringType.WORD): StringArgument = addArgument(StringArgument(name, type))

    // Block
    /**
     * Adds a [BlockDataArgument] to the command with the given name.
     * @return The created [BlockDataArgument].
     */
    fun addBlockDataArgument(name: String): BlockDataArgument = addArgument(BlockDataArgument(name))

    /**
     * Adds a [BlockPredicateArgument] to the command with the given name.
     * @return The created [BlockPredicateArgument].
     */
    fun addBlockPredicateArgument(name: String): BlockPredicateArgument = addArgument(BlockPredicateArgument(name))

    // Entity
    /**
     * Adds an [EntityAnchorArgument] to the command with the given name.
     * @return The created [EntityAnchorArgument].
     */
    fun addEntityAnchorArgument(name: String): EntityAnchorArgument = addArgument(EntityAnchorArgument(name))

    /**
     * Adds an [EntityArgument] to the command with the given name.
     * @return The created [EntityArgument].
     */
    fun addEntityArgument(name: String, type: EntityDisplayType): EntityArgument = addArgument(EntityArgument(name, type))

    // Item
    /**
     * Adds an [ItemSlotArgument] to the command with the given name.
     * @return The created [ItemSlotArgument].
     */
    fun addItemSlotArgument(name: String, multiple: Boolean = false): ItemSlotArgument = addArgument(ItemSlotArgument(name, multiple))

    /**
     * Adds an [ItemStackArgument] to the command with the given name.
     * @return The created [ItemStackArgument].
     */
    fun addItemStackArgument(name: String): ItemStackArgument = addArgument(ItemStackArgument(name))

    /**
     * Adds an [ItemStackPredicateArgument] to the command with the given name.
     * @return The created [ItemStackPredicateArgument].
     */
    fun addItemPredicateArgument(name: String): ItemStackPredicateArgument = addArgument(ItemStackPredicateArgument(name))

    // List
    /**
     * Adds a [ListArgument] to the command with the given name. It uses its [StringArgument] as a base wrapper.
     *
     * @param list A function return a list of possible values.
     * @param tooltip A function that assigns each suggestion with a tooltip. If the value is null, it will not add a tooltip.
     * @param type The [StringType] it will use in the [StringArgument].
     * @return The created [ListArgument].
     */
    @JvmOverloads
    fun addListArgument(
        name: String,
        list: CommandContext<CommandSender>.() -> List<String>,
        tooltip: (String) -> String? = { null },
        type: StringType = StringType.WORD
    ): ListArgument<String, String> = addArgument(ListArgument(StringArgument(name, type), list, { Suggestion.create(it, tooltip(it)) }, { it }))

    /**
     * Adds a [ListArgument] to the command with the given name. It uses its [StringArgument] as a base wrapper.
     *
     * @param list The list of possible values.
     * @param tooltip A function that assigns each suggestion with a tooltip. If the value is null, it will not add a tooltip.
     * @param type The [StringType] it will use in the [StringArgument].
     * @return The created [ListArgument].
     */
    @JvmOverloads
    fun addListArgument(
        name: String,
        list: List<String>,
        tooltip: (String) -> String? = { null },
        type: StringType = StringType.WORD
    ): ListArgument<String, String> = addArgument(ListArgument(StringArgument(name, type), list, { Suggestion.create(it, tooltip(it)) }, { it }))

    /**
     * Adds a [ListArgument] to the command with the given name.
     *
     * @param list The list of possible values.
     * @param parse A function to parse the returned [String] into type `T`.
     * @param converter A function to convert a value into a [String] (default: uses `toString()`).
     * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
     * @return The created [ListArgument].
     */
    @JvmOverloads
    fun <T> addListArgument(
        name: String,
        list: List<T>,
        parse: CommandSender.(String) -> T,
        converter: CommandSender.(T) -> String? = { it.toString() },
        async: Boolean = false,
    ): ListArgument<T, String> = addArgument(ListArgument(StringArgument(name, StringType.WORD), list, { converter(it)?.let { Suggestion.withText(it) } }, parse, async))

    /**
     * Adds a [ListArgument] to the command with the given name.
     *
     * @param list A function returning the list of possible values.
     * @param parse A function to parse the returned [String] into type `T`.
     * @param converter A function to convert a value into a [String] (default: uses `toString()`).
     * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
     * @return The created [ListArgument].
     */
    @JvmOverloads
    fun <T> addListArgument(
        name: String,
        list: CommandContext<CommandSender>.() -> List<T>,
        parse: CommandSender.(String) -> T,
        converter: CommandSender.(T) -> String? = { it.toString() },
        async: Boolean = false,
    ): ListArgument<T, String> = addArgument(ListArgument(StringArgument(name, StringType.WORD), list, { converter(it)?.let { Suggestion.withText(it) } }, parse, async))

    /**
     * Adds a [ListArgument] to the command wrapped around the given [AbstractStellarCommand].
     *
     * @param type The base argument the list is wrapped around to.
     * @param list The list of possible values.
     * @param parse A function to parse the returned [String] into type `T`.
     * @param converter A function to convert a value into a [String] (default: uses `toString()`).
     * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
     * @return The created [ListArgument].
     */
    @JvmOverloads
    fun <T, R> addListArgument(
        type: AbstractStellarArgument<*, R>,
        list: List<T>,
        parse: CommandSender.(R) -> T,
        converter: CommandSender.(T) -> String? = { it.toString() },
        async: Boolean = false,
    ): ListArgument<T, R> = addArgument(ListArgument(type, list, { converter(it)?.let { Suggestion.withText(it) } }, parse, async))

    /**
     * Adds a [ListArgument] to the command wrapped around the given [AbstractStellarCommand].
     *
     * @param type The base argument the list is wrapped around to.
     * @param list A function returning the list of possible values.
     * @param parse A function to parse the returned [String] into type `T`.
     * @param converter A function to convert a value into a [String] (default: uses `toString()`).
     * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
     * @return The created [ListArgument].
     */
    @JvmOverloads
    fun <T, R> addListArgument(
        type: AbstractStellarArgument<*, R>,
        list: CommandContext<CommandSender>.() -> List<T>,
        parse: CommandSender.(R) -> T,
        converter: CommandSender.(T) -> String? = { it.toString() },
        async: Boolean = false,
    ): ListArgument<T, R> = addArgument(ListArgument(type, list, { converter(it)?.let { Suggestion.withText(it) } }, parse, async))

    /**
     * Adds a [ListArgument] to the command with the given name.
     *
     * @param list The list of possible values.
     * @param parse A function to parse the returned [String] into type `T`.
     * @param converter A function to convert a value into a [Suggestion] (default: uses `toString()`).
     * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
     * @return The created [ListArgument].
     */
    @JvmOverloads
    fun <T> addAdvancedListArgument(
        name: String,
        list: List<T>,
        parse: CommandSender.(String) -> T,
        converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
        async: Boolean = false,
    ): ListArgument<T, String> = addArgument(ListArgument(StringArgument(name, StringType.WORD), list, converter, parse, async))

    /**
     * Adds a [ListArgument] to the command with the given name.
     *
     * @param list A function returning the list of possible values.
     * @param parse A function to parse the returned [String] into type `T`.
     * @param converter A function to convert a value into a [Suggestion] (default: uses `toString()`).
     * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
     * @return The created [ListArgument].
     */
    @JvmOverloads
    fun <T> addAdvancedListArgument(
        name: String,
        list: CommandContext<CommandSender>.() -> List<T>,
        parse: CommandSender.(String) -> T,
        converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
        async: Boolean = false,
    ): ListArgument<T, String> = addArgument(ListArgument(StringArgument(name, StringType.WORD), list, converter, parse, async))

    /**
     * Adds a [ListArgument] to the command wrapped around the given [AbstractStellarCommand].
     *
     * @param type The base argument the list is wrapped around to.
     * @param list The list of possible values.
     * @param parse A function to parse the returned [String] into type `T`.
     * @param converter A function to convert a value into a [Suggestion] (default: uses `toString()`).
     * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
     * @return The created [ListArgument].
     */
    @JvmOverloads
    fun <T, R> addAdvancedListArgument(
        type: AbstractStellarArgument<*, R>,
        list: List<T>,
        parse: CommandSender.(R) -> T,
        converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
        async: Boolean = false,
    ): ListArgument<T, R> = addArgument(ListArgument(type, list, converter, parse, async))

    /**
     * Adds a [ListArgument] to the command wrapped around the given [AbstractStellarCommand].
     *
     * @param type The base argument the list is wrapped around to.
     * @param list A function returning the list of possible values.
     * @param parse A function to parse the returned [String] into type `T`.
     * @param converter A function to convert a value into a [Suggestion] (default: uses `toString()`).
     * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
     * @return The created [ListArgument].
     */
    @JvmOverloads
    fun <T, R> addAdvancedListArgument(
        type: AbstractStellarArgument<*, R>,
        list: CommandContext<CommandSender>.() -> List<T>,
        parse: CommandSender.(R) -> T,
        converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
        async: Boolean = false,
    ): ListArgument<T, R> = addArgument(ListArgument(type, list, converter, parse, async))

    /**
     * Adds an [EnumArgument] to the command with the given name.
     *
     * @param converter A function providing a [CommandSender] and an [Enum] instance from the [enum], returning the [Suggestion] sent to the player.
     * If the [Suggestion] is null, then it will be filtered out (default: uses the `name` property.
     * This is useful when you wish to get the argument input and process the information yourself.
     * @param parse A function providing a [CommandSender] and the argument input, returning the parsed [Enum] (default: `enum.valueOf(input.uppercase())`).
     * @param async Whether the _suggestions_ should be gotten asynchronously (default: `true`).
     * @return The created [EnumArgument].
     */
    @JvmOverloads
    inline fun <reified T : Enum<T>> addEnumArgument(
        name: String,
        noinline converter: CommandSender.(Enum<T>) -> Suggestion? = {
            Suggestion.withText(it.name)
        },
        noinline parse: CommandSender.(String) -> Enum<T>? = { input ->
            try {
                valueOf(Enum::class.java as Class<out Enum<*>>, input.uppercase()) as Enum<T>
            } catch (e: IllegalArgumentException) {
                null
            }
        },
        async: Boolean = true,
    ): EnumArgument<T> = addArgument(EnumArgument(name, T::class, converter, parse, async))

    /**
     * Adds an [EnumArgument] to the command with the given name.
     *
     * @param formatting The formatting style for the enum names (default: [EnumFormatting.LOWERCASE]).
     * @param async Whether the _suggestions_ should be gotten asynchronously (default: `true`).
     * @return The created [EnumArgument].
     */
    inline fun <reified T : Enum<T>> addEnumArgument(
        name: String,
        formatting: EnumFormatting = EnumFormatting.LOWERCASE,
        async: Boolean = true,
    ): EnumArgument<T> = addArgument(EnumArgument(name, T::class, { Suggestion.withText(formatting.action(it.name)) }, async = async))

    /**
     * Adds an [OnlinePlayersArgument] to the command with the given name. It is a list of all currently online players.
     *
     * @param filter A function to filter players (default: exclude sender).
     * @param async Whether the _suggestions_ should be gotten asynchronously (default: `false`).
     * @return The created [OnlinePlayersArgument], which returns a [Player] when parsed.
     */
    @JvmOverloads
    fun addOnlinePlayersArgument(name: String, filter: CommandSender.(Player) -> Boolean = { it != this }, async: Boolean = false): OnlinePlayersArgument = addArgument(OnlinePlayersArgument(name, filter, async))

    // Math
    /**
     * Adds an [AngleArgument] to the command with the given name.
     * @return The created [AngleArgument].
     */
    fun addAngleArgument(name: String): AngleArgument = addArgument(AngleArgument(name))

    /**
     * Adds an [AxisArgument] to the command with the given name.
     * @return The created [AxisArgument].
     */
    fun addAxisArgument(name: String): AxisArgument = addArgument(AxisArgument(name))

    /**
     * Adds a [DoubleRangeArgument] to the command with the given name. Only works in Kotlin.
     * @return The created [DoubleRangeArgument].
     */
    fun addDoubleRangeArgument(name: String): DoubleRangeArgument = addArgument(DoubleRangeArgument(name))

    /**
     * Adds an [IntRangeArgument] to the command with the given name. Only works in Kotlin.
     * @return The created [IntRangeArgument].
     */
    fun addIntRangeArgument(name: String): IntRangeArgument = addArgument(IntRangeArgument(name))

    /**
     * Adds an [OperationArgument] to the command with the given name.
     * @return The created [OperationArgument].
     */
    fun addOperationArgument(name: String): OperationArgument = addArgument(OperationArgument(name))

    /**
     * Adds a [RotationArgument] to the command with the given name.
     * @return The created [RotationArgument].
     */
    fun addRotationArgument(name: String): RotationArgument = addArgument(RotationArgument(name))

    /**
     * Adds a [TimeArgument] to the command with the given name.
     *
     * @param minimum The minimum allowed time value (default: `0`).
     * @return The created [TimeArgument].
     */
    @JvmOverloads
    fun addTimeArgument(name: String, minimum: Int = 0): TimeArgument = addArgument(TimeArgument(name, minimum))

    // Misc
    /**
     * Adds a [NamespacedKeyArgument] to the command with the given name.
     * @return The created [NamespacedKeyArgument].
     */
    fun addNamespacedKeyArgument(name: String): NamespacedKeyArgument = addArgument(NamespacedKeyArgument(name))

    /**
     * Adds a [UUIDArgument] to the command with the given name.
     * @return The created [UUIDArgument].
     */
    fun addUUIDArgument(name: String): UUIDArgument = addArgument(UUIDArgument(name))

    // Phrase
    /**
     * Adds a [PhraseArgument] to the command with the given name.
     * @return The created [PhraseArgument].
     */
    fun addPhraseArgument(name: String): PhraseArgument = addArgument(PhraseArgument(name))

    // Player
    /**
     * Adds a [GameModeArgument] to the command with the given name.
     * @return The created [GameModeArgument].
     */
    fun addGameModeArgument(name: String): GameModeArgument = addArgument(GameModeArgument(name))

    /**
     * Adds a [GameProfileArgument] to the command with the given name.
     * @return The created [GameProfileArgument].
     */
    fun addGameProfileArgument(name: String): GameProfileArgument = addArgument(GameProfileArgument(name))

    // Scoreboard
    /**
     * Adds a [DisplaySlotArgument] to the command with the given name.
     * @return The created [DisplaySlotArgument].
     */
    fun addDisplaySlotArgument(name: String): DisplaySlotArgument = addArgument(DisplaySlotArgument(name))

    /**
     * Adds an [ObjectiveArgument] to the command with the given name.
     * @return The created [ObjectiveArgument].
     */
    fun addObjectiveArgument(name: String): ObjectiveArgument = addArgument(ObjectiveArgument(name))

    /**
     * Adds an [ObjectiveCriteriaArgument] to the command with the given name.
     * @return The created [ObjectiveCriteriaArgument].
     */
    fun addObjectiveCriteriaArgument(name: String): ObjectiveCriteriaArgument = addArgument(ObjectiveCriteriaArgument(name))

    /**
     * Adds a [ScoreHolderArgument] to the command with the given name.
     *
     * @param type The type of score holder.
     * @return The created [ScoreHolderArgument].
     */
    fun addScoreHolderArgument(name: String, type: ScoreHolderType): ScoreHolderArgument = addArgument(ScoreHolderArgument(name, type))

    /**
     * Adds a [TeamArgument] to the command with the given name.
     * @return The created [TeamArgument].
     */
    fun addTeamArgument(name: String): TeamArgument = addArgument(TeamArgument(name))

    // Structure
    /**
     * Adds a [LootTableArgument] to the command with the given name.
     * @return The created [LootTableArgument].
     */
    fun addLootTableArgument(name: String): LootTableArgument = addArgument(LootTableArgument(name))

    /**
     * Adds a [MirrorArgument] to the command with the given name.
     * @return The created [MirrorArgument].
     */
    fun addMirrorArgument(name: String): MirrorArgument = addArgument(MirrorArgument(name))

    /**
     * Adds a [StructureRotationArgument] to the command with the given name.
     * @return The created [StructureRotationArgument].
     */
    fun addStructureRotationArgument(name: String): StructureRotationArgument = addArgument(StructureRotationArgument(name))

    // Text
    /**
     * Adds a [ColorArgument] to the command with the given name.
     * @return The created [ColorArgument].
     */
    fun addColorArgument(name: String): ColorArgument = addArgument(ColorArgument(name))

    /**
     * Adds a [ComponentArgument] to the command with the given name.
     * @return The created [ComponentArgument].
     */
    fun addComponentArgument(name: String): ComponentArgument = addArgument(ComponentArgument(name))

    /**
     * Adds a [MessageArgument] to the command with the given name.
     * @return The created [MessageArgument].
     */
    fun addMessageArgument(name: String): MessageArgument = addArgument(MessageArgument(name))

    /**
     * Adds a [StyleArgument] to the command with the given name.
     * @return The created [StyleArgument].
     */
    fun addStyleArgument(name: String): StyleArgument = addArgument(StyleArgument(name))

    // World
    /**
     * Adds an [EnvironmentArgument] to the command with the given name.
     * @return The created [EnvironmentArgument].
     */
    fun addEnvironmentArgument(name: String): EnvironmentArgument = addArgument(EnvironmentArgument(name))

    /**
     * Adds a [HeightMapArgument] to the command with the given name.
     * @return The created [HeightMapArgument].
     */
    fun addHeightMapArgument(name: String): HeightMapArgument = addArgument(HeightMapArgument(name))

    /**
     * Adds a [LocationArgument] to the command with the given name.
     *
     * @param type The type of location.
     * @return The created [LocationArgument].
     */
    fun addLocationArgument(name: String, type: LocationType): LocationArgument = addArgument(LocationArgument(name, type))

    /**
     * Adds or sets a piece of information with the given name and text.
     * It will be stored as information, to be displayed as a help topic.
     * @return The modified command object.
     */
    abstract fun setInformation(name: String, text: String): T

    /**
     * Sets the description of this command with the given text.
     * It will be stored as information, to be displayed as a help topic.
     * @return The modified command object.
     */
    abstract fun setDescription(text: String): T

    /**
     * Sets the usage text of this command with the given text.
     * It will be stored as information, to be displayed as a help topic.
     * @return The modified command object.
     */
    abstract fun setUsageText(text: String): T

    /**
     * Clears all the information.
     * @return The modified command object.
     */
    abstract fun clearInformation(): T

    /**
     * Registers the command with the given plugin.
     *
     * @param plugin The given `JavaPlugin` instance.
     * @return The registered command object.
     */
    abstract fun register(plugin: JavaPlugin): T

}