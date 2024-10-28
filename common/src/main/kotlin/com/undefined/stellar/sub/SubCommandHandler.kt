package com.undefined.stellar.sub

import com.undefined.stellar.BaseStellarCommand
import com.undefined.stellar.sub.brigadier.entity.EntityDisplayType
import com.undefined.stellar.sub.brigadier.entity.EntitySubCommand
import com.undefined.stellar.sub.brigadier.player.GameProfileSubCommand
import com.undefined.stellar.sub.brigadier.primitive.*
import com.undefined.stellar.sub.brigadier.world.LocationSubCommand
import java.util.UUID

open class SubCommandHandler {

    open fun getThis(): BaseStellarCommand<*>? = null
    val subCommands: MutableList<BaseStellarSubCommand<*>> = mutableListOf()
    val optionalSubCommands: MutableList<BaseStellarSubCommand<*>> = mutableListOf()

    fun addSubCommand(name: String): StellarSubCommand {
        val subCommand = StellarSubCommand(getThis()!!, name)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addStringSubCommand(name: String, type: StringType = StringType.SINGLE_WORD): StringSubCommand {
        val subCommand = StringSubCommand(getThis()!!, name, type)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addIntegerSubCommand(name: String, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): IntegerSubCommand {
        val subCommand = IntegerSubCommand(getThis()!!, name, min, max)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addLongSubCommand(name: String, min: Long = Long.MIN_VALUE, max: Long = Long.MAX_VALUE): LongSubCommand {
        val subCommand = LongSubCommand(getThis()!!, name, min, max)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addFloatSubCommand(name: String, min: Float = Float.MIN_VALUE, max: Float = Float.MAX_VALUE): FloatSubCommand {
        val subCommand = FloatSubCommand(getThis()!!, name, min, max)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addDoubleSubCommand(name: String, min: Double = Double.MIN_VALUE, max: Double = Double.MAX_VALUE): DoubleSubCommand {
        val subCommand = DoubleSubCommand(getThis()!!, name, min, max)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addBooleanSubCommand(name: String): BooleanSubCommand {
        val subCommand = BooleanSubCommand(getThis()!!, name)
        subCommands.add(subCommand)
        return subCommand
    }

    fun <T> addListSubCommand(name: String, list: List<T>, parse: (String) -> T): ListSubCommand<T> {
        val subCommand = ListSubCommand(getThis()!!, name, list, parse = parse)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addStringListSubCommand(name: String, list: List<String>): ListSubCommand<String> {
        val subCommand = ListSubCommand(getThis()!!, name, list) { it }
        subCommands.add(subCommand)
        return subCommand
    }

    fun addUUIDListSubCommand(name: String, list: List<UUID>): ListSubCommand<UUID> {
        val subCommand = ListSubCommand<UUID>(getThis()!!, name, list) { UUID.fromString(it) }
        subCommands.add(subCommand)
        return subCommand
    }

    inline fun <reified T : Enum<T>> addEnumSubCommand(name: String): EnumSubCommand<T> {
        val subCommand = EnumSubCommand<T>(getThis()!!, name, T::class)
        subCommands.add(subCommand)
        return subCommand
    }

    inline fun <reified T : Enum<T>> addEnumSubCommand(
        name: String,
        noinline stringifier: (Enum<*>) -> String = { it.name },
        noinline parse: (String) -> Enum<T>
    ): EnumSubCommand<T> {
        val subCommand = EnumSubCommand(getThis()!!, name, T::class, stringifier, parse)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addEntitySubCommand(name: String, type: EntityDisplayType): EntitySubCommand {
        val subCommand = EntitySubCommand(getThis()!!, name, type)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addGameProfileSubCommand(name: String): GameProfileSubCommand {
        val subCommand = GameProfileSubCommand(getThis()!!, name)
        subCommands.add(subCommand)
        return subCommand
    }

    fun addLocationSubCommand(name: String): LocationSubCommand {
        val subCommand = LocationSubCommand(getThis()!!, name)
        subCommands.add(subCommand)
        return subCommand
    }

}