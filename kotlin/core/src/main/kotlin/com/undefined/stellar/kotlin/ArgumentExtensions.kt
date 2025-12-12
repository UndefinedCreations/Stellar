package com.undefined.stellar.kotlin

import com.undefined.stellar.StellarCommand
import com.undefined.stellar.ParameterArgument
import com.undefined.stellar.StellarConfig
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
import com.undefined.stellar.argument.text.*
import com.undefined.stellar.argument.world.EnvironmentArgument
import com.undefined.stellar.argument.world.HeightMapArgument
import com.undefined.stellar.argument.world.LocationArgument
import com.undefined.stellar.argument.world.LocationType
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.argument.EnumFormatting
import com.undefined.stellar.data.suggestion.Suggestion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.lang.Enum.valueOf

/**
 * Adds a [LiteralArgument] to the command with the given name and aliases.
 * @return The created [LiteralArgument].
 */
fun StellarCommand<*>.argument(name: String, vararg aliases: String, block: LiteralArgument.() -> Unit = {}): LiteralArgument = addArgument(LiteralArgument(name).apply { addAliases(*aliases) }).apply(block)

/**
 * Adds a [LiteralArgument] to the command with the given name and aliases.
 * @return The created [LiteralArgument].
 */
fun StellarCommand<*>.literalArgument(name: String, vararg aliases: String, block: LiteralArgument.() -> Unit = {}): LiteralArgument = addArgument(LiteralArgument(name).apply { addAliases(*aliases) }).apply(block)

// Basic
/**
 * Adds a [BooleanArgument] to the command with the given name.
 * @return The created [BooleanArgument].
 */
fun StellarCommand<*>.booleanArgument(name: String, block: BooleanArgument.() -> Unit = {}): BooleanArgument = addArgument(BooleanArgument(name)).apply(block)

/**
 * Adds a [DoubleArgument] to the command with the given name.
 *
 * @param minimum The minimum allowed value (default: [Double.Companion.MIN_VALUE]).
 * @param maximum The maximum allowed value (default: [Double.Companion.MAX_VALUE]).
 * @return The created [DoubleArgument].
 */
fun StellarCommand<*>.doubleArgument(name: String, minimum: Double = Double.MIN_VALUE, maximum: Double = Double.MAX_VALUE, block: DoubleArgument.() -> Unit = {}): DoubleArgument = addArgument(DoubleArgument(name, minimum, maximum)).apply(block)

/**
 * Adds a [FloatArgument] to the command with the given name.
 *
 * @param minimum The minimum allowed value (default: [Float.Companion.MIN_VALUE]).
 * @param maximum The maximum allowed value (default: [Float.Companion.MAX_VALUE]).
 * @return The created [FloatArgument].
 */
fun StellarCommand<*>.floatArgument(name: String, minimum: Float = Float.MIN_VALUE, maximum: Float = Float.MAX_VALUE, block: FloatArgument.() -> Unit = {}): FloatArgument = addArgument(FloatArgument(name, minimum, maximum)).apply(block)

/**
 * Adds an [IntegerArgument] to the command with the given name.
 *
 * @param minimum The minimum allowed value (default: [Int.Companion.MIN_VALUE]).
 * @param maximum The maximum allowed value (default: [Int.Companion.MAX_VALUE]).
 * @return The created [IntegerArgument].
 */
fun StellarCommand<*>.integerArgument(name: String, minimum: Int = Int.MIN_VALUE, maximum: Int = Int.MAX_VALUE, block: IntegerArgument.() -> Unit = {}): IntegerArgument = addArgument(IntegerArgument(name, minimum, maximum)).apply(block)

/**
 * Adds a [LongArgument] to the command with the given name.
 *
 * @param minimum The minimum allowed value (default: [Long.Companion.MIN_VALUE]).
 * @param maximum The maximum allowed value (default: [Long.Companion.MAX_VALUE]).
 * @return The created [LongArgument].
 */
fun StellarCommand<*>.longArgument(name: String, minimum: Long = Long.MIN_VALUE, maximum: Long = Long.MAX_VALUE, block: LongArgument.() -> Unit = {}): LongArgument = addArgument(LongArgument(name, minimum, maximum)).apply(block)

/**
 * Adds a [StringArgument] to the command with the given name.
 * @return The created [StringArgument].
 */
fun StellarCommand<*>.stringArgument(name: String, type: StringType = StringType.WORD, block: StringArgument.() -> Unit = {}): StringArgument = addArgument(StringArgument(name, type)).apply(block)

// Block
/**
 * Adds a [BlockDataArgument] to the command with the given name.
 * @return The created [BlockDataArgument].
 */
fun StellarCommand<*>.blockDataArgument(name: String, block: BlockDataArgument.() -> Unit = {}): BlockDataArgument = addArgument(BlockDataArgument(name)).apply(block)

/**
 * Adds a [BlockPredicateArgument] to the command with the given name.
 * @return The created [BlockPredicateArgument].
 */
fun StellarCommand<*>.blockPredicateArgument(name: String, block: BlockPredicateArgument.() -> Unit = {}): BlockPredicateArgument = addArgument(BlockPredicateArgument(name)).apply(block)

// Entity
/**
 * Adds an [EntityAnchorArgument] to the command with the given name.
 * @return The created [EntityAnchorArgument].
 */
fun StellarCommand<*>.entityAnchorArgument(name: String, block: EntityAnchorArgument.() -> Unit = {}): EntityAnchorArgument = addArgument(EntityAnchorArgument(name)).apply(block)

/**
 * Adds an [EntityArgument] to the command with the given name.
 * @return The created [EntityArgument].
 */
fun StellarCommand<*>.entityArgument(name: String, type: EntityDisplayType, block: EntityArgument.() -> Unit = {}): EntityArgument = addArgument(EntityArgument(name, type)).apply(block)

// Item
/**
 * Adds an [ItemSlotArgument] to the command with the given name.
 * @return The created [ItemSlotArgument].
 */
fun StellarCommand<*>.itemSlotArgument(name: String, multiple: Boolean = false, block: ItemSlotArgument.() -> Unit = {}): ItemSlotArgument = addArgument(ItemSlotArgument(name, multiple)).apply(block)

/**
 * Adds an [ItemStackArgument] to the command with the given name.
 * @return The created [ItemStackArgument].
 */
fun StellarCommand<*>.itemStackArgument(name: String, block: ItemStackArgument.() -> Unit = {}): ItemStackArgument = addArgument(ItemStackArgument(name)).apply(block)

/**
 * Adds an [ItemStackPredicateArgument] to the command with the given name.
 * @return The created [ItemStackPredicateArgument].
 */
fun StellarCommand<*>.itemPredicateArgument(name: String, block: ItemStackPredicateArgument.() -> Unit = {}): ItemStackPredicateArgument = addArgument(ItemStackPredicateArgument(name)).apply(block)

// List
/**
 * Adds a [ListArgument] to the command with the given name. It uses its [StringArgument] as a base wrapper.
 *
 * @param list A function returning a list of possible values.
 * @param tooltip A function that assigns each suggestion with a tooltip. If the value is null, it will not add a tooltip.
 * @param type The [StringType] it will use in the [StringArgument].
 * @param scope The [CoroutineScope] used to compute the list.
 * @return The created [ListArgument].
 */
fun StellarCommand<*>.listArgument(
    name: String,
    list: suspend CommandContext<CommandSender>.() -> List<String>,
    tooltip: (String) -> String? = { null },
    type: StringType = StringType.WORD,
    scope: CoroutineScope = StellarConfig.scope,
    block: ListArgument<String, String>.() -> Unit = {},
): ListArgument<String, String> = addArgument(ListArgument(StringArgument(name, type), {
    scope.future {
        list(this@ListArgument)
    }
}, { Suggestion.create(it, tooltip(it)) }, { it }) as ListArgument<String, String>).apply(block)

/**
 * Adds a [ListArgument] to the command with the given name. It uses its [StringArgument] as a base wrapper.
 *
 * @param list The list of possible values.
 * @param tooltip A function that assigns each suggestion with a tooltip. If the value is null, it will not add a tooltip.
 * @param type The [StringType] it will use in the [StringArgument].
 * @return The created [ListArgument].
 */
fun StellarCommand<*>.listArgument(
    name: String,
    list: List<String>,
    tooltip: (String) -> String? = { null },
    type: StringType = StringType.WORD,
    block: ListArgument<String, String>.() -> Unit = {},
): ListArgument<String, String> = addArgument(ListArgument(StringArgument(name, type), list, { Suggestion.create(it, tooltip(it)) }, { it }) as ListArgument<String, String>).apply(block)

/**
 * Adds a [ListArgument] to the command with the given name.
 *
 * @param list The list of possible values.
 * @param parse A function to parse the returned [String] into type `T`.
 * @param converter A function to convert a value into a [String] (default: uses `toString()`).
 * @param scope The [CoroutineScope] used to compute the list.
 * @return The created [ListArgument].
 */
fun <T> StellarCommand<*>.listArgument(
    name: String,
    list: List<T>,
    parse: CommandSender.(String) -> T,
    converter: suspend CommandContext<CommandSender>.(T) -> String? = { it.toString() },
    scope: CoroutineScope = StellarConfig.scope,
    block: ListArgument<T, String>.() -> Unit = {},
): ListArgument<T, String> = addArgument(ListArgument(StringArgument(name, StringType.WORD), {
    scope.future {
        list.mapNotNull { converter(it)?.let { Suggestion.withText(it) } }
    }
}, parse) as ListArgument<T, String>).apply(block)

/**
 * Adds a [ListArgument] to the command with the given name.
 *
 * @param list A function returning the list of possible values.
 * @param parse A function to parse the returned [String] into type `T`.
 * @param converter A function to convert a value into a [String] (default: uses `toString()`).
 * @param scope The [CoroutineScope] used to compute the list.
 * @return The created [ListArgument].
 */
fun <T> StellarCommand<*>.listArgument(
    name: String,
    list: suspend CommandContext<CommandSender>.() -> Iterable<T>,
    parse: CommandSender.(String) -> T,
    converter: suspend CommandContext<CommandSender>.(T) -> String? = { it.toString() },
    scope: CoroutineScope = StellarConfig.scope,
    block: ListArgument<T, String>.() -> Unit = {},
): ListArgument<T, String> = addArgument(ListArgument(StringArgument(name, StringType.WORD), {
    scope.future {
        list().mapNotNull {
            val convertedText = converter(it).takeIf { suggestion -> suggestion?.isNotBlank() == true }
            convertedText?.let { Suggestion.withText(convertedText) }
        }.filter { it.text.startsWith(input, true) && it.text != input }
    }
}, parse) as ListArgument<T, String>).apply(block)

/**
 * Adds a [ListArgument] to the command wrapped around the given [StellarCommand].
 *
 * @param type The base argument the list is wrapped around to.
 * @param list The list of possible values.
 * @param parse A function to parse the returned [String] into type `T`.
 * @param converter A function to convert a value into a [String] (default: uses `toString()`).
 * @param scope The [CoroutineScope] used to compute the list.
 * @return The created [ListArgument].
 */
fun <T, R> StellarCommand<*>.listArgument(
    type: ParameterArgument<*, R>,
    list: List<T>,
    parse: CommandSender.(R) -> T,
    converter: suspend CommandContext<CommandSender>.(T) -> String? = { it.toString() },
    scope: CoroutineScope = StellarConfig.scope,
    block: ListArgument<T, R>.() -> Unit = {},
): ListArgument<T, R> = addArgument(ListArgument(type, {
    scope.future {
        list.mapNotNull {
            val convertedText = converter(it).takeIf { suggestion -> suggestion?.isNotBlank() == true }
            convertedText?.let { Suggestion.withText(convertedText) }
        }.filter { it.text.startsWith(input, true) && it.text != input }
    }
}, parse) as ListArgument<T, R>).apply(block)

/**
 * Adds a [ListArgument] to the command wrapped around the given [StellarCommand].
 *
 * @param type The base argument the list is wrapped around to.
 * @param list A function returning the list of possible values.
 * @param parse A function to parse the returned [String] into type `T`.
 * @param converter A function to convert a value into a [String] (default: uses `toString()`).
 * @param scope The [CoroutineScope] used to compute the list.
 * @return The created [ListArgument].
 */
fun <T, R> StellarCommand<*>.listArgument(
    type: ParameterArgument<*, R>,
    list: suspend CommandContext<CommandSender>.() -> List<T>,
    parse: CommandSender.(R) -> T,
    converter: suspend CommandContext<CommandSender>.(T) -> String? = { it.toString() },
    scope: CoroutineScope = StellarConfig.scope,
    block: ListArgument<T, R>.() -> Unit = {},
): ListArgument<T, R> = addArgument(ListArgument(type, {
    scope.future {
        list().mapNotNull {
            val convertedText = converter(it).takeIf { suggestion -> suggestion?.isNotBlank() == true }
            convertedText?.let { Suggestion.withText(convertedText) }
        }.filter { it.text.startsWith(input, true) && it.text != input }
    }
}, parse) as ListArgument<T, R>).apply(block)

/**
 * Adds a [ListArgument] to the command with the given name.
 *
 * @param list The list of possible values.
 * @param parse A function to parse the returned [String] into type `T`.
 * @param converter A function to convert a value into a [Suggestion] (default: uses `toString()`).
 * @param scope The [CoroutineScope] used to compute the list.
 * @return The created [ListArgument].
 */
fun <T> StellarCommand<*>.advancedListArgument(
    name: String,
    list: Collection<T>,
    parse: CommandSender.(String) -> T,
    converter: suspend CommandContext<CommandSender>.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
    scope: CoroutineScope = StellarConfig.scope,
    block: ListArgument<T, String>.() -> Unit = {},
): ListArgument<T, String> = addArgument(ListArgument(StringArgument(name, StringType.WORD), {
    scope.future {
        list.mapNotNull {
            converter(it).takeIf { suggestion -> suggestion?.text?.isNotBlank() == true }
        }.filter { it.text.startsWith(input, true) && it.text != input }
    }
}, parse) as ListArgument<T, String>).apply(block)

/**
 * Adds a [ListArgument] to the command with the given name.
 *
 * @param list A function returning the list of possible values.
 * @param parse A function to parse the returned [String] into type `T`.
 * @param converter A function to convert a value into a [Suggestion] (default: uses `toString()`).
 * @param scope The [CoroutineScope] used to compute the list.
 * @return The created [ListArgument].
 */
fun <T> StellarCommand<*>.advancedListArgument(
    name: String,
    list: suspend CommandContext<CommandSender>.() -> List<T>,
    parse: CommandSender.(String) -> T,
    converter: suspend CommandContext<CommandSender>.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
    scope: CoroutineScope = StellarConfig.scope,
    block: ListArgument<T, String>.() -> Unit = {},
): ListArgument<T, String> = addArgument(ListArgument(StringArgument(name, StringType.WORD), {
    scope.future {
        list().mapNotNull {
            converter(it).takeIf { suggestion -> suggestion?.text?.isNotBlank() == true }
        }.filter { it.text.startsWith(input, true) && it.text != input }
    }
}, parse) as ListArgument<T, String>).apply(block)

/**
 * Adds a [ListArgument] to the command wrapped around the given [StellarCommand].
 *
 * @param type The base argument the list is wrapped around to.
 * @param list The list of possible values.
 * @param parse A function to parse the returned [String] into type `T`.
 * @param converter A function to convert a value into a [Suggestion] (default: uses `toString()`).
 * @param scope The [CoroutineScope] used to compute the list.
 * @return The created [ListArgument].
 */
fun <T, R> StellarCommand<*>.advancedListArgument(
    type: ParameterArgument<*, R>,
    list: List<T>,
    parse: CommandSender.(R) -> T,
    converter: suspend CommandContext<CommandSender>.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
    scope: CoroutineScope = StellarConfig.scope,
    block: ListArgument<T, R>.() -> Unit = {},
): ListArgument<T, R> = addArgument(ListArgument(type, {
    scope.future {
        list.mapNotNull {
            converter(it).takeIf { suggestion -> suggestion?.text?.isNotBlank() == true }
        }.filter { it.text.startsWith(input, true) && it.text != input }
    }
}, parse) as ListArgument<T, R>).apply(block).apply(block)

/**
 * Adds a [ListArgument] to the command wrapped around the given [StellarCommand].
 *
 * @param type The base argument the list is wrapped around to.
 * @param list A function returning the list of possible values.
 * @param parse A function to parse the returned [String] into type `T`.
 * @param converter A function to convert a value into a [Suggestion] (default: uses `toString()`).
 * @param scope The [CoroutineScope] used to compute the list.
 * @return The created [ListArgument].
 */
fun <T, R> StellarCommand<*>.advancedListArgument(
    type: ParameterArgument<*, R>,
    list: suspend CommandContext<CommandSender>.() -> List<T>,
    parse: CommandSender.(R) -> T,
    converter: suspend CommandContext<CommandSender>.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
    scope: CoroutineScope = StellarConfig.scope,
    block: ListArgument<T, R>.() -> Unit = {},
): ListArgument<T, R> = addArgument(ListArgument(type, {
    scope.future {
        list().mapNotNull {
            converter(it).takeIf { suggestion -> suggestion?.text?.isNotBlank() == true }
        }.filter { it.text.startsWith(input, true) && it.text != input }
    }
}, parse) as ListArgument<T, R>).apply(block).apply(block)

/**
 * Adds a [ListArgument] to the command with the given name with the values of [T].
 *
 * @param converter A function providing a [CommandSender] and an [Enum] instance from the [T], returning the [Suggestion] sent to the player.
 * If the [Suggestion] is null, then it will be filtered out (default: uses the `name` property).
 * This is useful when you wish to get the argument input and process the information yourself.
 * @param parse A function providing a [CommandSender] and the argument input, returning the parsed [Enum] (default: `enum.valueOf(input.uppercase())`).
 * @param scope The [CoroutineScope] used to compute the list.
 * @return The created [ListArgument].
 */
inline fun <reified T : Enum<T>> StellarCommand<*>.enumArgument(
    name: String,
    noinline converter: suspend CommandContext<CommandSender>.(Enum<T>) -> String? = { it.name },
    noinline parse: CommandSender.(String) -> Enum<T> = { input ->
        valueOf(Enum::class.java as Class<out Enum<*>>, input.uppercase()) as Enum<T>
    },
    scope: CoroutineScope = StellarConfig.scope,
    noinline block: ListArgument<Enum<T>, String>.() -> Unit = {},
): ListArgument<Enum<T>, String> = listArgument<Enum<T>>(name, T::class.java.enumConstants.toList(), parse, converter, scope,block)

/**
 * Adds an [EnumArgument] to the command with the given name.
 *
 * @param formatting The formatting style for the enum names (default: [EnumFormatting.LOWERCASE]).
 * @return The created [EnumArgument].
 */
inline fun <reified T : Enum<T>> StellarCommand<*>.enumArgument(
    name: String,
    formatting: EnumFormatting = EnumFormatting.LOWERCASE,
    block: EnumArgument<T>.() -> Unit = {},
): EnumArgument<T> = addArgument(EnumArgument(name, T::class.java, { Suggestion.withText(formatting.action(it.name)) })).apply(block)

/**
 * Adds an [EnumArgument] to the command with the given name.
 *
 * @param formatting The formatting style for the enum names (default: [EnumFormatting.LOWERCASE]).
 * @return The created [EnumArgument].
 */
fun <T : Enum<T>> StellarCommand<*>.enumArgument(
    name: String,
    enum: Class<T>,
    formatting: EnumFormatting = EnumFormatting.LOWERCASE,
    block: EnumArgument<T>.() -> Unit = {},
): EnumArgument<T> = addArgument(EnumArgument(name, enum, { Suggestion.withText(formatting.action(it.name)) })).apply(block)

typealias KotlinOnlinePlayersArgument = ListArgument<Player, String>

/**
 * Adds an [OnlinePlayersArgument] to the command with the given name. It is a list of all currently online players.
 *
 * @param filter A function to filter players (default: exclude sender).
 * @param scope The [CoroutineScope] used to compute the list.
 * @return The created [OnlinePlayersArgument], which returns a [Player] when parsed.
 */
fun StellarCommand<*>.onlinePlayersArgument(
    name: String,
    filter: suspend CommandSender.(Player) -> Boolean = { it != this },
    scope: CoroutineScope = StellarConfig.scope,
    block: OnlinePlayersArgument.() -> Unit = {},
): OnlinePlayersArgument = addArgument(OnlinePlayersArgument(name) { players ->
    scope.future {
        players.filter { filter(it) }
    }
}).apply(block)

// Math
/**
 * Adds an [AngleArgument] to the command with the given name.
 * @return The created [AngleArgument].
 */
fun StellarCommand<*>.angleArgument(name: String, block: AngleArgument.() -> Unit = {}): AngleArgument = addArgument(AngleArgument(name)).apply(block)

/**
 * Adds an [AxisArgument] to the command with the given name.
 * @return The created [AxisArgument].
 */
fun StellarCommand<*>.axisArgument(name: String, block: AxisArgument.() -> Unit = {}): AxisArgument = addArgument(AxisArgument(name)).apply(block)

/**
 * Adds a [DoubleRangeArgument] to the command with the given name.
 * @return The created [DoubleRangeArgument].
 */
fun StellarCommand<*>.doubleRangeArgument(name: String, block: DoubleRangeArgument.() -> Unit = {}): DoubleRangeArgument = addArgument(DoubleRangeArgument(name)).apply(block)

/**
 * Adds an [IntRangeArgument] to the command with the given name.
 * @return The created [IntRangeArgument].
 */
fun StellarCommand<*>.intRangeArgument(name: String, block: IntRangeArgument.() -> Unit = {}): IntRangeArgument = addArgument(IntRangeArgument(name)).apply(block)

/**
 * Adds an [OperationArgument] to the command with the given name.
 * @return The created [OperationArgument].
 */
fun StellarCommand<*>.operationArgument(name: String, block: OperationArgument.() -> Unit = {}): OperationArgument = addArgument(OperationArgument(name)).apply(block)

/**
 * Adds a [RotationArgument] to the command with the given name.
 * @return The created [RotationArgument].
 */
fun StellarCommand<*>.rotationArgument(name: String, block: RotationArgument.() -> Unit = {}): RotationArgument = addArgument(RotationArgument(name)).apply(block)

/**
 * Adds a [TimeArgument] to the command with the given name.
 *
 * @param minimum The minimum allowed time value (default: `0`).
 * @return The created [TimeArgument].
 */
fun StellarCommand<*>.timeArgument(name: String, minimum: Int = 0, block: TimeArgument.() -> Unit = {}): TimeArgument = addArgument(TimeArgument(name, minimum)).apply(block)

// Misc
/**
 * Adds a [NamespacedKeyArgument] to the command with the given name.
 * @return The created [NamespacedKeyArgument].
 */
fun StellarCommand<*>.namespacedKeyArgument(name: String, block: NamespacedKeyArgument.() -> Unit = {}): NamespacedKeyArgument = addArgument(NamespacedKeyArgument(name)).apply(block)

/**
 * Adds a [UUIDArgument] to the command with the given name.
 * @return The created [UUIDArgument].
 */
fun StellarCommand<*>.uuidArgument(name: String, block: UUIDArgument.() -> Unit = {}): UUIDArgument = addArgument(UUIDArgument(name)).apply(block)

// Phrase
/**
 * Adds a [PhraseArgument] to the command with the given name.
 * @return The created [PhraseArgument].
 */
fun StellarCommand<*>.phraseArgument(name: String, block: PhraseArgument.() -> Unit = {}): PhraseArgument = addArgument(PhraseArgument(name)).apply(block)

// Player
/**
 * Adds a [GameModeArgument] to the command with the given name.
 * @return The created [GameModeArgument].
 */
fun StellarCommand<*>.gameModeArgument(name: String, block: GameModeArgument.() -> Unit = {}): GameModeArgument = addArgument(GameModeArgument(name)).apply(block).apply(block)

/**
 * Adds a [GameProfileArgument] to the command with the given name.
 * @return The created [GameProfileArgument].
 */
fun StellarCommand<*>.gameProfileArgument(name: String, block: GameProfileArgument.() -> Unit = {}): GameProfileArgument = addArgument(GameProfileArgument(name)).apply(block).apply(block)

// Scoreboard
/**
 * Adds a [DisplaySlotArgument] to the command with the given name.
 * @return The created [DisplaySlotArgument].
 */
fun StellarCommand<*>.displaySlotArgument(name: String, block: DisplaySlotArgument.() -> Unit = {}): DisplaySlotArgument = addArgument(DisplaySlotArgument(name)).apply(block).apply(block)

/**
 * Adds an [ObjectiveArgument] to the command with the given name.
 * @return The created [ObjectiveArgument].
 */
fun StellarCommand<*>.objectiveArgument(name: String, block: ObjectiveArgument.() -> Unit = {}): ObjectiveArgument = addArgument(ObjectiveArgument(name)).apply(block).apply(block)

/**
 * Adds an [ObjectiveCriteriaArgument] to the command with the given name.
 * @return The created [ObjectiveCriteriaArgument].
 */
fun StellarCommand<*>.objectiveCriteriaArgument(name: String, block: ObjectiveCriteriaArgument.() -> Unit = {}): ObjectiveCriteriaArgument = addArgument(ObjectiveCriteriaArgument(name)).apply(block)

/**
 * Adds a [ScoreHolderArgument] to the command with the given name.
 *
 * @param type The type of score holder.
 * @return The created [ScoreHolderArgument].
 */
fun StellarCommand<*>.scoreHolderArgument(name: String, type: ScoreHolderType, block: ScoreHolderArgument.() -> Unit = {}): ScoreHolderArgument = addArgument(ScoreHolderArgument(name, type)).apply(block)

/**
 * Adds a [TeamArgument] to the command with the given name.
 * @return The created [TeamArgument].
 */
fun StellarCommand<*>.teamArgument(name: String, block: TeamArgument.() -> Unit = {}): TeamArgument = addArgument(TeamArgument(name)).apply(block)

// Structure
/**
 * Adds a [LootTableArgument] to the command with the given name.
 * @return The created [LootTableArgument].
 */
fun StellarCommand<*>.lootTableArgument(name: String, block: LootTableArgument.() -> Unit = {}): LootTableArgument = addArgument(LootTableArgument(name)).apply(block)

/**
 * Adds a [MirrorArgument] to the command with the given name.
 * @return The created [MirrorArgument].
 */
fun StellarCommand<*>.mirrorArgument(name: String, block: MirrorArgument.() -> Unit = {}): MirrorArgument = addArgument(MirrorArgument(name)).apply(block)

/**
 * Adds a [StructureRotationArgument] to the command with the given name.
 * @return The created [StructureRotationArgument].
 */
fun StellarCommand<*>.structureRotationArgument(name: String, block: StructureRotationArgument.() -> Unit = {}): StructureRotationArgument = addArgument(StructureRotationArgument(name)).apply(block)

// Text
/**
 * Adds a [ColorArgument] to the command with the given name.
 * @return The created [ColorArgument].
 */
fun StellarCommand<*>.colorArgument(name: String, block: ColorArgument.() -> Unit = {}): ColorArgument = addArgument(ColorArgument(name)).apply(block)

/**
 * Adds a [ComponentArgument] to the command with the given name.
 * @return The created [ComponentArgument].
 */
fun StellarCommand<*>.componentArgument(name: String, block: ComponentArgument.() -> Unit = {}): ComponentArgument = addArgument(ComponentArgument(name)).apply(block)

/**
 * Adds a [HexArgument] to the command with the given name.
 * @return The created [HexArgument].
 */
fun StellarCommand<*>.hexArgument(name: String, block: HexArgument.() -> Unit = {}): HexArgument = addArgument(HexArgument(name)).apply(block)

/**
 * Adds a [MessageArgument] to the command with the given name.
 * @return The created [MessageArgument].
 */
fun StellarCommand<*>.messageArgument(name: String, block: MessageArgument.() -> Unit = {}): MessageArgument = addArgument(MessageArgument(name)).apply(block)

/**
 * Adds a [StyleArgument] to the command with the given name.
 * @return The created [StyleArgument].
 */
fun StellarCommand<*>.styleArgument(name: String, block: StyleArgument.() -> Unit = {}): StyleArgument = addArgument(StyleArgument(name)).apply(block)

// World
/**
 * Adds an [EnvironmentArgument] to the command with the given name.
 * @return The created [EnvironmentArgument].
 */
fun StellarCommand<*>.environmentArgument(name: String, block: EnvironmentArgument.() -> Unit = {}): EnvironmentArgument = addArgument(EnvironmentArgument(name)).apply(block)

/**
 * Adds a [HeightMapArgument] to the command with the given name.
 * @return The created [HeightMapArgument].
 */
fun StellarCommand<*>.heightMapArgument(name: String, block: HeightMapArgument.() -> Unit = {}): HeightMapArgument = addArgument(HeightMapArgument(name)).apply(block)

/**
 * Adds a [LocationArgument] to the command with the given name.
 *
 * @param type The type of location.
 * @return The created [LocationArgument].
 */
fun StellarCommand<*>.locationArgument(name: String, type: LocationType, block: LocationArgument.() -> Unit = {}): LocationArgument = addArgument(LocationArgument(name, type)).apply(block)