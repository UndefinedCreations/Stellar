@file:Suppress("DEPRECATION")

package com.undefined.stellar.argument

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.basic.*
import com.undefined.stellar.argument.block.BlockDataArgument
import com.undefined.stellar.argument.block.BlockPredicateArgument
import com.undefined.stellar.argument.entity.EntityAnchorArgument
import com.undefined.stellar.argument.item.ItemPredicateArgument
import com.undefined.stellar.argument.item.ItemSlotArgument
import com.undefined.stellar.argument.item.ItemSlotsArgument
import com.undefined.stellar.argument.math.AngleArgument
import com.undefined.stellar.argument.math.OperationArgument
import com.undefined.stellar.argument.math.RangeArgument
import com.undefined.stellar.argument.math.TimeArgument
import com.undefined.stellar.argument.misc.NamespacedKeyArgument
import com.undefined.stellar.argument.misc.UUIDArgument
import com.undefined.stellar.argument.player.GameModeArgument
import com.undefined.stellar.argument.player.GameProfileArgument
import com.undefined.stellar.argument.registry.*
import com.undefined.stellar.argument.scoreboard.ObjectiveCriteriaArgument
import com.undefined.stellar.argument.structure.LootTableArgument
import com.undefined.stellar.argument.structure.MirrorArgument
import com.undefined.stellar.argument.text.ColorArgument
import com.undefined.stellar.argument.text.ComponentArgument
import com.undefined.stellar.argument.text.MessageArgument
import com.undefined.stellar.argument.text.StyleArgument
import com.undefined.stellar.argument.world.HeightMapArgument
import com.undefined.stellar.argument.world.LocationArgument
import com.undefined.stellar.argument.world.LocationType
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * An open class that handles the addition of arguments.
 *
 * @since 1.0
 */
open class ArgumentHandler {

    @get:ApiStatus.Internal
    open val base: AbstractStellarCommand<*> get() = throw IllegalStateException("Cannot access the getter from the property base when it hasn't been overridden!")
    open val arguments: MutableList<AbstractStellarArgument<*, *>> = mutableListOf()

    /**
     * Add argument from a `AbstractStellarArgument` instance.
     */
    fun addArgument(argument: AbstractStellarArgument<*, *>): AbstractStellarArgument<*, *> =
        argument.also { arguments.add(it) }

    /**
     * Add literal argument with specified names. The first name being the actual command name, while the rest are aliases.
     */
    fun addArgument(vararg names: String): LiteralStellarArgument =
        addArgument { LiteralStellarArgument(base, names.first()).apply { this.aliases.addAll(names) } }

    /**
     * Add literal argument with specified names. The first name being the actual command name, while the rest are aliases.
     */
    fun addLiteralArgument(vararg names: String): LiteralStellarArgument =
        addArgument { LiteralStellarArgument(base, names.first()).apply { this.aliases.addAll(names) } }

    /**
     * Add argument from function type returning an argument.
     */
    inline fun <reified T : AbstractStellarArgument<*, *>> addArgument(argument: () -> AbstractStellarArgument<*, *>): T {
        val parsedArgument = argument()
        addArgument(parsedArgument)
        return parsedArgument as T
    }

    /**
     * Add `CustomArgument` from function type including parent and returning a `CustomArgument`
     */
    fun <T, R> addCustomArgument(block: (parent: AbstractStellarCommand<*>) -> CustomArgument<T, R>): CustomArgument<T, R> = addArgument { block(base) }

    /**
     * Add `StringArgument` with specified name, and `StringType`.
     */
    fun addStringArgument(name: String, type: StringType = StringType.WORD): StringArgument =
        addArgument { StringArgument(base, name, type)  }

    /**
     * Add `PhraseArgument` with specified name.
     */
    fun addPhraseArgument(name: String): PhraseArgument =
        addArgument { PhraseArgument(base, name) }

    /**
     * Add `IntegerArgument` with specified name, and minimum, maximum values, defining the range of the possible integer.
     */
    fun addIntegerArgument(name: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): IntegerArgument =
        addArgument { IntegerArgument(base, name, min, max) }

    /**
     * Add `LongArgument` with specified name, and minimum, maximum values, defining the range of the possible long.
     */
    fun addLongArgument(name: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): LongArgument =
        addArgument { LongArgument(base, name, min, max) }

    /**
     * Add `FloatArgument` with specified name, and minimum, maximum values, defining the range of the possible float.
     */
    fun addFloatArgument(name: String, min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE): FloatArgument =
        addArgument { FloatArgument(base, name, min, max) }

    /**
     * Add `DoubleArgument` with specified name, and minimum, maximum values, defining the range of the possible double.
     */
    fun addDoubleArgument(name: String, min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE): DoubleArgument =
        addArgument { DoubleArgument(base, name, min, max) }

    /**
     * Add `Boolean` with specified name.
     */
    fun addBooleanArgument(name: String): BooleanArgument =
        addArgument { BooleanArgument(base, name) }

    /**
     * Add `LongArgument` with specified name, and minimum, maximum values, defining the range of the possible long.
     */
    fun <T> addAdvancedListArgument(
        name: String,
        list: List<T>,
        converter: CommandContext<CommandSender>.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
        parse: CommandContext<CommandSender>.(String) -> T,
        async: Boolean = false,
    ): ListArgument<T, String> = addArgument { ListArgument(StringArgument(base, name, StringType.WORD), list, converter, parse, async) }

    /**
     * Add `ListArgument` with type.
     *
     * @param type An instance of `AbstractStellarArgument` to use for parsing
     */
    fun <T, R> addAdvancedListArgument(
        type: AbstractStellarArgument<*, R>,
        list: List<T>,
        converter: CommandContext<CommandSender>.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
        parse: CommandContext<CommandSender>.(R) -> T,
        async: Boolean = false,
    ): ListArgument<T, R> = addArgument { ListArgument(type, list, converter, parse, async) }

    fun <T> addAdvancedListArgument(
        name: String,
        list: CommandContext<CommandSender>.() -> List<T>,
        converter: CommandContext<CommandSender>.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
        parse: CommandContext<CommandSender>.(String) -> T,
        async: Boolean = false,
    ): ListArgument<T, String> = addArgument { ListArgument(StringArgument(base, name, StringType.WORD), list, converter, parse, async) }

    fun <T, R> addAdvancedListArgument(
        type: AbstractStellarArgument<*, R>,
        list: CommandContext<CommandSender>.() -> List<T>,
        converter: CommandContext<CommandSender>.(T) -> Suggestion? = { Suggestion.withText(it.toString()) },
        parse: CommandContext<CommandSender>.(R) -> T,
        async: Boolean = false,
    ): ListArgument<T, R> = addArgument { ListArgument(type, list, converter, parse, async) }

    fun <T> addListArgument(
        name: String,
        list: List<T>,
        converter: CommandContext<CommandSender>.(T) -> String? = { it.toString() },
        parse: CommandContext<CommandSender>.(String) -> T,
        async: Boolean = false,
    ): ListArgument<T, String> = addArgument { ListArgument(StringArgument(base, name, StringType.WORD), list, { converter(it)?.let { Suggestion.withText(it) } }, parse, async) }

    fun <T, R> addListArgument(
        type: AbstractStellarArgument<*, R>,
        list: List<T>,
        converter: CommandContext<CommandSender>.(T) -> String? = { it.toString() },
        parse: CommandContext<CommandSender>.(R) -> T,
        async: Boolean = false,
    ): ListArgument<T, R> = addArgument { ListArgument(type, list, { converter(it)?.let { Suggestion.withText(it) } }, parse, async) }

    fun <T> addListArgument(
        name: String,
        list: CommandContext<CommandSender>.() -> List<T>,
        converter: CommandContext<CommandSender>.(T) -> String? = { it.toString() },
        parse: CommandContext<CommandSender>.(String) -> T,
        async: Boolean = false,
    ): ListArgument<T, String> = addArgument { ListArgument(StringArgument(base, name, StringType.WORD), list, { converter(it)?.let { Suggestion.withText(it) } }, parse, async) }

    fun <T, R> addListArgument(
        type: AbstractStellarArgument<*, R>,
        list: CommandContext<CommandSender>.() -> List<T>,
        converter: CommandContext<CommandSender>.(T) -> String? = { it.toString() },
        parse: CommandContext<CommandSender>.(R) -> T,
        async: Boolean = false,
    ): ListArgument<T, R> = addArgument { ListArgument(type, list, { converter(it)?.let { Suggestion.withText(it) } }, parse, async) }

    fun addStringListArgument(
        name: String,
        list: List<String>,
        type: StringType = StringType.WORD,
        parse: CommandContext<CommandSender>.(String) -> Any = { it },
        async: Boolean = false,
    ): ListArgument<String, String> =
        addArgument { ListArgument(StringArgument(base, name, type), list, { Suggestion.withText(it.toString()) }, parse, async) }

    fun addStringListArgument(
        name: String,
        vararg list: String,
        async: Boolean = false,
    ): ListArgument<String, String> =
        addArgument { ListArgument(StringArgument(base, name, StringType.WORD), list.toList(), { Suggestion.withText(it) }, { it }, async) }

    fun addUUIDListArgument(
        name: String,
        list: List<UUID>,
        parse: CommandContext<CommandSender>.(UUID) -> Any? = { it },
        async: Boolean = false,
    ): ListArgument<UUID, UUID> =
        addArgument { ListArgument(UUIDArgument(base, name), list, parse = parse, async = async) }

    fun addStringListArgument(
        name: String,
        list: CommandContext<CommandSender>.() -> List<String>,
        type: StringType = StringType.WORD,
        async: Boolean = false,
    ): ListArgument<String, String> =
        addArgument { ListArgument(StringArgument(base, name, type), list, { Suggestion.withText(it) }, { it }, async) }

    fun addStringListArgument(
        name: String,
        vararg list: CommandContext<CommandSender>.() -> List<String>,
        async: Boolean = false,
    ): ListArgument<String, String> =
        addArgument { ListArgument(StringArgument(base, name, StringType.WORD), { list.flatMap { it() } }, converter = { Suggestion.withText(it) }, { it }, async) }

    fun addUUIDListArgument(
        name: String,
        list: CommandContext<CommandSender>.() -> List<UUID>,
        async: Boolean = false,
    ): ListArgument<UUID, UUID> =
        addArgument { ListArgument(UUIDArgument(base, name), list, parse = { it }, async = async) }

    inline fun <reified T : Enum<T>> addEnumArgument(
        name: String,
        formatting: EnumFormatting = EnumFormatting.LOWERCASE,
        async: Boolean = false,
    ): EnumArgument<T> =
        addArgument { EnumArgument(base, name, T::class, { Suggestion.withText(formatting.action(it.name)) }, async = async) }

    inline fun <reified T : Enum<T>> addEnumArgument(
        name: String,
        noinline converter: CommandContext<CommandSender>.(Enum<*>) -> Suggestion? = { Suggestion.withText(it.name) },
        noinline parse: CommandContext<CommandSender>.(String) -> Enum<T>,
        async: Boolean = false,
    ): EnumArgument<T> = addArgument { EnumArgument(base, name, T::class, converter, parse, async) }

    fun addEntityArgument(name: String, type: com.undefined.stellar.argument.entity.EntityDisplayType): com.undefined.stellar.argument.entity.EntityArgument =
        addArgument { com.undefined.stellar.argument.entity.EntityArgument(base, name, type) }

    fun addGameProfileArgument(name: String): GameProfileArgument =
        addArgument { GameProfileArgument(base, name) }

    fun addLocationArgument(name: String, type: LocationType = LocationType.LOCATION_3D): LocationArgument =
        addArgument { LocationArgument(base, name, type) }

    fun addBlockDataArgument(name: String): BlockDataArgument =
        addArgument { BlockDataArgument(base, name) }

    fun addBlockPredicateArgument(name: String): BlockPredicateArgument =
        addArgument { BlockPredicateArgument(base, name) }

    fun addItemArgument(name: String): com.undefined.stellar.argument.item.ItemArgument =
        addArgument { com.undefined.stellar.argument.item.ItemArgument(base, name) }

    fun addItemPredicateArgument(name: String): ItemPredicateArgument =
        addArgument { ItemPredicateArgument(base, name) }

    fun addColorArgument(name: String): ColorArgument =
        addArgument { ColorArgument(base, name) }

    fun addComponentArgument(name: String): ComponentArgument =
        addArgument { ComponentArgument(base, name) }

    fun addStyleArgument(name: String): StyleArgument =
        addArgument { StyleArgument(base, name) }

    fun addMessageArgument(name: String): MessageArgument =
        addArgument { MessageArgument(base, name) }

    fun addObjectiveArgument(name: String): com.undefined.stellar.argument.scoreboard.ObjectiveArgument =
        addArgument { com.undefined.stellar.argument.scoreboard.ObjectiveArgument(base, name) }

    fun addObjectiveCriteriaArgument(name: String): ObjectiveCriteriaArgument =
        addArgument { ObjectiveCriteriaArgument(base, name) }

    fun addOperationArgument(name: String): OperationArgument =
        addArgument { OperationArgument(base, name) }

    fun addParticleArgument(name: String): com.undefined.stellar.argument.world.ParticleArgument =
        addArgument { com.undefined.stellar.argument.world.ParticleArgument(base, name) }

    fun addAngleArgument(name: String): AngleArgument =
        addArgument { AngleArgument(base, name) }

    fun addRotationArgument(name: String): com.undefined.stellar.argument.math.RotationArgument =
        addArgument { com.undefined.stellar.argument.math.RotationArgument(base, name) }

    fun addDisplaySlotArgument(name: String): com.undefined.stellar.argument.scoreboard.DisplaySlotArgument =
        addArgument { com.undefined.stellar.argument.scoreboard.DisplaySlotArgument(base, name) }

    fun addScoreHolderArgument(name: String, type: com.undefined.stellar.argument.scoreboard.ScoreHolderType = com.undefined.stellar.argument.scoreboard.ScoreHolderType.SINGLE): com.undefined.stellar.argument.scoreboard.ScoreHolderArgument =
        addArgument { com.undefined.stellar.argument.scoreboard.ScoreHolderArgument(base, name, type) }

    fun addAxisArgument(name: String): com.undefined.stellar.argument.math.AxisArgument =
        addArgument { com.undefined.stellar.argument.math.AxisArgument(base, name) }

    fun addTeamArgument(name: String): com.undefined.stellar.argument.scoreboard.TeamArgument =
        addArgument { com.undefined.stellar.argument.scoreboard.TeamArgument(base, name) }

    fun addItemSlotArgument(name: String): ItemSlotArgument =
        addArgument { ItemSlotArgument(base, name) }

    fun addItemSlotsArgument(name: String): ItemSlotsArgument =
        addArgument { ItemSlotsArgument(base, name) }

    fun addNamespacedKeyArgument(name: String): NamespacedKeyArgument =
        addArgument { NamespacedKeyArgument(base, name) }

    fun addEntityAnchorArgument(name: String): EntityAnchorArgument =
        addArgument { EntityAnchorArgument(base, name) }

    fun addRangeArgument(name: String): RangeArgument =
        addArgument { RangeArgument(base, name) }

    fun addGameModeArgument(name: String): GameModeArgument =
        addArgument { GameModeArgument(base, name) }

    fun addDimensionArgument(name: String): com.undefined.stellar.argument.world.EnvironmentArgument =
        addArgument { com.undefined.stellar.argument.world.EnvironmentArgument(base, name) }

    fun addTimeArgument(name: String, minimum: Int = 0): TimeArgument =
        addArgument { TimeArgument(base, name, minimum) }

    fun addMirrorArgument(name: String): MirrorArgument =
        addArgument { MirrorArgument(base, name) }

    fun addStructureRotationArgument(name: String): com.undefined.stellar.argument.structure.StructureRotationArgument =
        addArgument { com.undefined.stellar.argument.structure.StructureRotationArgument(base, name) }

    fun addHeightMapArgument(name: String): HeightMapArgument =
        addArgument { HeightMapArgument(base, name) }

    fun addLootTableArgument(name: String): LootTableArgument =
        addArgument { LootTableArgument(base, name) }

    fun addUUIDArgument(name: String): UUIDArgument =
        addArgument { UUIDArgument(base, name) }

    fun addOnlinePlayersArgument(name: String, async: Boolean = false): ListArgument<Player, String> =
        addArgument {
            ListArgument(
                StringArgument(base, name, StringType.WORD),
                { Bukkit.getOnlinePlayers().toList() },
                { Suggestion.withText(it.name) },
                { Bukkit.getPlayer(it) },
                async
            )
        }

    fun addOfflinePlayersArgument(name: String, async: Boolean = false): ListArgument<OfflinePlayer, String> =
        addArgument {
            ListArgument(
                StringArgument(base, name, StringType.WORD),
                { Bukkit.getOfflinePlayers().toList() },
                { Suggestion.withText(it.name!!) },
                { Bukkit.getOfflinePlayer(it) },
                async
            )
        }

    fun addGameEventArgument(name: String): GameEventArgument =
        addArgument { GameEventArgument(base, name) }

    fun addStructureTypeArgument(name: String): StructureTypeArgument =
        addArgument { StructureTypeArgument(base, name) }

    fun addPotionEffectTypeArgument(name: String): PotionEffectTypeArgument =
        addArgument { PotionEffectTypeArgument(base, name) }

    fun addBlockTypeArgument(name: String): BlockTypeArgument =
        addArgument { BlockTypeArgument(base, name) }

    fun addItemTypeArgument(name: String): ItemTypeArgument =
        addArgument { ItemTypeArgument(base, name) }

    fun addCatTypeArgument(name: String): CatTypeArgument =
        addArgument { CatTypeArgument(base, name) }

    fun addFrogVariantArgument(name: String): FrogVariantArgument =
        addArgument { FrogVariantArgument(base, name) }

    fun addVillagerProfessionArgument(name: String): VillagerProfessionArgument =
        addArgument { VillagerProfessionArgument(base, name) }

    fun addVillagerTypeArgument(name: String): VillagerTypeArgument =
        addArgument { VillagerTypeArgument(base, name) }

    fun addMapDecorationType(name: String): MapDecorationTypeArgument =
        addArgument { MapDecorationTypeArgument(base, name) }

    fun addInventoryTypeArgument(name: String): InventoryTypeArgument =
        addArgument { InventoryTypeArgument(base, name) }

    fun addAttributeArgument(name: String): AttributeArgument =
        addArgument { AttributeArgument(base, name) }

    fun addFluidArgument(name: String): FluidArgument =
        addArgument { FluidArgument(base, name) }

    fun addSoundArgument(name: String): SoundArgument =
        addArgument { SoundArgument(base, name) }

    fun addBiomeArgument(name: String): BiomeArgument =
        addArgument { BiomeArgument(base, name) }

    fun addStructureArgument(name: String): StructureArgument =
        addArgument { StructureArgument(base, name) }

    fun addTrimMaterialArgument(name: String): TrimMaterialArgument =
        addArgument { TrimMaterialArgument(base, name) }

    fun addTrimPatternArgument(name: String): TrimPatternArgument =
        addArgument { TrimPatternArgument(base, name) }

    fun addDamageTypeArgument(name: String): DamageTypeArgument =
        addArgument { DamageTypeArgument(base, name) }

    fun addWolfVariantArgument(name: String): WolfVariantArgument =
        addArgument { WolfVariantArgument(base, name) }

    fun addPatternTypeArgument(name: String): PatternTypeArgument =
        addArgument { PatternTypeArgument(base, name) }

    fun addArtArgument(name: String): ArtArgument =
        addArgument { ArtArgument(base, name) }

    fun addInstrumentArgument(name: String): InstrumentArgument =
        addArgument { InstrumentArgument(base, name) }

    fun addEntityTypeArgument(name: String): EntityTypeArgument =
        addArgument { EntityTypeArgument(base, name) }

    fun addPotionArgument(name: String): PotionArgument =
        addArgument { PotionArgument(base, name) }

    fun addMemoryKeyArgument(name: String): MemoryKeyArgument =
        addArgument { MemoryKeyArgument(base, name) }

}