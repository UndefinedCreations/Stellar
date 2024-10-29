package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.undefined.stellar.exception.UnsupportedSubCommandException
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import com.undefined.stellar.sub.brigadier.entity.EntityDisplayType
import com.undefined.stellar.sub.brigadier.entity.EntitySubCommand
import com.undefined.stellar.sub.brigadier.player.GameProfileSubCommand
import com.undefined.stellar.sub.brigadier.primitive.*
import com.undefined.stellar.sub.brigadier.world.LocationSubCommand
import com.undefined.stellar.sub.brigadier.world.LocationType
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.GameProfileArgument
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument
import net.minecraft.commands.arguments.coordinates.Vec2Argument
import net.minecraft.commands.arguments.coordinates.Vec3Argument
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ColumnPos
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import org.bukkit.Location
import org.bukkit.World

object ArgumentHelper {

    fun <T : NativeTypeSubCommand<*>> nativeSubCommandToArgument(subCommand: T): RequiredArgumentBuilder<CommandSourceStack, *> =
        when (subCommand) {
            is StringSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, subCommand.type.brigadier())
            is IntegerSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, IntegerArgumentType.integer(subCommand.min, subCommand.max))
            is LongSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, LongArgumentType.longArg(subCommand.min, subCommand.max))
            is FloatSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, FloatArgumentType.floatArg(subCommand.min, subCommand.max))
            is DoubleSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, DoubleArgumentType.doubleArg(subCommand.min, subCommand.max))
            is BooleanSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, BoolArgumentType.bool())
            is ListSubCommand<*> -> RequiredArgumentBuilder.argument<CommandSourceStack, String>(subCommand.name, StringArgumentType.word()).suggestStringList(subCommand.getStringList())
            is EnumSubCommand<*> -> RequiredArgumentBuilder.argument<CommandSourceStack, String>(subCommand.name, StringArgumentType.word()).suggestStringList(subCommand.getStringList())
            is EntitySubCommand -> RequiredArgumentBuilder.argument(subCommand.name, subCommand.type.brigadier())
            is GameProfileSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, GameProfileArgument.gameProfile())
            is LocationSubCommand -> when (subCommand.type) {
                    LocationType.LOCATION3D -> RequiredArgumentBuilder.argument(subCommand.name, BlockPosArgument.blockPos())
                    LocationType.LOCATION2D -> RequiredArgumentBuilder.argument(subCommand.name, ColumnPosArgument.columnPos())
                    LocationType.DOUBLE_LOCATION_3D -> RequiredArgumentBuilder.argument(subCommand.name, Vec3Argument.vec3())
                    LocationType.DOUBLE_LOCATION_2D -> RequiredArgumentBuilder.argument(subCommand.name, Vec2Argument.vec2())
                }
            else -> throw UnsupportedSubCommandException()
        }

    fun <T : NativeTypeSubCommand<*>> handleNativeSubCommandExecutors(subCommand: T, context: CommandContext<CommandSourceStack>) =
        when (subCommand) {
            is StringSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, StringArgumentType.getString(context, subCommand.name)) }
            is IntegerSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, IntegerArgumentType.getInteger(context, subCommand.name)) }
            is FloatSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, FloatArgumentType.getFloat(context, subCommand.name)) }
            is DoubleSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, DoubleArgumentType.getDouble(context, subCommand.name)) }
            is BooleanSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, BoolArgumentType.getBool(context, subCommand.name)) }
            is ListSubCommand<*> -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, subCommand.parse.invoke(StringArgumentType.getString(context, subCommand.name))!!) }
            is EnumSubCommand<*> -> subCommand.customExecutions.forEach { execution ->
                val enum = subCommand.parse(StringArgumentType.getString(context, subCommand.name))
                enum?.let { execution.run(context.source.bukkitSender, enum) }
            }
            is EntitySubCommand -> {
                subCommand.pluralEntitiesExecutions.forEach {
                    it.run(context.source.bukkitSender, EntityArgument.getEntities(context, subCommand.name).map { it.bukkitEntity })
                }
                subCommand.singularEntityExecutions.forEach {
                    it.run(context.source.bukkitSender, EntityArgument.getEntity(context, subCommand.name).bukkitEntity)
                }
            }
            is GameProfileSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, GameProfileArgument.getGameProfiles(context, subCommand.name)) }
            is LocationSubCommand -> subCommand.customExecutions.forEach {
                val location = getLocation(context, subCommand)
                it.run(context.source.bukkitSender, location)
            }
            else -> throw UnsupportedSubCommandException()
        }

    fun <T : NativeTypeSubCommand<*>> handleNativeSubCommandRunnables(subCommand: T, context: CommandContext<CommandSourceStack>): Boolean {
        when (subCommand) {
            is StringSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, StringArgumentType.getString(context, subCommand.name))) return false }
            is IntegerSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, IntegerArgumentType.getInteger(context, subCommand.name))) return false }
            is FloatSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, FloatArgumentType.getFloat(context, subCommand.name))) return false }
            is DoubleSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, DoubleArgumentType.getDouble(context, subCommand.name))) return false }
            is BooleanSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, BoolArgumentType.getBool(context, subCommand.name))) return false }
            is ListSubCommand<*> -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, subCommand.parse.invoke(StringArgumentType.getString(context, subCommand.name))!!)) return false }
            is EnumSubCommand<*> -> subCommand.customRunnables.forEach { runnable ->
                val enum = subCommand.parse(StringArgumentType.getString(context, subCommand.name))
                enum?.let { if (!runnable.run(context.source.bukkitSender, enum)) return false }
            }
            is EntitySubCommand -> {
                subCommand.pluralEntitiesRunnables.forEach { runnable ->
                    if (!runnable.run(context.source.bukkitSender, EntityArgument.getEntities(context, subCommand.name).map { it.bukkitEntity })) return false
                }
                subCommand.singularEntityRunnables.forEach { runnable ->
                    if (!runnable.run(context.source.bukkitSender, EntityArgument.getEntity(context, subCommand.name).bukkitEntity)) return false
                }
            }
            is GameProfileSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, GameProfileArgument.getGameProfiles(context, subCommand.name))) return false }
            is LocationSubCommand -> subCommand.customRunnables.forEach {
                val blockPos = BlockPosArgument.getBlockPos(context, subCommand.name)
                if (!it.run(context.source.bukkitSender, Location(context.source.bukkitWorld, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble()))) return false
            }
            else -> throw UnsupportedSubCommandException()
        }
        return true
    }


    private fun StringType.brigadier(): StringArgumentType = when (this) {
        StringType.SINGLE_WORD -> StringArgumentType.word()
        StringType.QUOTABLE_PHRASE -> StringArgumentType.string()
        StringType.GREEDY_PHRASE -> StringArgumentType.greedyString()
    }

    private fun EntityDisplayType.brigadier(): EntityArgument = when (this) {
        EntityDisplayType.ENTITY -> EntityArgument.entity()
        EntityDisplayType.ENTITIES -> EntityArgument.entities()
        EntityDisplayType.PLAYER -> EntityArgument.player()
        EntityDisplayType.PLAYERS -> EntityArgument.players()
    }

    private fun RequiredArgumentBuilder<CommandSourceStack, *>.suggestStringList(list: List<String>) =
        suggests { _: CommandContext<CommandSourceStack>, suggestionsBuilder: SuggestionsBuilder ->
            list.filter { it.startsWith(suggestionsBuilder.remaining) }.forEach { suggestionsBuilder.suggest(it) }
            return@suggests suggestionsBuilder.buildFuture()
        }

    private fun getLocation(context: CommandContext<CommandSourceStack>, command: LocationSubCommand): Location {
        val world = context.source.bukkitWorld
        return when (command.type) {
            LocationType.LOCATION3D -> BlockPosArgument.getBlockPos(context, command.name).toLocation(world)
            LocationType.LOCATION2D -> ColumnPosArgument.getColumnPos(context, command.name).toLocation(world)
            LocationType.DOUBLE_LOCATION_3D -> Vec3Argument.getVec3(context, command.name).toLocation(world)
            LocationType.DOUBLE_LOCATION_2D -> Vec2Argument.getVec2(context, command.name).toLocation(world)
        }
    }

    private fun BlockPos.toLocation(world: World?) = Location(world, x.toDouble(), y.toDouble(), z.toDouble())

    private fun ColumnPos.toLocation(world: World?) = Location(world, x.toDouble(), 0.0, z.toDouble())

    private fun Vec3.toLocation(world: World?) = Location(world, x, y, z)

    private fun Vec2.toLocation(world: World?) = Location(world, x.toDouble(), 0.0, y.toDouble())

}