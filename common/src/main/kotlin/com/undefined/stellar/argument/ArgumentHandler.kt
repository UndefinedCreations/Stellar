package com.undefined.stellar.argument

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.types.block.*
import com.undefined.stellar.argument.types.custom.CustomArgument
import com.undefined.stellar.argument.types.custom.EnumArgument
import com.undefined.stellar.argument.types.custom.ListArgument
import com.undefined.stellar.argument.types.entity.*
import com.undefined.stellar.argument.types.item.*
import com.undefined.stellar.argument.types.math.*
import com.undefined.stellar.argument.types.misc.NamespacedKeyArgument
import com.undefined.stellar.argument.types.misc.UUIDArgument
import com.undefined.stellar.argument.types.registry.BlockTypeArgument
import com.undefined.stellar.argument.types.registry.FluidArgument
import com.undefined.stellar.argument.types.player.GameModeArgument
import com.undefined.stellar.argument.types.player.GameProfileArgument
import com.undefined.stellar.argument.types.primitive.*
import com.undefined.stellar.argument.types.registry.*
import com.undefined.stellar.argument.types.scoreboard.*
import com.undefined.stellar.argument.types.registry.InstrumentArgument
import com.undefined.stellar.argument.types.registry.SoundArgument
import com.undefined.stellar.argument.types.structure.*
import com.undefined.stellar.argument.types.text.ColorArgument
import com.undefined.stellar.argument.types.text.ComponentArgument
import com.undefined.stellar.argument.types.text.MessageArgument
import com.undefined.stellar.argument.types.text.StyleArgument
import com.undefined.stellar.argument.types.world.*
import com.undefined.stellar.data.suggestion.Suggestion
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

@Suppress("DEPRECATION")
open class ArgumentHandler {

    open val base: AbstractStellarCommand<*> get() = throw IllegalStateException("Cannot access the getter from the property base when it hasn't been overridden!")
    open val arguments: MutableList<AbstractStellarArgument<*>> = mutableListOf()

    fun addArgument(argument: AbstractStellarArgument<*>): AbstractStellarArgument<*> {
        arguments.add(argument)
        return argument
    }

    fun addArgument(name: String): LiteralStellarArgument =
        addArgument { LiteralStellarArgument(base, name) }

    fun addLiteralArgument(name: String): LiteralStellarArgument =
        addArgument { LiteralStellarArgument(base, name) }

    inline fun <reified T : AbstractStellarArgument<*>> addArgument(argument: () -> AbstractStellarArgument<*>): T {
        val parsedArgument = argument()
        addArgument(parsedArgument)
        return parsedArgument as T
    }

    fun <T> addCustomArgument(argument: CustomArgument<T>): CustomArgument<T> {
        addArgument(argument)
        return argument
    }

    fun addStringArgument(name: String, type: StringType = StringType.WORD): StringArgument =
        addArgument { StringArgument(base, name, type)  }

    fun addPhraseArgument(name: String): PhraseArgument =
        addArgument { PhraseArgument(base, name) }

    fun addIntegerArgument(name: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): IntegerArgument =
        addArgument { IntegerArgument(base, name, min, max) }

    fun addLongArgument(name: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): LongArgument =
        addArgument { LongArgument(base, name, min, max) }

    fun addFloatArgument(name: String, min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE): FloatArgument =
        addArgument { FloatArgument(base, name, min, max) }

    fun addDoubleArgument(name: String, min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE): DoubleArgument =
        addArgument { DoubleArgument(base, name, min, max) }

    fun addBooleanArgument(name: String): BooleanArgument =
        addArgument { BooleanArgument(base, name) }

    fun <T, U : AbstractStellarArgument<*>> addListArgument(
        type: AbstractStellarArgument<*>,
        list: List<T>,
        stringifier: (T) -> Suggestion,
        parse: (Any?) -> T
    ): ListArgument<T> = addArgument { ListArgument(base, type, list, stringifier, parse) }

    fun addStringListArgument(name: String, list: List<String>, type: StringType = StringType.WORD): ListArgument<String> =
        addArgument { ListArgument(base, StringArgument(base, name, type), list, { Suggestion.withText(it.toString()) }, { it }) }

    fun addStringListArgument(name: String, vararg list: String): ListArgument<String> =
        addArgument { ListArgument(base, StringArgument(base, name, StringType.WORD), list.toList(), { Suggestion.withText(it.toString()) }, { it }) }

    fun addUUIDListArgument(name: String, list: List<UUID>): ListArgument<UUID> =
        addArgument { ListArgument(base, UUIDArgument(base, name), list, parse = { UUID.fromString(it.toString()) }) }

    inline fun <reified T : Enum<T>> addEnumArgument(name: String): EnumArgument<T> =
        addArgument { EnumArgument<T>(base, StringArgument(base, name, StringType.WORD), T::class) }

    inline fun <reified T : Enum<T>> addEnumArgument(
        name: String,
        noinline converter: (Enum<*>?) -> Suggestion = { Suggestion.withText(it?.name ?: "") },
        noinline parse: (Any?) -> Enum<T>?
    ): EnumArgument<T> = addArgument { EnumArgument(base, StringArgument(base, name, StringType.WORD), T::class, converter, parse) }

    fun addEntityArgument(name: String, type: EntityDisplayType): EntityArgument =
        addArgument { EntityArgument(base, name, type) }

    fun addGameProfileArgument(name: String): GameProfileArgument =
        addArgument { GameProfileArgument(base, name) }

    fun addLocationArgument(name: String, type: LocationType = LocationType.LOCATION3D): LocationArgument =
        addArgument { LocationArgument(base, name, type) }

    fun addBlockDataArgument(name: String): BlockDataArgument =
        addArgument { BlockDataArgument(base, name) }

    fun addBlockPredicateArgument(name: String): BlockPredicateArgument =
        addArgument { BlockPredicateArgument(base, name) }

    fun addItemArgument(name: String): ItemArgument =
        addArgument { ItemArgument(base, name) }

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

    fun addObjectiveArgument(name: String): ObjectiveArgument =
        addArgument { ObjectiveArgument(base, name) }

    fun addObjectiveCriteriaArgument(name: String): ObjectiveCriteriaArgument =
        addArgument { ObjectiveCriteriaArgument(base, name) }

    fun addOperationArgument(name: String): OperationArgument =
        addArgument { OperationArgument(base, name) }

    fun addParticleArgument(name: String): ParticleArgument =
        addArgument { ParticleArgument(base, name) }

    fun addAngleArgument(name: String): AngleArgument =
        addArgument { AngleArgument(base, name) }

    fun addRotationArgument(name: String): RotationArgument =
        addArgument { RotationArgument(base, name) }

    fun addDisplaySlotArgument(name: String): DisplaySlotArgument =
        addArgument { DisplaySlotArgument(base, name) }

    fun addScoreHolderArgument(name: String): ScoreHoldersArgument =
        addArgument { ScoreHoldersArgument(base, name) }

    fun addScoreHoldersArgument(name: String): ScoreHoldersArgument =
        addArgument { ScoreHoldersArgument(base, name) }

    fun addAxisArgument(name: String): AxisArgument =
        addArgument { AxisArgument(base, name) }

    fun addTeamArgument(name: String): TeamArgument =
        addArgument { TeamArgument(base, name) }

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

    fun addDimensionArgument(name: String): DimensionArgument =
        addArgument { DimensionArgument(base, name) }

    fun addTimeArgument(name: String, minimum: Int = 0): TimeArgument =
        addArgument { TimeArgument(base, name, minimum) }

    fun addMirrorArgument(name: String): MirrorArgument =
        addArgument { MirrorArgument(base, name) }

    fun addStructureRotationArgument(name: String): StructureRotationArgument =
        addArgument { StructureRotationArgument(base, name) }

    fun addHeightMapArgument(name: String): HeightMapArgument =
        addArgument { HeightMapArgument(base, name) }

    fun addLootTableArgument(name: String): LootTableArgument =
        addArgument { LootTableArgument(base, name) }

    fun addUUIDArgument(name: String): UUIDArgument =
        addArgument { UUIDArgument(base, name) }

    fun addOnlinePlayersArgument(name: String): ListArgument<Player> =
        addArgument {
            ListArgument(
                base,
                StringArgument(base, name, StringType.WORD),
                { Bukkit.getOnlinePlayers().toList() },
                { Suggestion.withText(it.name) },
                { Bukkit.getPlayer(it.toString()) },
            )
        }

    fun addOfflinePlayersArgument(name: String): ListArgument<OfflinePlayer> =
        addArgument {
            ListArgument(
                base,
                StringArgument(base, name, StringType.WORD),
                { Bukkit.getOfflinePlayers().toList() },
                { Suggestion.withText(it.name!!) },
                { Bukkit.getOfflinePlayer(it.toString()) },
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