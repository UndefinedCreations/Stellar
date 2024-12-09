package com.undefined.stellar.argument

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.argument.types.custom.CustomArgument
import com.undefined.stellar.argument.types.custom.EnumArgument
import com.undefined.stellar.argument.types.custom.ListArgument
import com.undefined.stellar.argument.types.custom.OnlinePlayersArgument
import com.undefined.stellar.argument.types.entity.EntityAnchorArgument
import com.undefined.stellar.argument.types.entity.EntityArgument
import com.undefined.stellar.argument.types.entity.EntityDisplayType
import com.undefined.stellar.argument.types.item.ItemArgument
import com.undefined.stellar.argument.types.item.ItemPredicateArgument
import com.undefined.stellar.argument.types.item.ItemSlotArgument
import com.undefined.stellar.argument.types.item.ItemSlotsArgument
import com.undefined.stellar.argument.types.math.*
import com.undefined.stellar.argument.types.misc.NamespacedKeyArgument
import com.undefined.stellar.argument.types.misc.UUIDArgument
import com.undefined.stellar.argument.types.player.GameModeArgument
import com.undefined.stellar.argument.types.player.GameProfileArgument
import com.undefined.stellar.argument.types.primitive.*
import com.undefined.stellar.argument.types.scoreboard.*
import com.undefined.stellar.argument.types.structure.LootTableArgument
import com.undefined.stellar.argument.types.structure.MirrorArgument
import com.undefined.stellar.argument.types.structure.StructureRotationArgument
import com.undefined.stellar.argument.types.text.ColorArgument
import com.undefined.stellar.argument.types.text.ComponentArgument
import com.undefined.stellar.argument.types.text.MessageArgument
import com.undefined.stellar.argument.types.text.StyleArgument
import com.undefined.stellar.argument.types.world.*
import org.bukkit.Bukkit
import java.util.*

open class ArgumentHandler {

    open val base: AbstractStellarCommand<*> get() = throw IllegalStateException("Cannot access the getter from the property base when it hasn't been overrided!")
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

    fun <T> addCustomArgument(Argument: CustomArgument<T>): CustomArgument<T> {
        addArgument(Argument)
        return Argument
    }

    fun addStringArgument(name: String, type: StringType = StringType.SINGLE_WORD): StringArgument =
        addArgument { StringArgument(base, name, type) }

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

    fun <T> addListArgument(name: String, list: List<T>, parse: (String) -> T): ListArgument<T> =
        addArgument { ListArgument(base, name, list, parse = parse) }

    fun addStringListArgument(name: String, list: List<String>): ListArgument<String> =
        addArgument { ListArgument(base, name, list) { it } }

    fun addUUIDListArgument(name: String, list: List<UUID>): ListArgument<UUID> =
        addArgument { ListArgument(base, name, list) { UUID.fromString(it) } }

    inline fun <reified T : Enum<T>> addEnumArgument(name: String): EnumArgument<T> =
        addArgument { EnumArgument<T>(base, name, T::class) }

    inline fun <reified T : Enum<T>> addEnumArgument(
        name: String,
        noinline stringifier: (Enum<*>) -> String = { it.name },
        noinline parse: (String) -> Enum<T>?
    ): EnumArgument<T> = addArgument { EnumArgument(base, name, T::class, stringifier, parse) }

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

    fun addDimensionSubCommand(name: String): DimensionArgument =
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

    fun addOnlinePlayersArgument(name: String): OnlinePlayersArgument =
        addArgument { OnlinePlayersArgument(base, name) { Bukkit.getOnlinePlayers().toList() } }

    fun addGameEventArgument(name: String): GameEventArgument =
        addArgument { GameEventArgument(base, name) }

}