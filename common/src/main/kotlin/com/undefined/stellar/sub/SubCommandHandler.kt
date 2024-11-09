package com.undefined.stellar.sub

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.sub.brigadier.entity.EntityDisplayType
import com.undefined.stellar.sub.brigadier.entity.EntitySubCommand
import com.undefined.stellar.sub.brigadier.item.ItemPredicateSubCommand
import com.undefined.stellar.sub.brigadier.item.ItemSubCommand
import com.undefined.stellar.sub.brigadier.text.ColorSubCommand
import com.undefined.stellar.sub.brigadier.player.GameProfileSubCommand
import com.undefined.stellar.sub.brigadier.primitive.*
import com.undefined.stellar.sub.brigadier.text.ComponentSubCommand
import com.undefined.stellar.sub.brigadier.text.StyleSubCommand
import com.undefined.stellar.sub.brigadier.world.BlockPredicateSubCommand
import com.undefined.stellar.sub.brigadier.world.BlockDataSubCommand
import com.undefined.stellar.sub.brigadier.world.LocationSubCommand
import com.undefined.stellar.sub.brigadier.world.LocationType
import com.undefined.stellar.sub.custom.EnumSubCommand
import com.undefined.stellar.sub.custom.ListSubCommand
import java.util.UUID

open class SubCommandHandler {

    open fun getBase(): BaseStellarCommand<*>? = null
    val subCommands: MutableList<AbstractStellarSubCommand<*>> = mutableListOf()
    val optionalSubCommands: MutableList<AbstractStellarSubCommand<*>> = mutableListOf()

    fun addSubCommand(name: String): StellarSubCommand {
        val subCommand = StellarSubCommand(getBase()!!, name)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addStringSubCommand(name: String, type: StringType = StringType.SINGLE_WORD): StringSubCommand {
        val subCommand = StringSubCommand(getBase()!!, name, type)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addIntegerSubCommand(name: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): IntegerSubCommand {
        val subCommand = IntegerSubCommand(getBase()!!, name, min, max)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addLongSubCommand(name: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): LongSubCommand {
        val subCommand = LongSubCommand(getBase()!!, name, min, max)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addFloatSubCommand(name: String, min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE): FloatSubCommand {
        val subCommand = FloatSubCommand(getBase()!!, name, min, max)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addDoubleSubCommand(name: String, min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE): DoubleSubCommand {
        val subCommand = DoubleSubCommand(getBase()!!, name, min, max)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addBooleanSubCommand(name: String): BooleanSubCommand {
        val subCommand = BooleanSubCommand(getBase()!!, name)
        subCommands.add(subCommand)
        return subCommand
    }

    fun <T> addListSubCommand(name: String, list: List<T>, parse: (String) -> T): ListSubCommand<T> {
        val subCommand = ListSubCommand(getBase()!!, name, list, parse = parse)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addStringListSubCommand(name: String, list: List<String>): ListSubCommand<String> {
        val subCommand = ListSubCommand(getBase()!!, name, list) { it }
        subCommands.add(subCommand)
        return subCommand
    }

    fun addUUIDListSubCommand(name: String, list: List<UUID>): ListSubCommand<UUID> {
        val subCommand = ListSubCommand<UUID>(getBase()!!, name, list) { UUID.fromString(it) }
        subCommands.add(subCommand)
        return subCommand
    }

    inline fun <reified T : Enum<T>> addEnumSubCommand(name: String): EnumSubCommand<T> {
        val subCommand = EnumSubCommand<T>(getBase()!!, name, T::class)
        subCommands.add(subCommand)
        return subCommand
    }

    inline fun <reified T : Enum<T>> addEnumSubCommand(
        name: String,
        noinline stringifier: (Enum<*>) -> String = { it.name },
        noinline parse: (String) -> Enum<T>?
    ): EnumSubCommand<T> {
        val subCommand = EnumSubCommand(getBase()!!, name, T::class, stringifier, parse)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addEntitySubCommand(name: String, type: EntityDisplayType): EntitySubCommand {
        val subCommand = EntitySubCommand(getBase()!!, name, type)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addGameProfileSubCommand(name: String): GameProfileSubCommand {
        val subCommand = GameProfileSubCommand(getBase()!!, name)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addLocationSubCommand(name: String, type: LocationType = LocationType.LOCATION3D): LocationSubCommand {
        val subCommand = LocationSubCommand(getBase()!!, name, type)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addBlockDataSubCommand(name: String): BlockDataSubCommand {
        val subCommand = BlockDataSubCommand(getBase()!!, name)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addBlockPredicateSubCommand(name: String): BlockPredicateSubCommand {
        val subCommand = BlockPredicateSubCommand(getBase()!!, name)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addItemSubCommand(name: String): ItemSubCommand {
        val subCommand = ItemSubCommand(getBase()!!, name)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addItemPredicateSubCommand(name: String): ItemPredicateSubCommand {
        val subCommand = ItemPredicateSubCommand(getBase()!!, name)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addColorSubCommand(name: String): ColorSubCommand {
        val subCommand = ColorSubCommand(getBase()!!, name)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addComponentSubCommand(name: String): ComponentSubCommand {
        val subCommand = ComponentSubCommand(getBase()!!, name)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addStyleSubCommand(name: String): StyleSubCommand {
        val subCommand = StyleSubCommand(getBase()!!, name)
        subCommands.add(subCommand)
        return subCommand
    }

}