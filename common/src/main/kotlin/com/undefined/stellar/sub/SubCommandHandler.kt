package com.undefined.stellar.sub

import com.undefined.stellar.AbstractStellarCommand
import com.undefined.stellar.sub.brigadier.entity.EntityAnchorSubCommand
import com.undefined.stellar.sub.brigadier.entity.EntityDisplayType
import com.undefined.stellar.sub.brigadier.entity.EntitySubCommand
import com.undefined.stellar.sub.brigadier.item.ItemPredicateSubCommand
import com.undefined.stellar.sub.brigadier.item.ItemSlotSubCommand
import com.undefined.stellar.sub.brigadier.item.ItemSlotsSubCommand
import com.undefined.stellar.sub.brigadier.item.ItemSubCommand
import com.undefined.stellar.sub.brigadier.math.*
import com.undefined.stellar.sub.brigadier.misc.NamespacedKeySubCommand
import com.undefined.stellar.sub.brigadier.misc.UUIDSubCommand
import com.undefined.stellar.sub.brigadier.player.GameModeSubCommand
import com.undefined.stellar.sub.brigadier.player.GameProfileSubCommand
import com.undefined.stellar.sub.brigadier.primitive.*
import com.undefined.stellar.sub.brigadier.scoreboard.*
import com.undefined.stellar.sub.brigadier.structure.LootTableSubCommand
import com.undefined.stellar.sub.brigadier.structure.MirrorSubCommand
import com.undefined.stellar.sub.brigadier.structure.StructureRotationSubCommand
import com.undefined.stellar.sub.brigadier.text.*
import com.undefined.stellar.sub.brigadier.world.*
import com.undefined.stellar.sub.custom.CustomSubCommand
import com.undefined.stellar.sub.custom.EnumSubCommand
import com.undefined.stellar.sub.custom.ListSubCommand
import com.undefined.stellar.sub.custom.OnlinePlayersSubCommand
import org.bukkit.Bukkit
import java.util.UUID

open class SubCommandHandler {

    open fun getBase(): AbstractStellarCommand<*>? = null
    private val _subCommands: MutableList<AbstractStellarSubCommand<*>> = mutableListOf()
    val subCommands: List<AbstractStellarSubCommand<*>>
        get() {
            val base = getBase()
            if (base is CustomSubCommand<*>) return _subCommands + base.getSubCommandsList()
            return _subCommands
        }
    val optionalSubCommands: MutableList<BaseStellarSubCommand<*>> = mutableListOf()

    fun addSubCommand(subCommand: AbstractStellarSubCommand<*>): AbstractStellarSubCommand<*> {
        _subCommands.add(subCommand)
        return subCommand
    }

    fun addSubCommand(name: String): StellarSubCommand {
        val subCommand = StellarSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun <T> addCustomSubCommand(subCommand: CustomSubCommand<T>): CustomSubCommand<T> {
        addSubCommand(subCommand)
        return subCommand
    }

    fun addStringSubCommand(name: String, type: StringType = StringType.SINGLE_WORD): StringSubCommand {
        val subCommand = StringSubCommand(getBase()!!, name, type)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addIntegerSubCommand(name: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): IntegerSubCommand {
        val subCommand = IntegerSubCommand(getBase()!!, name, min, max)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addLongSubCommand(name: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): LongSubCommand {
        val subCommand = LongSubCommand(getBase()!!, name, min, max)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addFloatSubCommand(name: String, min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE): FloatSubCommand {
        val subCommand = FloatSubCommand(getBase()!!, name, min, max)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addDoubleSubCommand(name: String, min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE): DoubleSubCommand {
        val subCommand = DoubleSubCommand(getBase()!!, name, min, max)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addBooleanSubCommand(name: String): BooleanSubCommand {
        val subCommand = BooleanSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun <T> addListSubCommand(name: String, list: List<T>, parse: (String) -> T): ListSubCommand<T> {
        val subCommand = ListSubCommand(getBase()!!, name, list, parse = parse)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addStringListSubCommand(name: String, list: List<String>): ListSubCommand<String> {
        val subCommand = ListSubCommand(getBase()!!, name, list) { it }
        addSubCommand(subCommand)
        return subCommand
    }

    fun addUUIDListSubCommand(name: String, list: List<UUID>): ListSubCommand<UUID> {
        val subCommand = ListSubCommand(getBase()!!, name, list) { UUID.fromString(it) }
        addSubCommand(subCommand)
        return subCommand
    }

    inline fun <reified T : Enum<T>> addEnumSubCommand(name: String): EnumSubCommand<T> {
        val subCommand = EnumSubCommand<T>(getBase()!!, name, T::class)
        addSubCommand(subCommand)
        return subCommand
    }

    inline fun <reified T : Enum<T>> addEnumSubCommand(
        name: String,
        noinline stringifier: (Enum<*>) -> String = { it.name },
        noinline parse: (String) -> Enum<T>?
    ): EnumSubCommand<T> {
        val subCommand = EnumSubCommand(getBase()!!, name, T::class, stringifier, parse)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addEntitySubCommand(name: String, type: EntityDisplayType): EntitySubCommand {
        val subCommand = EntitySubCommand(getBase()!!, name, type)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addGameProfileSubCommand(name: String): GameProfileSubCommand {
        val subCommand = GameProfileSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addLocationSubCommand(name: String, type: LocationType = LocationType.LOCATION3D): LocationSubCommand {
        val subCommand = LocationSubCommand(getBase()!!, name, type)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addBlockDataSubCommand(name: String): BlockDataSubCommand {
        val subCommand = BlockDataSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addBlockPredicateSubCommand(name: String): BlockPredicateSubCommand {
        val subCommand = BlockPredicateSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addItemSubCommand(name: String): ItemSubCommand {
        val subCommand = ItemSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addItemPredicateSubCommand(name: String): ItemPredicateSubCommand {
        val subCommand = ItemPredicateSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addColorSubCommand(name: String): ColorSubCommand {
        val subCommand = ColorSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addComponentSubCommand(name: String): ComponentSubCommand {
        val subCommand = ComponentSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addStyleSubCommand(name: String): StyleSubCommand {
        val subCommand = StyleSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addMessageSubCommand(name: String): MessageSubCommand {
        val subCommand = MessageSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addObjectiveSubCommand(name: String): ObjectiveSubCommand {
        val subCommand = ObjectiveSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addObjectiveCriteriaSubCommand(name: String): ObjectiveCriteriaSubCommand {
        val subCommand = ObjectiveCriteriaSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addOperationSubCommand(name: String): OperationSubCommand {
        val subCommand = OperationSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addParticleSubCommand(name: String): ParticleSubCommand {
        val subCommand = ParticleSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addAngleSubCommand(name: String): AngleSubCommand {
        val subCommand = AngleSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addRotationSubCommand(name: String): RotationSubCommand {
        val subCommand = RotationSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addDisplaySlotSubCommand(name: String): DisplaySlotSubCommand {
        val subCommand = DisplaySlotSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addScoreHolderSubCommand(name: String): ScoreHolderSubCommand {
        val subCommand = ScoreHolderSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addScoreHoldersSubCommand(name: String): ScoreHoldersSubCommand {
        val subCommand = ScoreHoldersSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addAxisSubCommand(name: String): AxisSubCommand {
        val subCommand = AxisSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addTeamSubCommand(name: String): TeamSubCommand {
        val subCommand = TeamSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addItemSlotSubCommand(name: String): ItemSlotSubCommand {
        val subCommand = ItemSlotSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addItemSlotsSubCommand(name: String): ItemSlotsSubCommand {
        val subCommand = ItemSlotsSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addNamespacedKeySubCommand(name: String): NamespacedKeySubCommand {
        val subCommand = NamespacedKeySubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addEntityAnchorSubCommand(name: String): EntityAnchorSubCommand {
        val subCommand = EntityAnchorSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addRangeSubCommand(name: String): RangeSubCommand {
        val subCommand = RangeSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addGameModeSubCommand(name: String): GameModeSubCommand {
        val subCommand = GameModeSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addDimensionSubCommand(name: String): DimensionSubCommand {
        val subCommand = DimensionSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addTimeSubCommand(name: String, minimum: Int = 0): TimeSubCommand {
        val subCommand = TimeSubCommand(getBase()!!, name, minimum)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addMirrorSubCommand(name: String): MirrorSubCommand {
        val subCommand = MirrorSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addStructureRotationSubCommand(name: String): StructureRotationSubCommand {
        val subCommand = StructureRotationSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addHeightMapSubCommand(name: String): HeightMapSubCommand {
        val subCommand = HeightMapSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addLootTableSubCommand(name: String): LootTableSubCommand {
        val subCommand = LootTableSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addUUIDSubCommand(name: String): UUIDSubCommand {
        val subCommand = UUIDSubCommand(getBase()!!, name)
        addSubCommand(subCommand)
        return subCommand
    }

    fun addOnlinePlayersSubCommand(name: String): OnlinePlayersSubCommand {
        val subCommand = OnlinePlayersSubCommand(getBase()!!, name) { Bukkit.getOnlinePlayers().toList() }
        addSubCommand(subCommand)
        return subCommand
    }

}