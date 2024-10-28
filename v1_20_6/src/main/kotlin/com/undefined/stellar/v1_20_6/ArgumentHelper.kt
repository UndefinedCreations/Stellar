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
import com.undefined.stellar.sub.brigadier.world.Location2DSubCommand
import com.undefined.stellar.sub.brigadier.world.LocationSubCommand
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.GameProfileArgument
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument
import net.minecraft.commands.synchronization.ArgumentTypeInfos
import org.bukkit.Location

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
            is LocationSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, BlockPosArgument.blockPos())
            is Location2DSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, ColumnPosArgument.columnPos())
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
                val blockPos = BlockPosArgument.getBlockPos(context, subCommand.name)
                it.run(context.source.bukkitSender, Location(context.source.bukkitWorld, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble()))
            }
            is Location2DSubCommand -> subCommand.customExecutions.forEach {
                val columnPos = BlockPosArgument.getBlockPos(context, subCommand.name)
                it.run(context.source.bukkitSender, Location(context.source.bukkitWorld, columnPos.x.toDouble(), columnPos.y.toDouble(), columnPos.z.toDouble()))
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

}