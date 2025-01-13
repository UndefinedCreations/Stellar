@file:Suppress("DEPRECATION")

package com.undefined.stellar.argument

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.block.BlockDataArgument
import com.undefined.stellar.argument.block.BlockPredicateArgument
import com.undefined.stellar.argument.custom.EnumArgument
import com.undefined.stellar.argument.custom.EnumFormatting
import com.undefined.stellar.argument.custom.ListArgument
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
import com.undefined.stellar.argument.primitive.*
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
import java.util.*

/**
 * An open class that handles the addition of arguments
 *
 * @since 1.0
 */
open class ArgumentHandler {

    open val base: AbstractStellarCommand<*> get() = throw IllegalStateException("Cannot access the getter from the property base when it hasn't been overridden!")
    open val arguments: MutableList<AbstractStellarArgument<*>> = mutableListOf()

    fun addArgument(argument: AbstractStellarArgument<*>): AbstractStellarArgument<*> =
        argument.also { arguments.add(it) }

    fun addArgument(name: String, vararg aliases: String): LiteralStellarArgument =
        addArgument { LiteralStellarArgument(base, name).apply { this.aliases.addAll(aliases) } }

    fun addLiteralArgument(name: String): LiteralStellarArgument =
        addArgument { LiteralStellarArgument(base, name) }

    inline fun <reified T : AbstractStellarArgument<*>> addArgument(argument: () -> AbstractStellarArgument<*>): T {
        val parsedArgument = argument()
        addArgument(parsedArgument)
        return parsedArgument as T
    }

//    fun <T> addCustomArgument(argument: CustomArgument<T>): CustomArgument<T> {
//        addArgument(argument)
//        return argument
//    }

    fun addStringArgument(name: String, type: StringType = StringType.WORD): StringArgument =
        addArgument { StringArgument(base, name, type)  }

    fun addPhraseArgument(name: String): PhraseArgument =
        addArgument { PhraseArgument(base, name) }

    fun addIntegerArgument(name: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): IntegerArgument =
        addArgument { IntegerArgument(base, name, min, max) }

    fun addLongArgument(name: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): com.undefined.stellar.argument.primitive.LongArgument =
        addArgument { com.undefined.stellar.argument.primitive.LongArgument(base, name, min, max) }

    fun addFloatArgument(name: String, min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE): com.undefined.stellar.argument.primitive.FloatArgument =
        addArgument { com.undefined.stellar.argument.primitive.FloatArgument(base, name, min, max) }

    fun addDoubleArgument(name: String, min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE): DoubleArgument =
        addArgument { DoubleArgument(base, name, min, max) }

    fun addBooleanArgument(name: String): BooleanArgument =
        addArgument { BooleanArgument(base, name) }

    fun <T> addListArgument(
        name: String,
        list: List<T>,
        converter: (T) -> Suggestion,
        parse: (Any?) -> T
    ): ListArgument<T> = addArgument { ListArgument(StringArgument(base, name, StringType.WORD), list, converter, parse) }

    fun <T> addListArgument(
        type: AbstractStellarArgument<*>,
        list: List<T>,
        converter: (T) -> Suggestion,
        parse: (Any?) -> T
    ): ListArgument<T> = addArgument { ListArgument(type, list, converter, parse) }

    fun <T> addListArgument(
        name: String,
        list: CommandContext<CommandSender>.() -> List<T>,
        converter: (T) -> Suggestion,
        parse: (Any?) -> T
    ): ListArgument<T> = addArgument { ListArgument(StringArgument(base, name, StringType.WORD), list, converter, parse) }

    fun <T> addListArgument(
        type: AbstractStellarArgument<*>,
        list: CommandContext<CommandSender>.() -> List<T>,
        converter: (T) -> Suggestion,
        parse: (Any?) -> T
    ): ListArgument<T> = addArgument { ListArgument(type, list, converter, parse) }

    fun <T> addBasicListArgument(
        name: String,
        list: List<T>,
        converter: (T) -> String,
        parse: (Any?) -> T
    ): ListArgument<T> = addArgument { ListArgument(StringArgument(base, name, StringType.WORD), list, { Suggestion.withText(converter(it)) }, parse) }

    fun <T> addBasicListArgument(
        type: AbstractStellarArgument<*>,
        list: List<T>,
        converter: (T) -> String,
        parse: (Any?) -> T
    ): ListArgument<T> = addArgument { ListArgument(type, list, { Suggestion.withText(converter(it)) }, parse) }

    fun <T> addBasicListArgument(
        name: String,
        list: CommandContext<CommandSender>.() -> List<T>,
        converter: (T) -> String,
        parse: (Any?) -> T
    ): ListArgument<T> = addArgument { ListArgument(StringArgument(base, name, StringType.WORD), list, { Suggestion.withText(converter(it)) }, parse) }

    fun <T> addBasicListArgument(
        type: AbstractStellarArgument<*>,
        list: CommandContext<CommandSender>.() -> List<T>,
        converter: (T) -> String,
        parse: (Any?) -> T
    ): ListArgument<T> = addArgument { ListArgument(type, list, { Suggestion.withText(converter(it)) }, parse) }

    fun addStringListArgument(name: String, list: List<String>, type: StringType = StringType.WORD, parse: (Any?) -> Any? = { it }): ListArgument<String> =
        addArgument { ListArgument(StringArgument(base, name, type), list, { Suggestion.withText(it.toString()) }, parse) }

    fun addStringListArgument(name: String, vararg list: String): ListArgument<String> =
        addArgument { ListArgument(StringArgument(base, name, StringType.WORD), list.toList(), { Suggestion.withText(it.toString()) }, { it }) }

    fun addUUIDListArgument(name: String, list: List<UUID>, parse: (Any?) -> Any? = { it }): ListArgument<UUID> =
        addArgument { ListArgument(UUIDArgument(base, name), list, parse = parse) }

    fun addStringListArgument(name: String, list: CommandContext<CommandSender>.() -> List<String>, type: StringType = StringType.WORD): ListArgument<String> =
        addArgument { ListArgument(StringArgument(base, name, type), list, { Suggestion.withText(it.toString()) }, { it }) }

    fun addStringListArgument(name: String, vararg list: CommandContext<CommandSender>.() -> List<String>): ListArgument<String> =
        addArgument { ListArgument(StringArgument(base, name, StringType.WORD), list.toList(), { Suggestion.withText(it.toString()) }, { it }) }

    fun addUUIDListArgument(name: String, list: CommandContext<CommandSender>.() -> List<UUID>): ListArgument<UUID> =
        addArgument { ListArgument(UUIDArgument(base, name), list, parse = { it }) }

    inline fun <reified T : Enum<T>> addEnumArgument(name: String, formatting: EnumFormatting = EnumFormatting.LOWERCASE): EnumArgument<T> =
        addArgument { EnumArgument<T>(base, name, T::class, { Suggestion.withText(formatting.action(it!!.name)) }) }

    inline fun <reified T : Enum<T>> addEnumArgument(
        name: String,
        noinline converter: (Enum<*>?) -> Suggestion = { Suggestion.withText(it!!.name) },
        noinline parse: (Any?) -> Enum<T>?
    ): EnumArgument<T> = addArgument {
        EnumArgument(
            base,
            name,
            T::class,
            converter,
            parse
        )
    }

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

    fun addDimensionArgument(name: String): com.undefined.stellar.argument.world.DimensionArgument =
        addArgument { com.undefined.stellar.argument.world.DimensionArgument(base, name) }

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

    fun addOnlinePlayersArgument(name: String): ListArgument<Player> =
        addArgument {
            ListArgument(
                StringArgument(base, name, StringType.WORD),
                { Bukkit.getOnlinePlayers().toList() },
                { Suggestion.withText(it.name) },
                { Bukkit.getPlayer(it.toString()) },
            )
        }

    fun addOfflinePlayersArgument(name: String): ListArgument<OfflinePlayer> =
        addArgument {
            ListArgument(
                StringArgument(base, name, StringType.WORD),
                { Bukkit.getOfflinePlayers().toList() },
                { Suggestion.withText(it.name!!) },
                { Bukkit.getOfflinePlayer(it.toString()) },
            )
        }

    fun addGameEventArgument(name: String): GameEventArgument =
        addArgument { GameEventArgument(base, name) }

    fun addStructureTypeArgument(name: String): com.undefined.stellar.argument.registry.StructureTypeArgument =
        addArgument { com.undefined.stellar.argument.registry.StructureTypeArgument(base, name) }

    fun addPotionEffectTypeArgument(name: String): PotionEffectTypeArgument =
        addArgument { PotionEffectTypeArgument(base, name) }

    fun addBlockTypeArgument(name: String): BlockTypeArgument =
        addArgument { BlockTypeArgument(base, name) }

    fun addItemTypeArgument(name: String): ItemTypeArgument =
        addArgument { ItemTypeArgument(base, name) }

    fun addCatTypeArgument(name: String): CatTypeArgument =
        addArgument { CatTypeArgument(base, name) }

    fun addFrogVariantArgument(name: String): com.undefined.stellar.argument.registry.FrogVariantArgument =
        addArgument { com.undefined.stellar.argument.registry.FrogVariantArgument(base, name) }

    fun addVillagerProfessionArgument(name: String): VillagerProfessionArgument =
        addArgument { VillagerProfessionArgument(base, name) }

    fun addVillagerTypeArgument(name: String): com.undefined.stellar.argument.registry.VillagerTypeArgument =
        addArgument { com.undefined.stellar.argument.registry.VillagerTypeArgument(base, name) }

    fun addMapDecorationType(name: String): com.undefined.stellar.argument.registry.MapDecorationTypeArgument =
        addArgument { com.undefined.stellar.argument.registry.MapDecorationTypeArgument(base, name) }

    fun addInventoryTypeArgument(name: String): InventoryTypeArgument =
        addArgument { InventoryTypeArgument(base, name) }

    fun addAttributeArgument(name: String): com.undefined.stellar.argument.registry.AttributeArgument =
        addArgument { com.undefined.stellar.argument.registry.AttributeArgument(base, name) }

    fun addFluidArgument(name: String): FluidArgument =
        addArgument { FluidArgument(base, name) }

    fun addSoundArgument(name: String): SoundArgument =
        addArgument { SoundArgument(base, name) }

    fun addBiomeArgument(name: String): com.undefined.stellar.argument.registry.BiomeArgument =
        addArgument { com.undefined.stellar.argument.registry.BiomeArgument(base, name) }

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

    fun addArtArgument(name: String): com.undefined.stellar.argument.registry.ArtArgument =
        addArgument { com.undefined.stellar.argument.registry.ArtArgument(base, name) }

    fun addInstrumentArgument(name: String): InstrumentArgument =
        addArgument { InstrumentArgument(base, name) }

    fun addEntityTypeArgument(name: String): EntityTypeArgument =
        addArgument { EntityTypeArgument(base, name) }

    fun addPotionArgument(name: String): PotionArgument =
        addArgument { PotionArgument(base, name) }

    fun addMemoryKeyArgument(name: String): com.undefined.stellar.argument.registry.MemoryKeyArgument =
        addArgument { com.undefined.stellar.argument.registry.MemoryKeyArgument(base, name) }

}