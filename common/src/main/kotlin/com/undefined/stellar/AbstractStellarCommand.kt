package com.undefined.stellar

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
import com.undefined.stellar.data.execution.StellarExecution
import com.undefined.stellar.data.execution.StellarRunnable
import com.undefined.stellar.data.failure.HideDefaultFailureMessages
import com.undefined.stellar.data.requirement.StellarRequirement
import com.undefined.stellar.data.suggestion.Suggestion
import com.undefined.stellar.nms.NMS
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * This is the base of any command, whether it's an argument or a root command.
 *
 * @param name The command name.
 */
@Suppress("UNCHECKED_CAST")
abstract class AbstractStellarCommand<T : AbstractStellarCommand<T>>(val name: String) {

    lateinit var nms: NMS

    val aliases: MutableSet<String> = mutableSetOf()
    val requirements: MutableList<StellarRequirement<*>> = mutableListOf()
    val arguments: MutableSet<AbstractStellarArgument<*, *>> = mutableSetOf()
    val executions: MutableSet<StellarExecution<*>> = mutableSetOf()
    val runnables: MutableSet<StellarRunnable<*>> = mutableSetOf()
    val failureExecutions: MutableSet<StellarExecution<*>> = mutableSetOf()
    open val globalFailureExecutions: MutableSet<StellarExecution<*>> = mutableSetOf()
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
     * Adds a requirement that must be met for the command to be available to the player.
     *
     * @param C The type of CommandSender.
     * @param requirement The condition that must be met.
     * @return The modified command object.
     */
    inline fun <reified C : CommandSender> addRequirement(noinline requirement: C.() -> Boolean): T = apply {
        requirements.add(StellarRequirement(C::class, requirement))
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
    fun addRequirement(level: Int): T = addRequirement<Player> { nms.hasPermission(this, level) }

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
    fun addRequirements(vararg levels: Int): T = addRequirement<Player> { levels.all { nms.hasPermission(this, it) } }

    /**
     * Adds an executor to the command.
     *
     * @param C The type of CommandSender.
     * @param execution The execution block.
     * @return The modified command object.
     */
    inline fun <reified C : CommandSender> addExecution(noinline execution: CommandContext<C>.() -> Unit): T = apply {
        executions.add(StellarExecution(C::class, execution, false))
    } as T

    /**
     * Adds an async executor to the command.
     *
     * @param C The type of CommandSender.
     * @param execution The execution block.
     * @return The modified command object.
     */
    inline fun <reified C : CommandSender> addAsyncExecution(noinline execution: CommandContext<C>.() -> Unit): T = apply {
        executions.add(StellarExecution(C::class, execution, true))
    } as T

    /**
     * Adds a runnable to the command.
     *
     * @param C The type of CommandSender.
     * @param runnable The execution block.
     * @return The modified command object.
     */
    inline fun <reified C : CommandSender> addRunnable(noinline runnable: CommandContext<C>.() -> Boolean): T = apply {
        runnables.add(StellarRunnable(C::class, runnable, false))
    } as T

    /**
     * Adds an runnable to the command.
     *
     * @param C The type of CommandSender.
     * @param runnable The execution block.
     * @return The modified command object.
     */
    inline fun <reified C : CommandSender> addAsyncRunnable(noinline runnable: CommandContext<C>.() -> Boolean): T = apply {
        runnables.add(StellarRunnable(C::class, runnable, true))
    } as T

    /**
     * Adds a failure execution to the command to be displayed when the command fails.
     *
     * @param C The type of CommandSender.
     * @param execution The execution block.
     * @return The modified command object.
     */
    inline fun <reified C : CommandSender> addFailureExecution(noinline execution: CommandContext<C>.() -> Unit): T = apply {
        failureExecutions.add(StellarExecution(C::class, execution, false))
    } as T

    /**
     * Adds a failure execution to the _root_ command to be displayed when the command fails.
     *
     * @param C The type of CommandSender.
     * @param execution The execution block.
     * @return The modified command object.
     */
    inline fun <reified C : CommandSender> addGlobalFailureExecution(noinline execution: CommandContext<C>.() -> Unit): T = apply {
        globalFailureExecutions.add(StellarExecution(C::class, execution, false))
    } as T

    /**
     * Adds a failure message to the command to be displayed when the command fails.
     *
     * @param message The message component to be sent.
     * @return The modified command object.
     */
    fun addFailureMessage(message: Component): T = apply {
        failureExecutions.add(StellarExecution(CommandSender::class, { sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(message)) }, false))
    } as T

    /**
     * Adds a failure message to the _root_ command to be displayed when the command fails.
     *
     * @param message The message component to be sent.
     * @return The modified command object.
     */
    fun addGlobalFailureMessage(message: Component): T = apply {
        globalFailureExecutions.add(StellarExecution(CommandSender::class, { sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(message)) }, false))
    } as T

    /**
     * Adds a failure message to the command to be displayed when the command fails.
     *
     * @param message The message to be sent.
     * @return The modified command object.
     */
    fun addFailureMessage(message: String): T = apply {
        val component = MiniMessage.miniMessage().deserialize(message)
        failureExecutions.add(StellarExecution(CommandSender::class, { sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(component)) }, false))
    } as T

    /**
     * Adds a failure message to the _root_ command to be displayed when the command fails.
     *
     * @param message The message to be sent.
     * @return The modified command object.
     */
    fun addGlobalFailureMessage(message: String): T = apply {
        val component = MiniMessage.miniMessage().deserialize(message)
        globalFailureExecutions.add(StellarExecution(CommandSender::class, { sender.sendMessage(LegacyComponentSerializer.legacyAmpersand().serialize(component)) }, false))
    } as T

    /**
     * Hides the default Minecraft failure messages for the command.
     *
     * @param hide Whether to hide the messages.
     * @param global Whether to apply it on the root command.
     * @return The modified command object.
     */
    fun hideDefaultFailureMessages(hide: Boolean = true, global: Boolean = false): T = apply {
        hideDefaultFailureMessages = HideDefaultFailureMessages(hide, global)
    } as T

//    @ApiStatus.Internal TODO import
    open fun hasGlobalHiddenDefaultFailureMessages(): Boolean = hideDefaultFailureMessages.hide && hideDefaultFailureMessages.global

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
     * Registeres the command with the given plugin.
     *
     * @param plugin The given `JavaPlugin` instance.
     * @return The registered command object.
     */
    abstract fun register(plugin: JavaPlugin): T

    // Arguments
    fun <T : AbstractStellarArgument<*, *>> addArgument(argument: T): T = argument.apply {
        argument.parent = this@AbstractStellarCommand
        this@AbstractStellarCommand.arguments.add(argument)
    }

    // Basic
    fun addBooleanArgument(name: String): BooleanArgument = addArgument(BooleanArgument(name))
    fun addDoubleArgument(name: String, minimum: Double = Double.MIN_VALUE, maximum: Double = Double.MAX_VALUE): DoubleArgument = addArgument(DoubleArgument(name, minimum, maximum))
    fun addFloatArgument(name: String, minimum: Float = Float.MIN_VALUE, maximum: Float = Float.MAX_VALUE): FloatArgument = addArgument(FloatArgument(name, minimum, maximum))
    fun addIntegerArgument(name: String, minimum: Int = Int.MIN_VALUE, maximum: Int = Int.MAX_VALUE): IntegerArgument = addArgument(IntegerArgument(name, minimum, maximum))
    fun addLongArgument(name: String, minimum: Long = Long.MIN_VALUE, maximum: Long = Long.MAX_VALUE): LongArgument = addArgument(LongArgument(name, minimum, maximum))
    fun addStringArgument(name: String, type: StringType = StringType.WORD): StringArgument = addArgument(StringArgument(name, type))

    // Block
    fun addBlockDataArgument(name: String): BlockDataArgument = addArgument(BlockDataArgument(name))
    fun addBlockPredicateArgument(name: String): BlockPredicateArgument = addArgument(BlockPredicateArgument(name))

    // Entity
    fun addEntityAnchorArgument(name: String): EntityAnchorArgument = addArgument(EntityAnchorArgument(name))
    fun addEntityArgument(name: String, type: EntityDisplayType): EntityArgument = addArgument(EntityArgument(name, type))

    // Item
    fun addItemSlotArgument(name: String, multiple: Boolean = false): ItemSlotArgument = addArgument(ItemSlotArgument(name, multiple))
    fun addItemStackArgument(name: String): ItemStackArgument = addArgument(ItemStackArgument(name))
    fun addItemStackPredicateArgument(name: String): ItemStackPredicateArgument = addArgument(ItemStackPredicateArgument(name))

    // List
    inline fun <reified T : Enum<T>> addEnumArgument(
        name: String,
        noinline converter: CommandSender.(Enum<T>) -> Suggestion? = {
            Suggestion.withText(it.name)
        },
        async: Boolean = true,
    ): EnumArgument<T> = addArgument(EnumArgument(name, T::class, converter, async))

    inline fun <reified T : Enum<T>> addEnumArgument(
        name: String,
        formatting: EnumFormatting = EnumFormatting.LOWERCASE,
        async: Boolean = true,
    ): EnumArgument<T> = addArgument(EnumArgument(name, T::class, { Suggestion.withText(formatting.action(it.name)) }, async))

    fun <T> addAdvancedListArgument(
        name: String,
        list: List<T>,
        converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
        parse: CommandSender.(String) -> T,
        async: Boolean = false,
    ): ListArgument<T, String> = addArgument(ListArgument(StringArgument(name, StringType.WORD), list, converter, parse, async))

    fun <T, R> addAdvancedListArgument(
        type: AbstractStellarArgument<*, R>,
        list: List<T>,
        converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
        parse: CommandSender.(R) -> T,
        async: Boolean = false,
    ): ListArgument<T, R> = addArgument(ListArgument(type, list, converter, parse, async))

    fun <T> addAdvancedListArgument(
        name: String,
        list: CommandContext<CommandSender>.() -> List<T>,
        converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
        parse: CommandSender.(String) -> T,
        async: Boolean = false,
    ): ListArgument<T, String> = addArgument(ListArgument(StringArgument(name, StringType.WORD), list, converter, parse, async))

    fun <T, R> addAdvancedListArgument(
        type: AbstractStellarArgument<*, R>,
        list: CommandContext<CommandSender>.() -> List<T>,
        converter: CommandSender.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
        parse: CommandSender.(R) -> T,
        async: Boolean = false,
    ): ListArgument<T, R> = addArgument(ListArgument(type, list, converter, parse, async))

    fun <T> addListArgument(
        name: String,
        list: List<T>,
        converter: CommandSender.(T) -> String? = { it.toString() },
        parse: CommandSender.(String) -> T,
        async: Boolean = false,
    ): ListArgument<T, String> = addArgument(ListArgument(StringArgument(name, StringType.WORD), list, { converter(it)?.let { Suggestion.withText(it) } }, parse, async))

    fun <T, R> addListArgument(
        type: AbstractStellarArgument<*, R>,
        list: List<T>,
        converter: CommandSender.(T) -> String? = { it.toString() },
        parse: CommandSender.(R) -> T,
        async: Boolean = false,
    ): ListArgument<T, R> = addArgument(ListArgument(type, list, { converter(it)?.let { Suggestion.withText(it) } }, parse, async))

    fun <T> addListArgument(
        name: String,
        list: CommandContext<CommandSender>.() -> List<T>,
        converter: CommandSender.(T) -> String? = { it.toString() },
        parse: CommandSender.(String) -> T,
        async: Boolean = false,
    ): ListArgument<T, String> = addArgument(ListArgument(StringArgument(name, StringType.WORD), list, { converter(it)?.let { Suggestion.withText(it) } }, parse, async))

    fun <T, R> addListArgument(
        type: AbstractStellarArgument<*, R>,
        list: CommandContext<CommandSender>.() -> List<T>,
        converter: CommandSender.(T) -> String? = { it.toString() },
        parse: CommandSender.(R) -> T,
        async: Boolean = false,
    ): ListArgument<T, R> = addArgument(ListArgument(type, list, { converter(it)?.let { Suggestion.withText(it) } }, parse, async))

    fun addOnlinePlayersArgument(name: String, filter: (Player) -> Boolean = { true }, async: Boolean = false): OnlinePlayersArgument = addArgument(OnlinePlayersArgument(name, filter, async))

    // Math
    fun addAngleArgument(name: String): AngleArgument = addArgument(AngleArgument(name))
    fun addAxisArgument(name: String): AxisArgument = addArgument(AxisArgument(name))
    fun addDoubleRangeArgument(name: String): DoubleRangeArgument = addArgument(DoubleRangeArgument(name))
    fun addIntRangeArgument(name: String): IntRangeArgument = addArgument(IntRangeArgument(name))
    fun addOperationArgument(name: String): OperationArgument = addArgument(OperationArgument(name))
    fun addRotationArgument(name: String): RotationArgument = addArgument(RotationArgument(name))
    fun addTimeArgument(name: String, minimum: Int = 0): TimeArgument = addArgument(TimeArgument(name, minimum))

    // Misc
    fun addNamespacedKeyArgument(name: String): NamespacedKeyArgument = addArgument(NamespacedKeyArgument(name))
    fun addUUIDArgument(name: String): UUIDArgument = addArgument(UUIDArgument(name))

    // Phrase
    fun addPhraseArgument(name: String): PhraseArgument = addArgument(PhraseArgument(name))

    // Player
    fun addGameModeArgument(name: String): GameModeArgument = addArgument(GameModeArgument(name))
    fun addGameProfileArgument(name: String): GameProfileArgument = addArgument(GameProfileArgument(name))

    // Scoreboard
    fun addDisplaySlotArgument(name: String): DisplaySlotArgument = addArgument(DisplaySlotArgument(name))
    fun addObjectiveArgument(name: String): ObjectiveArgument = addArgument(ObjectiveArgument(name))
    fun addObjectiveCriteriaArgument(name: String): ObjectiveCriteriaArgument = addArgument(ObjectiveCriteriaArgument(name))
    fun addScoreHolderArgument(name: String, type: ScoreHolderType): ScoreHolderArgument = addArgument(ScoreHolderArgument(name, type))
    fun addTeamArgument(name: String): TeamArgument = addArgument(TeamArgument(name))

    // Structure
    fun addLootTableArgument(name: String): LootTableArgument = addArgument(LootTableArgument(name))
    fun addMirrorArgument(name: String): MirrorArgument = addArgument(MirrorArgument(name))
    fun addStructureRotationArgument(name: String): StructureRotationArgument = addArgument(StructureRotationArgument(name))

    // Text
    fun addColorArgument(name: String): ColorArgument = addArgument(ColorArgument(name))
    fun addComponentArgument(name: String): ComponentArgument = addArgument(ComponentArgument(name))
    fun addMessageArgument(name: String): MessageArgument = addArgument(MessageArgument(name))
    fun addStyleArgument(name: String): StyleArgument = addArgument(StyleArgument(name))

    // World
    fun addEnvironmentArgument(name: String): EnvironmentArgument = addArgument(EnvironmentArgument(name))
    fun addHeightMapArgument(name: String): HeightMapArgument = addArgument(HeightMapArgument(name))
    fun addLocationArgument(name: String, type: LocationType): LocationArgument = addArgument(LocationArgument(name, type))
    fun addParticleArgument(name: String): ParticleArgument = addArgument(ParticleArgument(name))

}