package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.undefined.stellar.exception.UnsupportedSubCommandException
import com.undefined.stellar.sub.BaseStellarSubCommand
import com.undefined.stellar.sub.brigadier.NativeTypeSubCommand
import com.undefined.stellar.sub.brigadier.entity.EntityDisplayType
import com.undefined.stellar.sub.brigadier.entity.EntitySubCommand
import com.undefined.stellar.sub.brigadier.player.GameProfileSubCommand
import com.undefined.stellar.sub.brigadier.primitive.*
import com.undefined.stellar.sub.brigadier.world.LocationSubCommand
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.GameProfileArgument
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
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
            is EnumSubCommand<*> -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, subCommand.parse(StringArgumentType.getString(context, subCommand.name))) }
            is EntitySubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, EntityArgument.getEntity(context, subCommand.name).bukkitEntity) }
            is GameProfileSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, GameProfileArgument.getGameProfiles(context, subCommand.name)) }
            is LocationSubCommand -> subCommand.customExecutions.forEach {
                val blockPos = BlockPosArgument.getBlockPos(context, subCommand.name)
                it.run(context.source.bukkitSender, Location(context.source.bukkitWorld, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble()))
            }
            else -> throw UnsupportedSubCommandException()
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