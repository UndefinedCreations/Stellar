@file:Suppress("DEPRECATION")

package com.undefined.stellar.v1_17

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.ParsedArgument
import com.mojang.brigadier.context.StringRange
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralStellarArgument
import com.undefined.stellar.argument.basic.*
import com.undefined.stellar.argument.block.BlockDataArgument
import com.undefined.stellar.argument.entity.EntityDisplayType
import com.undefined.stellar.argument.item.ItemSlotArgument
import com.undefined.stellar.argument.item.ItemSlotsArgument
import com.undefined.stellar.argument.misc.NamespacedKeyArgument
import com.undefined.stellar.argument.misc.UUIDArgument
import com.undefined.stellar.argument.player.GameModeArgument
import com.undefined.stellar.argument.structure.LootTableArgument
import com.undefined.stellar.argument.structure.MirrorArgument
import com.undefined.stellar.argument.world.EnvironmentArgument
import com.undefined.stellar.argument.world.HeightMapArgument
import com.undefined.stellar.argument.world.LocationArgument
import com.undefined.stellar.argument.world.LocationType
import com.undefined.stellar.data.argument.EntityAnchor
import com.undefined.stellar.data.argument.Operation
import com.undefined.stellar.data.argument.ParticleData
import com.undefined.stellar.exception.ArgumentVersionMismatchException
import com.undefined.stellar.exception.LiteralArgumentMismatchException
import com.undefined.stellar.exception.UnsupportedArgumentException
import com.undefined.stellar.util.NMSVersion
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.*
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument
import net.minecraft.commands.arguments.blocks.BlockStateArgument
import net.minecraft.commands.arguments.coordinates.*
import net.minecraft.commands.arguments.item.ItemArgument
import net.minecraft.commands.arguments.item.ItemPredicateArgument
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.*
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ColumnPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.pattern.BlockInWorld
import net.minecraft.world.level.gameevent.BlockPositionSource
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.craftbukkit.v1_17_R1.CraftParticle
import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.DisplaySlot
import java.util.*
import java.util.function.Predicate

@Suppress("UNCHECKED_CAST")
object ArgumentHelper {

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
            is StringArgument -> brigadier(argument.type)
            is PhraseArgument -> brigadier(StringType.PHRASE)
            is IntegerArgument -> IntegerArgumentType.integer(argument.min, argument.max)
            is LongArgument -> LongArgumentType.longArg(argument.min, argument.max)
            is FloatArgument -> FloatArgumentType.floatArg(argument.min, argument.max)
            is DoubleArgument -> DoubleArgumentType.doubleArg(argument.min, argument.max)
            is BooleanArgument -> BoolArgumentType.bool()
            is com.undefined.stellar.argument.entity.EntityArgument -> brigadier(argument.type)
            is com.undefined.stellar.argument.player.GameProfileArgument -> GameProfileArgument.gameProfile()
            is LocationArgument -> when (argument.type) {
                LocationType.LOCATION_3D -> BlockPosArgument.blockPos()
                LocationType.LOCATION_2D -> ColumnPosArgument.columnPos()
                LocationType.PRECISE_LOCATION_2D -> Vec3Argument.vec3()
                LocationType.PRECISE_LOCATION_3D -> Vec2Argument.vec2()
            }
            is BlockDataArgument -> BlockStateArgument.block()
            is com.undefined.stellar.argument.block.BlockPredicateArgument -> BlockPredicateArgument.blockPredicate()
            is com.undefined.stellar.argument.item.ItemArgument -> ItemArgument.item()
            is com.undefined.stellar.argument.item.ItemPredicateArgument -> ItemPredicateArgument.itemPredicate()
            is com.undefined.stellar.argument.text.ColorArgument -> ColorArgument.color()
            is com.undefined.stellar.argument.text.ComponentArgument -> ComponentArgument.textComponent()
            is com.undefined.stellar.argument.text.StyleArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.text.MessageArgument -> MessageArgument.message()
            is com.undefined.stellar.argument.scoreboard.ObjectiveArgument -> ObjectiveArgument.objective()
            is com.undefined.stellar.argument.scoreboard.ObjectiveCriteriaArgument -> ObjectiveCriteriaArgument.criteria()
            is com.undefined.stellar.argument.math.OperationArgument -> OperationArgument.operation()
            is com.undefined.stellar.argument.world.ParticleArgument -> ParticleArgument.particle()
            is com.undefined.stellar.argument.math.AngleArgument -> AngleArgument.angle()
            is com.undefined.stellar.argument.math.RotationArgument -> RotationArgument.rotation()
            is com.undefined.stellar.argument.scoreboard.DisplaySlotArgument -> ScoreboardSlotArgument.displaySlot()
            is com.undefined.stellar.argument.scoreboard.ScoreHolderArgument -> when (argument.type) {
                com.undefined.stellar.argument.scoreboard.ScoreHolderType.SINGLE -> ScoreHolderArgument.scoreHolder()
                com.undefined.stellar.argument.scoreboard.ScoreHolderType.MULTIPLE -> ScoreHolderArgument.scoreHolders()
            }
            is com.undefined.stellar.argument.math.AxisArgument -> SwizzleArgument.swizzle()
            is com.undefined.stellar.argument.scoreboard.TeamArgument -> TeamArgument.team()
            is ItemSlotArgument -> SlotArgument.slot()
            is ItemSlotsArgument -> throwArgumentVersionException(argument)
            is NamespacedKeyArgument -> ResourceLocationArgument.id()
            is com.undefined.stellar.argument.entity.EntityAnchorArgument -> EntityAnchorArgument.anchor()
            is com.undefined.stellar.argument.math.RangeArgument -> RangeArgument.intRange()
            is EnvironmentArgument -> DimensionArgument.dimension()
            is GameModeArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.math.TimeArgument -> TimeArgument.time()
            is MirrorArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.structure.StructureRotationArgument -> throwArgumentVersionException(argument)
            is HeightMapArgument -> throwArgumentVersionException(argument)
            is LootTableArgument -> throwArgumentVersionException(argument)
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
            is LocationArgument -> getLocation(context, argument)
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
            is com.undefined.stellar.argument.text.ComponentArgument ->  GsonComponentSerializer.gson().deserialize(Component.Serializer.toJson(ComponentArgument.getComponent(context, argument.name)))
            is com.undefined.stellar.argument.text.StyleArgument ->  GsonComponentSerializer.gson().deserialize(
                getArgumentInput(context, argument.name) ?: return null).style()
            is com.undefined.stellar.argument.text.MessageArgument ->  GsonComponentSerializer.gson().deserialize(Component.Serializer.toJson(MessageArgument.getMessage(context, argument.name)))
            is com.undefined.stellar.argument.scoreboard.ObjectiveArgument ->  Bukkit.getScoreboardManager()!!.mainScoreboard.getObjective(ObjectiveArgument.getObjective(context, argument.name).name)
            is com.undefined.stellar.argument.scoreboard.ObjectiveCriteriaArgument ->  ObjectiveCriteriaArgument.getCriteria(context, argument.name).name
            is com.undefined.stellar.argument.math.OperationArgument -> Operation.getOperation(getArgumentInput(context, argument.name) ?: return null)
            is com.undefined.stellar.argument.world.ParticleArgument ->  {
                val particleOptions = ParticleArgument.getParticle(context, argument.name)
                getParticleData(context, CraftParticle.toBukkit(particleOptions.type), particleOptions)
            }
            is com.undefined.stellar.argument.math.AngleArgument -> AngleArgument.getAngle(context, argument.name)
            is com.undefined.stellar.argument.math.RotationArgument -> {
                val rotation = RotationArgument.getRotation(context, argument.name).getPosition(context.source)
                Location(context.source.level.world, rotation.x, rotation.y, rotation.z)
            }
            is com.undefined.stellar.argument.scoreboard.DisplaySlotArgument -> getBukkitDisplaySlot(ScoreboardSlotArgument.getDisplaySlot(context, argument.name))
            is com.undefined.stellar.argument.scoreboard.ScoreHolderArgument -> when (argument.type) {
                com.undefined.stellar.argument.scoreboard.ScoreHolderType.SINGLE -> ScoreHolderArgument.getName(context, argument.name)
                com.undefined.stellar.argument.scoreboard.ScoreHolderType.MULTIPLE -> ScoreHolderArgument.getNames(context, argument.name)
            }
            is com.undefined.stellar.argument.math.AxisArgument -> getBukkitAxis(SwizzleArgument.getSwizzle(context, argument.name))
            is com.undefined.stellar.argument.scoreboard.TeamArgument -> Bukkit.getScoreboardManager()!!.mainScoreboard.getTeam(TeamArgument.getTeam(context, argument.name).name)
            is ItemSlotArgument -> SlotArgument.getSlot(context, argument.name)
            is ItemSlotsArgument -> throwArgumentVersionException(argument)
            is NamespacedKeyArgument -> NamespacedKey(ResourceLocationArgument.getId(context, argument.name).namespace, ResourceLocationArgument.getId(context, argument.name).path)
            is com.undefined.stellar.argument.entity.EntityAnchorArgument -> EntityAnchor.getFromName(getArgumentInput(context, argument.name) ?: return null)
            is com.undefined.stellar.argument.math.RangeArgument -> {
                val range = RangeArgument.Ints.getRange(context, argument.name)
                IntRange(range.min ?: 1, range.max ?: 2)
            }
            is EnvironmentArgument -> DimensionArgument.getDimension(context, argument.name).world.environment
            is GameModeArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.math.TimeArgument -> IntegerArgumentType.getInteger(context, argument.name).toLong()
            is MirrorArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.structure.StructureRotationArgument -> throwArgumentVersionException(argument)
            is HeightMapArgument -> throwArgumentVersionException(argument)
            is LootTableArgument -> throwArgumentVersionException(argument)
            is UUIDArgument -> UuidArgument.getUuid(context, argument.name)
            else -> throw UnsupportedArgumentException(argument)
        }
    }

    fun getArgumentInput(context: CommandContext<CommandSourceStack>, name: String): String? {
        val field = CommandContext::class.java.getDeclaredField("arguments")
        field.isAccessible = true
        val arguments: Map<String, ParsedArgument<CommandSourceStack, *>> = field.get(context) as Map<String, ParsedArgument<CommandSourceStack, *>>
        val argument = arguments[name] ?: return null
        val range = StringRange.between(argument.range.start, context.input.length)
        return range.get(context.input)
    }

    private fun brigadier(type: StringType): StringArgumentType = when (type) {
        StringType.WORD -> StringArgumentType.word()
        StringType.QUOTABLE_PHRASE -> StringArgumentType.string()
        StringType.PHRASE -> StringArgumentType.greedyString()
    }

    private fun brigadier(type: EntityDisplayType): EntityArgument = when (type) {
        EntityDisplayType.ENTITY -> EntityArgument.entity()
        EntityDisplayType.ENTITIES -> EntityArgument.entities()
        EntityDisplayType.PLAYER -> EntityArgument.player()
        EntityDisplayType.PLAYERS -> EntityArgument.players()
    }

    private fun getBukkitAxis(argument: EnumSet<Direction.Axis>): EnumSet<Axis> =
        argument.mapTo(EnumSet.noneOf(Axis::class.java)) {
            when (it) {
                Direction.Axis.X -> Axis.X
                Direction.Axis.Y -> Axis.Y
                Direction.Axis.Z -> Axis.Z
                null -> Axis.X
            }
        }

    private fun getBukkitDisplaySlot(slot: Int): DisplaySlot = when (slot) {
        0 -> DisplaySlot.PLAYER_LIST
        2 -> DisplaySlot.BELOW_NAME
        else -> DisplaySlot.SIDEBAR
    }

    private fun getParticleData(context: CommandContext<CommandSourceStack>, particle: Particle, particleOptions: ParticleOptions): ParticleData<*> = when (particleOptions) {
        is SimpleParticleType -> ParticleData(particle, null)
        is BlockParticleOption -> ParticleData<BlockData>(particle, CraftBlockData.fromData(particleOptions.state))
        is DustColorTransitionOptions -> {
            val fromColor = Color.fromRGB(
                (particleOptions.fromColor.x() * 255.0f).toInt(),
                (particleOptions.fromColor.y() * 255.0f).toInt(),
                (particleOptions.fromColor.z() * 255.0f).toInt()
            )
            val toColor = Color.fromRGB(
                (particleOptions.toColor.x() * 255.0f).toInt(),
                (particleOptions.toColor.y() * 255.0f).toInt(),
                (particleOptions.toColor.z() * 255.0f).toInt()
            )
            ParticleData(particle, Particle.DustTransition(fromColor, toColor, particleOptions.scale))
        }
        is DustParticleOptions -> ParticleData(
            particle,
            Particle.DustOptions(Color.fromRGB(
                (particleOptions.color.x() * 255.0f).toInt(),
                (particleOptions.color.y() * 255.0f).toInt(), (particleOptions.color.z() * 255.0f).toInt()
            ), particleOptions.scale)
        )
        is ItemParticleOption -> ParticleData<ItemStack>(
            particle,
            CraftItemStack.asBukkitCopy(particleOptions.item)
        )
        is VibrationParticleOption -> {
            val level: Level = context.source.level

            if (particleOptions.vibrationPath.destination is BlockPositionSource) {
                val to = particleOptions.vibrationPath.destination.getPosition(level).get().toLocation(level.world)
                val destination = Vibration.Destination.BlockDestination(Location(level.world, to.x, to.y, to.z))
                ParticleData(particle, Vibration(to, destination, particleOptions.vibrationPath.arrivalInTicks))
            } else {
                ParticleData(particle, null)
            }
        }
        else -> ParticleData(particle, null)
    }

    private fun getLocation(context: CommandContext<CommandSourceStack>, command: LocationArgument): Location {
        val world = context.source.level.world
        return when (command.type) {
            LocationType.LOCATION_3D -> context.getArgument(command.name, Coordinates::class.java).getBlockPos(context.source).toLocation(world)
            LocationType.LOCATION_2D -> ColumnPosArgument.getColumnPos(context, command.name).toLocation(world)
            LocationType.PRECISE_LOCATION_3D -> Vec3Argument.getVec3(context, command.name).toLocation(world)
            LocationType.PRECISE_LOCATION_2D -> Vec2Argument.getVec2(context, command.name).toLocation(world)
        }
    }

    private fun BlockPos.toLocation(world: World?) = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    private fun ColumnPos.toLocation(world: World?) = Location(world, x.toDouble(), 0.0, z.toDouble())
    private fun Vec3.toLocation(world: World?) = Location(world, x, y, z)
    private fun Vec2.toLocation(world: World?) = Location(world, x.toDouble(), 0.0, y.toDouble())

    private fun throwArgumentVersionException(argument: AbstractStellarArgument<*, *>): Nothing =
        throw ArgumentVersionMismatchException(argument, NMSVersion.version)

}