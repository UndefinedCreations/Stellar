package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.ParsedArgument
import com.mojang.brigadier.context.StringRange
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralStellarArgument
import com.undefined.stellar.argument.types.custom.CustomArgument
import com.undefined.stellar.argument.types.custom.EnumArgument
import com.undefined.stellar.argument.types.custom.ListArgument
import com.undefined.stellar.argument.types.entity.EntityDisplayType
import com.undefined.stellar.argument.types.item.ItemSlotArgument
import com.undefined.stellar.argument.types.item.ItemSlotsArgument
import com.undefined.stellar.argument.types.math.AxisArgument
import com.undefined.stellar.argument.types.misc.NamespacedKeyArgument
import com.undefined.stellar.argument.types.misc.UUIDArgument
import com.undefined.stellar.argument.types.primitive.*
import com.undefined.stellar.argument.types.scoreboard.DisplaySlotArgument
import com.undefined.stellar.argument.types.scoreboard.ScoreHoldersArgument
import com.undefined.stellar.argument.types.structure.MirrorArgument
import com.undefined.stellar.argument.types.structure.StructureRotationArgument
import com.undefined.stellar.argument.types.world.BlockDataArgument
import com.undefined.stellar.argument.types.world.HeightMapArgument
import com.undefined.stellar.argument.types.world.LocationArgument
import com.undefined.stellar.argument.types.world.LocationType
import com.undefined.stellar.data.argument.Anchor
import com.undefined.stellar.data.argument.Operation
import com.undefined.stellar.data.argument.ParticleData
import com.undefined.stellar.exception.LiteralArgumentMismatchException
import com.undefined.stellar.exception.ServerTypeMismatchException
import com.undefined.stellar.exception.UnsupportedArgumentException
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
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
import net.minecraft.core.Direction
import net.minecraft.core.particles.*
import net.minecraft.server.level.ColumnPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.pattern.BlockInWorld
import net.minecraft.world.level.gameevent.BlockPositionSource
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.block.structure.Mirror
import org.bukkit.block.structure.StructureRotation
import org.bukkit.craftbukkit.CraftParticle
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.craftbukkit.block.data.CraftBlockData
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.DisplaySlot
import java.time.Duration
import java.util.*
import java.util.function.Predicate

object ArgumentHelper {

    private val COMMAND_BUILD_CONTEXT: CommandBuildContext by lazy {
        val server = Bukkit.getServer() as? CraftServer ?: throw ServerTypeMismatchException()
        CommandBuildContext.simple(
            server.server.registryAccess(),
            server.server.worldData.dataConfiguration.enabledFeatures()
        )
    }

    fun getLiteralArguments(argument: AbstractStellarArgument<*>): List<ArgumentBuilder<CommandSourceStack, *>> {
        val arguments: MutableList<ArgumentBuilder<CommandSourceStack, *>> = mutableListOf()
        for (name in argument.aliases + argument.name)
            arguments.add(LiteralArgumentBuilder.literal(name))
        return arguments
    }

    fun getRequiredArgumentBuilder(argument: AbstractStellarArgument<*>): RequiredArgumentBuilder<CommandSourceStack, *> =
        RequiredArgumentBuilder.argument(argument.name, getArgumentType(argument))

    fun <T : AbstractStellarArgument<*>> getArgumentType(argument: T): ArgumentType<*> =
        when (argument) {
            is ListArgument<*> -> StringArgumentType.string()
            is CustomArgument<*> -> getArgumentType(argument.type)
            is StringArgument -> brigadier(argument.type)
            is IntegerArgument -> IntegerArgumentType.integer(argument.min, argument.max)
            is LongArgument -> LongArgumentType.longArg(argument.min, argument.max)
            is FloatArgument -> FloatArgumentType.floatArg(argument.min, argument.max)
            is DoubleArgument -> DoubleArgumentType.doubleArg(argument.min, argument.max)
            is BooleanArgument -> BoolArgumentType.bool()
            is com.undefined.stellar.argument.types.entity.EntityArgument -> brigadier(argument.type)
            is com.undefined.stellar.argument.types.player.GameProfileArgument -> GameProfileArgument.gameProfile()
            is LocationArgument -> when (argument.type) {
                LocationType.LOCATION3D -> BlockPosArgument.blockPos()
                LocationType.LOCATION2D -> ColumnPosArgument.columnPos()
                LocationType.DOUBLE_LOCATION_3D -> Vec3Argument.vec3()
                LocationType.DOUBLE_LOCATION_2D -> Vec2Argument.vec2()
            }
            is BlockDataArgument -> BlockStateArgument.block(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.types.world.BlockPredicateArgument -> BlockPredicateArgument.blockPredicate(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.types.item.ItemArgument -> ItemArgument.item(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.types.item.ItemPredicateArgument -> ItemPredicateArgument.itemPredicate(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.types.text.ColorArgument -> ColorArgument.color()
            is com.undefined.stellar.argument.types.text.ComponentArgument -> ComponentArgument.textComponent(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.types.text.StyleArgument -> StyleArgument.style(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.types.text.MessageArgument -> MessageArgument.message()
            is com.undefined.stellar.argument.types.scoreboard.ObjectiveArgument -> ObjectiveArgument.objective()
            is com.undefined.stellar.argument.types.scoreboard.ObjectiveCriteriaArgument -> ObjectiveCriteriaArgument.criteria()
            is com.undefined.stellar.argument.types.math.OperationArgument -> OperationArgument.operation()
            is com.undefined.stellar.argument.types.world.ParticleArgument -> ParticleArgument.particle(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.types.math.AngleArgument -> AngleArgument.angle()
            is com.undefined.stellar.argument.types.math.RotationArgument -> RotationArgument.rotation()
            is DisplaySlotArgument -> ScoreboardSlotArgument.displaySlot()
            is com.undefined.stellar.argument.types.scoreboard.ScoreHolderArgument -> ScoreHolderArgument.scoreHolder()
            is ScoreHoldersArgument -> ScoreHolderArgument.scoreHolders()
            is AxisArgument -> SwizzleArgument.swizzle()
            is com.undefined.stellar.argument.types.scoreboard.TeamArgument -> TeamArgument.team()
            is ItemSlotArgument -> SlotArgument.slot()
            is ItemSlotsArgument -> SlotsArgument.slots()
            is NamespacedKeyArgument -> ResourceLocationArgument.id()
            is com.undefined.stellar.argument.types.entity.EntityAnchorArgument -> EntityAnchorArgument.anchor()
            is com.undefined.stellar.argument.types.math.RangeArgument -> RangeArgument.intRange()
            is com.undefined.stellar.argument.types.world.DimensionArgument -> DimensionArgument.dimension()
            is com.undefined.stellar.argument.types.player.GameModeArgument -> GameModeArgument.gameMode()
            is com.undefined.stellar.argument.types.math.TimeArgument -> TimeArgument.time(argument.minimum)
            is MirrorArgument -> TemplateMirrorArgument.templateMirror()
            is StructureRotationArgument -> TemplateRotationArgument.templateRotation()
            is HeightMapArgument -> HeightmapTypeArgument.heightmap()
            is com.undefined.stellar.argument.types.structure.LootTableArgument -> LootTableArgument.lootTable(COMMAND_BUILD_CONTEXT)
            is UUIDArgument -> UuidArgument.uuid()
            else -> throw UnsupportedArgumentException()
        }

    fun <T : AbstractStellarArgument<*>> getParsedArgument(context: CommandContext<CommandSourceStack>, Argument: T): Any? {
        return when (Argument) {
            is LiteralStellarArgument -> throw LiteralArgumentMismatchException()
            is CustomArgument<*> -> Argument.parse(CommandContextAdapter.getStellarCommandContext(context))
            is StringArgument -> StringArgumentType.getString(context, Argument.name)
            is IntegerArgument -> IntegerArgumentType.getInteger(context, Argument.name)
            is FloatArgument -> FloatArgumentType.getFloat(context, Argument.name)
            is DoubleArgument -> DoubleArgumentType.getDouble(context, Argument.name)
            is BooleanArgument -> BoolArgumentType.getBool(context, Argument.name)
            is ListArgument<*> -> Argument.parse(StringArgumentType.getString(context, Argument.name))
            is EnumArgument<*> -> Argument.parse(StringArgumentType.getString(context, Argument.name))
            is com.undefined.stellar.argument.types.entity.EntityArgument -> EntityArgument.getEntities(context, Argument.name)
                .map { it.bukkitEntity }.toMutableList()
                .addAll(listOf(EntityArgument.getEntity(context, Argument.name).bukkitEntity))
            is com.undefined.stellar.argument.types.player.GameProfileArgument -> GameProfileArgument.getGameProfiles(context, Argument.name)
            is LocationArgument -> getLocation(context, Argument)
            is BlockDataArgument -> CraftBlockData.fromData(BlockStateArgument.getBlock(context, Argument.name).state)
            is com.undefined.stellar.argument.types.world.BlockPredicateArgument -> Predicate<Block> { block: Block ->
                BlockPredicateArgument.getBlockPredicate(context, Argument.name).test(BlockInWorld(
                    context.source.level,
                    BlockPos(block.x, block.y, block.z), true
                ))
            }
            is com.undefined.stellar.argument.types.item.ItemArgument -> CraftItemStack.asBukkitCopy(ItemArgument.getItem(context, Argument.name).createItemStack(1, false))
            is com.undefined.stellar.argument.types.item.ItemPredicateArgument -> Predicate<ItemStack> { item: ItemStack ->
                ItemPredicateArgument.getItemPredicate(context, Argument.name).test(CraftItemStack.asNMSCopy(item))
            }
            is com.undefined.stellar.argument.types.text.ColorArgument -> ColorArgument.getColor(context, Argument.name).color?.let { Style.style(TextColor.color(it)) } ?: Style.empty()
            is com.undefined.stellar.argument.types.text.ComponentArgument ->  GsonComponentSerializer.gson().deserialize(net.minecraft.network.chat.Component.Serializer.toJson(ComponentArgument.getComponent(context, Argument.name), COMMAND_BUILD_CONTEXT))
            is com.undefined.stellar.argument.types.text.StyleArgument ->  GsonComponentSerializer.gson().deserialize(getArgumentInput(context, Argument.name) ?: return null).style()
            is com.undefined.stellar.argument.types.text.MessageArgument ->  GsonComponentSerializer.gson().deserialize(net.minecraft.network.chat.Component.Serializer.toJson(MessageArgument.getMessage(context, Argument.name), COMMAND_BUILD_CONTEXT))
            is com.undefined.stellar.argument.types.scoreboard.ObjectiveArgument ->  Bukkit.getScoreboardManager().mainScoreboard.getObjective(ObjectiveArgument.getObjective(context, Argument.name).name)
            is com.undefined.stellar.argument.types.scoreboard.ObjectiveCriteriaArgument ->  ObjectiveCriteriaArgument.getCriteria(context, Argument.name).name
            is com.undefined.stellar.argument.types.math.OperationArgument ->  Operation.getOperation(getArgumentInput(context, Argument.name) ?: return null)
            is com.undefined.stellar.argument.types.world.ParticleArgument ->  {
                val particleOptions = ParticleArgument.getParticle(context, Argument.name)
                getParticleData(context, CraftParticle.minecraftToBukkit(particleOptions.type), particleOptions)
            }
            is com.undefined.stellar.argument.types.math.AngleArgument -> AngleArgument.getAngle(context, Argument.name)
            is com.undefined.stellar.argument.types.math.RotationArgument -> {
                val rotation = RotationArgument.getRotation(context, Argument.name).getPosition(context.source)
                Location(context.source.bukkitWorld, rotation.x, rotation.y, rotation.z)
            }
            is DisplaySlotArgument -> getBukkitDisplaySlot(ScoreboardSlotArgument.getDisplaySlot(context, Argument.name))
            is com.undefined.stellar.argument.types.scoreboard.ScoreHolderArgument -> ScoreHolderArgument.getName(context, Argument.name).scoreboardName
            is ScoreHoldersArgument -> ScoreHolderArgument.getNames(context, Argument.name).map { scoreholder -> scoreholder.scoreboardName }
            is AxisArgument -> getBukkitAxis(SwizzleArgument.getSwizzle(context, Argument.name))
            is com.undefined.stellar.argument.types.scoreboard.TeamArgument -> Bukkit.getScoreboardManager().mainScoreboard.getTeam(TeamArgument.getTeam(context, Argument.name).name)
            is ItemSlotArgument -> SlotArgument.getSlot(context, Argument.name)
            is ItemSlotsArgument -> SlotsArgument.getSlots(context, Argument.name).slots().toList()
            is NamespacedKeyArgument -> NamespacedKey(ResourceLocationArgument.getId(context, Argument.name).namespace, ResourceLocationArgument.getId(context, Argument.name).path)
            is com.undefined.stellar.argument.types.entity.EntityAnchorArgument -> Anchor.getFromName(getArgumentInput(context, Argument.name) ?: return null)
            is com.undefined.stellar.argument.types.math.RangeArgument -> {
                val range = RangeArgument.Ints.getRange(context, Argument.name)
                IntRange(range.min.orElse(1), range.max.orElse(2))
            }
            is com.undefined.stellar.argument.types.world.DimensionArgument -> DimensionArgument.getDimension(context, Argument.name).world.environment
            is com.undefined.stellar.argument.types.player.GameModeArgument -> GameMode.getByValue(GameModeArgument.getGameMode(context, Argument.name).id)
            is com.undefined.stellar.argument.types.math.TimeArgument -> Duration.ofSeconds(IntegerArgumentType.getInteger(context, Argument.name).toLong() / 20)
            is MirrorArgument -> Mirror.valueOf(TemplateMirrorArgument.getMirror(context, Argument.name).name)
            is StructureRotationArgument -> StructureRotation.valueOf(TemplateRotationArgument.getRotation(context, Argument.name).name)
            is HeightMapArgument -> HeightMap.valueOf(HeightmapTypeArgument.getHeightmap(context, Argument.name).name)
            is com.undefined.stellar.argument.types.structure.LootTableArgument -> LootTableArgument.getLootTable(context, Argument.name).value().craftLootTable
            is UUIDArgument -> UuidArgument.getUuid(context, Argument.name)
            else -> throw UnsupportedArgumentException()
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
        StringType.SINGLE_WORD -> StringArgumentType.word()
        StringType.QUOTABLE_PHRASE -> StringArgumentType.string()
        StringType.GREEDY_PHRASE -> StringArgumentType.greedyString()
    }

    private fun brigadier(type: EntityDisplayType): EntityArgument = when (type) {
        EntityDisplayType.ENTITY -> EntityArgument.entity()
        EntityDisplayType.ENTITIES -> EntityArgument.entities()
        EntityDisplayType.PLAYER -> EntityArgument.player()
        EntityDisplayType.PLAYERS -> EntityArgument.players()
    }

    private fun RequiredArgumentBuilder<CommandSourceStack, *>.suggestStringList(list: () -> List<String>) =
        suggests { context, suggestionsBuilder ->
            list().filter { it.startsWith(suggestionsBuilder.remaining, true) }.forEach { suggestionsBuilder.suggest(it) }
            return@suggests suggestionsBuilder.buildFuture()
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

    private fun getBukkitDisplaySlot(slot: net.minecraft.world.scores.DisplaySlot): DisplaySlot = when (slot) {
        net.minecraft.world.scores.DisplaySlot.LIST -> DisplaySlot.PLAYER_LIST
        net.minecraft.world.scores.DisplaySlot.SIDEBAR -> DisplaySlot.SIDEBAR
        net.minecraft.world.scores.DisplaySlot.BELOW_NAME -> DisplaySlot.BELOW_NAME
        net.minecraft.world.scores.DisplaySlot.TEAM_BLACK -> DisplaySlot.SIDEBAR_TEAM_BLACK
        net.minecraft.world.scores.DisplaySlot.TEAM_DARK_BLUE -> DisplaySlot.SIDEBAR_TEAM_DARK_BLUE
        net.minecraft.world.scores.DisplaySlot.TEAM_DARK_GREEN -> DisplaySlot.SIDEBAR_TEAM_DARK_GREEN
        net.minecraft.world.scores.DisplaySlot.TEAM_DARK_AQUA -> DisplaySlot.SIDEBAR_TEAM_DARK_AQUA
        net.minecraft.world.scores.DisplaySlot.TEAM_DARK_RED -> DisplaySlot.SIDEBAR_TEAM_DARK_RED
        net.minecraft.world.scores.DisplaySlot.TEAM_DARK_PURPLE -> DisplaySlot.SIDEBAR_TEAM_DARK_PURPLE
        net.minecraft.world.scores.DisplaySlot.TEAM_GOLD -> DisplaySlot.SIDEBAR_TEAM_GOLD
        net.minecraft.world.scores.DisplaySlot.TEAM_GRAY -> DisplaySlot.SIDEBAR_TEAM_GRAY
        net.minecraft.world.scores.DisplaySlot.TEAM_DARK_GRAY -> DisplaySlot.SIDEBAR_TEAM_DARK_GRAY
        net.minecraft.world.scores.DisplaySlot.TEAM_BLUE -> DisplaySlot.SIDEBAR_TEAM_BLUE
        net.minecraft.world.scores.DisplaySlot.TEAM_GREEN -> DisplaySlot.SIDEBAR_TEAM_GREEN
        net.minecraft.world.scores.DisplaySlot.TEAM_AQUA -> DisplaySlot.SIDEBAR_TEAM_AQUA
        net.minecraft.world.scores.DisplaySlot.TEAM_RED -> DisplaySlot.SIDEBAR_TEAM_RED
        net.minecraft.world.scores.DisplaySlot.TEAM_LIGHT_PURPLE -> DisplaySlot.SIDEBAR_TEAM_LIGHT_PURPLE
        net.minecraft.world.scores.DisplaySlot.TEAM_YELLOW -> DisplaySlot.SIDEBAR_TEAM_YELLOW
        net.minecraft.world.scores.DisplaySlot.TEAM_WHITE -> DisplaySlot.SIDEBAR_TEAM_WHITE
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
            val origin: Vec3 = context.source.position
            val level: Level = context.source.level
            val destination: Vibration.Destination

            if (particleOptions.destination is BlockPositionSource) {
                val to: Vec3 = particleOptions.destination.getPosition(level).get()
                destination = Vibration.Destination.BlockDestination(Location(level.world, to.x(), to.y(), to.z()))
                ParticleData(particle, Vibration(destination, particleOptions.arrivalInTicks))
            } else {
                ParticleData(particle, null)
            }
        }
        is ShriekParticleOption -> ParticleData(particle, particleOptions.delay)
        is SculkChargeParticleOptions -> ParticleData(particle, particleOptions.roll())
        is ColorParticleOption -> {
            val color = Color.fromARGB(
                (particleOptions.alpha * 255.0f).toInt(),
                (particleOptions.red * 255.0f).toInt(),
                (particleOptions.green * 255.0f).toInt(),
                (particleOptions.blue * 255.0f).toInt())
            ParticleData(particle, color)
        }
        else -> ParticleData(particle, null)
    }

    private fun getLocation(context: CommandContext<CommandSourceStack>, command: LocationArgument): Location {
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