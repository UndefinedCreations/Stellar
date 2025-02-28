@file:Suppress("UnstableApiUsage")

package com.undefined.stellar.v1_21_4

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralStellarArgument
import com.undefined.stellar.argument.basic.*
import com.undefined.stellar.argument.block.BlockDataArgument
import com.undefined.stellar.argument.item.ItemSlotArgument
import com.undefined.stellar.argument.item.ItemSlotsArgument
import com.undefined.stellar.argument.math.AxisArgument
import com.undefined.stellar.argument.misc.NamespacedKeyArgument
import com.undefined.stellar.argument.misc.UUIDArgument
import com.undefined.stellar.argument.scoreboard.DisplaySlotArgument
import com.undefined.stellar.argument.scoreboard.ScoreHolderType
import com.undefined.stellar.argument.structure.MirrorArgument
import com.undefined.stellar.argument.world.HeightMapArgument
import com.undefined.stellar.argument.world.LocationArgument
import com.undefined.stellar.argument.world.LocationType
import com.undefined.stellar.data.argument.EntityAnchor
import com.undefined.stellar.data.argument.Operation
import com.undefined.stellar.exception.LiteralArgumentMismatchException
import com.undefined.stellar.exception.UnsupportedArgumentException
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.*
import net.minecraft.commands.arguments.ResourceOrIdArgument.LootTableArgument
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument
import net.minecraft.commands.arguments.blocks.BlockStateArgument
import net.minecraft.commands.arguments.coordinates.*
import net.minecraft.commands.arguments.item.ItemArgument
import net.minecraft.commands.arguments.item.ItemPredicateArgument
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.block.state.pattern.BlockInWorld
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.structure.Mirror
import org.bukkit.block.structure.StructureRotation
import org.bukkit.craftbukkit.CraftParticle
import org.bukkit.craftbukkit.block.data.CraftBlockData
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import java.util.function.Predicate

@Suppress("DEPRECATION")
object ArgumentAdapter {

    private val COMMAND_BUILD_CONTEXT: CommandBuildContext by lazy {
        CommandBuildContext.simple(
            MinecraftServer.getServer().registryAccess(),
            MinecraftServer.getServer().worldData.dataConfiguration.enabledFeatures()
        )
    }

    fun getLiteralArguments(argument: AbstractStellarArgument<*, *>): List<ArgumentBuilder<CommandSourceStack, *>> {
        val arguments: MutableList<ArgumentBuilder<CommandSourceStack, *>> = mutableListOf()
        for (name in argument.aliases + argument.name)
            arguments.add(LiteralArgumentBuilder.literal(name))
        return arguments
    }

    fun getRequiredArgumentBuilder(argument: AbstractStellarArgument<*, *>): RequiredArgumentBuilder<CommandSourceStack, *> =
        RequiredArgumentBuilder.argument(argument.name, getArgumentType(argument))

    private fun <T : AbstractStellarArgument<*, *>> getArgumentType(argument: T): ArgumentType<*> =
        when (argument) {
            is ListArgument<*, *> -> getArgumentType(argument.type)
            is CustomArgument<*, *> -> getArgumentType(argument.type)
            is StringArgument -> ArgumentHelper.brigadier(argument.type)
            is PhraseArgument -> ArgumentHelper.brigadier(StringType.PHRASE)
            is IntegerArgument -> IntegerArgumentType.integer(argument.min, argument.max)
            is LongArgument -> LongArgumentType.longArg(argument.min, argument.max)
            is FloatArgument -> FloatArgumentType.floatArg(argument.min, argument.max)
            is DoubleArgument -> DoubleArgumentType.doubleArg(argument.min, argument.max)
            is BooleanArgument -> BoolArgumentType.bool()
            is com.undefined.stellar.argument.entity.EntityArgument -> ArgumentHelper.brigadier(argument.type)
            is com.undefined.stellar.argument.player.GameProfileArgument -> GameProfileArgument.gameProfile()
            is LocationArgument -> when (argument.type) {
                LocationType.LOCATION_3D -> BlockPosArgument.blockPos()
                LocationType.LOCATION_2D -> ColumnPosArgument.columnPos()
                LocationType.PRECISE_LOCATION_3D -> Vec3Argument.vec3()
                LocationType.PRECISE_LOCATION_2D -> Vec2Argument.vec2()
            }
            is BlockDataArgument -> BlockStateArgument.block(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.block.BlockPredicateArgument -> BlockPredicateArgument.blockPredicate(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.item.ItemArgument -> ItemArgument.item(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.item.ItemPredicateArgument -> ItemPredicateArgument.itemPredicate(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.text.ColorArgument -> ColorArgument.color()
            is com.undefined.stellar.argument.text.ComponentArgument -> ComponentArgument.textComponent(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.text.StyleArgument -> StyleArgument.style(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.text.MessageArgument -> MessageArgument.message()
            is com.undefined.stellar.argument.scoreboard.ObjectiveArgument -> ObjectiveArgument.objective()
            is com.undefined.stellar.argument.scoreboard.ObjectiveCriteriaArgument -> ObjectiveCriteriaArgument.criteria()
            is com.undefined.stellar.argument.math.OperationArgument -> OperationArgument.operation()
            is com.undefined.stellar.argument.world.ParticleArgument -> ParticleArgument.particle(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.math.AngleArgument -> AngleArgument.angle()
            is com.undefined.stellar.argument.math.RotationArgument -> RotationArgument.rotation()
            is DisplaySlotArgument -> ScoreboardSlotArgument.displaySlot()
            is com.undefined.stellar.argument.scoreboard.ScoreHolderArgument -> when (argument.type) {
                ScoreHolderType.SINGLE -> ScoreHolderArgument.scoreHolder()
                ScoreHolderType.MULTIPLE -> ScoreHolderArgument.scoreHolders()
            }
            is AxisArgument -> SwizzleArgument.swizzle()
            is com.undefined.stellar.argument.scoreboard.TeamArgument -> TeamArgument.team()
            is ItemSlotArgument -> SlotArgument.slot()
            is ItemSlotsArgument -> SlotsArgument.slots()
            is NamespacedKeyArgument -> ResourceLocationArgument.id()
            is com.undefined.stellar.argument.entity.EntityAnchorArgument -> EntityAnchorArgument.anchor()
            is com.undefined.stellar.argument.math.RangeArgument -> RangeArgument.intRange()
            is com.undefined.stellar.argument.world.EnvironmentArgument -> DimensionArgument.dimension()
            is com.undefined.stellar.argument.player.GameModeArgument -> GameModeArgument.gameMode()
            is com.undefined.stellar.argument.math.TimeArgument -> TimeArgument.time(argument.minimum)
            is MirrorArgument -> TemplateMirrorArgument.templateMirror()
            is com.undefined.stellar.argument.structure.StructureRotationArgument -> TemplateRotationArgument.templateRotation()
            is HeightMapArgument -> HeightmapTypeArgument.heightmap()
            is com.undefined.stellar.argument.structure.LootTableArgument -> LootTableArgument.lootTable(COMMAND_BUILD_CONTEXT)
            is UUIDArgument -> UuidArgument.uuid()
            else -> throw UnsupportedArgumentException(argument)
        }

    fun <T : AbstractStellarArgument<*, *>> getParsedArgument(context: CommandContext<CommandSourceStack>, argument: T): Any? {
        return when (argument) {
            is LiteralStellarArgument -> throw LiteralArgumentMismatchException()
            is CustomArgument<*, *> -> argument.parseInternal(CommandContextAdapter.getStellarCommandContext(context), getParsedArgument(context, argument.type))
            is StringArgument -> StringArgumentType.getString(context, argument.name)
            is IntegerArgument -> IntegerArgumentType.getInteger(context, argument.name)
            is LongArgument -> LongArgumentType.getLong(context, argument.name)
            is FloatArgument -> FloatArgumentType.getFloat(context, argument.name)
            is DoubleArgument -> DoubleArgumentType.getDouble(context, argument.name)
            is BooleanArgument -> BoolArgumentType.getBool(context, argument.name)
            is ListArgument<*, *> -> argument.parseInternal(CommandContextAdapter.getStellarCommandContext(context), getParsedArgument(context, argument.type))
            is com.undefined.stellar.argument.entity.EntityArgument -> EntityArgument.getEntities(context, argument.name)
                .map { it.bukkitEntity }.toMutableList()
                .addAll(listOf(EntityArgument.getEntity(context, argument.name).bukkitEntity))
            is com.undefined.stellar.argument.player.GameProfileArgument -> GameProfileArgument.getGameProfiles(context, argument.name)
            is LocationArgument -> ArgumentHelper.getLocation(context, argument)
            is BlockDataArgument -> CraftBlockData.fromData(BlockStateArgument.getBlock(context, argument.name).state)
            is com.undefined.stellar.argument.block.BlockPredicateArgument -> Predicate<Block> { block: Block ->
                BlockPredicateArgument.getBlockPredicate(context, argument.name).test(BlockInWorld(
                    context.source.level,
                    BlockPos(block.x, block.y, block.z), true
                ))
            }
            is com.undefined.stellar.argument.item.ItemArgument -> CraftItemStack.asBukkitCopy(ItemArgument.getItem(context, argument.name).createItemStack(1, false))
            is com.undefined.stellar.argument.item.ItemPredicateArgument -> Predicate<ItemStack> { item: ItemStack ->
                ItemPredicateArgument.getItemPredicate(context, argument.name).test(CraftItemStack.asNMSCopy(item))
            }
            is com.undefined.stellar.argument.text.ColorArgument -> ChatColor.getByChar(ColorArgument.getColor(context, argument.name).char)
            is com.undefined.stellar.argument.text.ComponentArgument ->  GsonComponentSerializer.gson().deserialize(Component.Serializer.toJson(ComponentArgument.getComponent(context, argument.name), COMMAND_BUILD_CONTEXT))
            is com.undefined.stellar.argument.text.StyleArgument ->  GsonComponentSerializer.gson().deserialize(
                ArgumentHelper.getArgumentInput(context, argument.name) ?: return null).style()
            is com.undefined.stellar.argument.text.MessageArgument ->  GsonComponentSerializer.gson().deserialize(Component.Serializer.toJson(MessageArgument.getMessage(context, argument.name), COMMAND_BUILD_CONTEXT))
            is com.undefined.stellar.argument.scoreboard.ObjectiveArgument ->  Bukkit.getScoreboardManager().mainScoreboard.getObjective(ObjectiveArgument.getObjective(context, argument.name).name)
            is com.undefined.stellar.argument.scoreboard.ObjectiveCriteriaArgument ->  ObjectiveCriteriaArgument.getCriteria(context, argument.name).name
            is com.undefined.stellar.argument.math.OperationArgument -> Operation.getOperation(ArgumentHelper.getArgumentInput(context, argument.name) ?: return null)
            is com.undefined.stellar.argument.world.ParticleArgument ->  {
                val particleOptions = ParticleArgument.getParticle(context, argument.name)
                ArgumentHelper.getParticleData(context, CraftParticle.minecraftToBukkit(particleOptions.type), particleOptions)
            }
            is com.undefined.stellar.argument.math.AngleArgument -> AngleArgument.getAngle(context, argument.name)
            is com.undefined.stellar.argument.math.RotationArgument -> {
                val coordinates = RotationArgument.getRotation(context, argument.name)
                val position = coordinates.getPosition(context.source)
                val rotation = coordinates.getRotation(context.source)
                Location(context.source.level.world, position.x, position.y, position.z, rotation.x, rotation.y)
            }
            is DisplaySlotArgument -> ArgumentHelper.getBukkitDisplaySlot(ScoreboardSlotArgument.getDisplaySlot(context, argument.name))
            is com.undefined.stellar.argument.scoreboard.ScoreHolderArgument -> when (argument.type) {
                ScoreHolderType.SINGLE -> ScoreHolderArgument.getName(context, argument.name).scoreboardName
                ScoreHolderType.MULTIPLE -> ScoreHolderArgument.getNames(context, argument.name).map { it.scoreboardName }
            }
            is AxisArgument -> ArgumentHelper.getBukkitAxis(SwizzleArgument.getSwizzle(context, argument.name))
            is com.undefined.stellar.argument.scoreboard.TeamArgument -> Bukkit.getScoreboardManager().mainScoreboard.getTeam(TeamArgument.getTeam(context, argument.name).name)
            is ItemSlotArgument -> SlotArgument.getSlot(context, argument.name)
            is ItemSlotsArgument -> SlotsArgument.getSlots(context, argument.name).slots().toList()
            is NamespacedKeyArgument -> NamespacedKey(ResourceLocationArgument.getId(context, argument.name).namespace, ResourceLocationArgument.getId(context, argument.name).path)
            is com.undefined.stellar.argument.entity.EntityAnchorArgument -> EntityAnchor.getFromName(ArgumentHelper.getArgumentInput(context, argument.name) ?: return null)
            is com.undefined.stellar.argument.math.RangeArgument -> {
                val range = RangeArgument.Ints.getRange(context, argument.name)
                IntRange(range.min.orElse(1), range.max.orElse(2))
            }
            is com.undefined.stellar.argument.world.EnvironmentArgument -> DimensionArgument.getDimension(context, argument.name).world.environment
            is com.undefined.stellar.argument.player.GameModeArgument -> GameMode.getByValue(GameModeArgument.getGameMode(context, argument.name).id)
            is com.undefined.stellar.argument.math.TimeArgument -> IntegerArgumentType.getInteger(context, argument.name).toLong()
            is MirrorArgument -> Mirror.valueOf(TemplateMirrorArgument.getMirror(context, argument.name).name)
            is com.undefined.stellar.argument.structure.StructureRotationArgument -> StructureRotation.valueOf(TemplateRotationArgument.getRotation(context, argument.name).name)
            is HeightMapArgument -> HeightMap.valueOf(HeightmapTypeArgument.getHeightmap(context, argument.name).name)
            is com.undefined.stellar.argument.structure.LootTableArgument -> LootTableArgument.getLootTable(context, argument.name).value().craftLootTable
            is UUIDArgument -> UuidArgument.getUuid(context, argument.name)
            else -> throw UnsupportedArgumentException(argument)
        }
    }

}