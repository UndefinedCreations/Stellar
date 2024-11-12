package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.ParsedArgument
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.undefined.stellar.data.arguments.Anchor
import com.undefined.stellar.data.arguments.Operation
import com.undefined.stellar.data.arguments.ParticleData
import com.undefined.stellar.exception.ServerTypeMismatchException
import com.undefined.stellar.exception.UnsupportedSubCommandException
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import com.undefined.stellar.sub.brigadier.entity.EntityAnchorSubCommand
import com.undefined.stellar.sub.brigadier.entity.EntityDisplayType
import com.undefined.stellar.sub.brigadier.entity.EntitySubCommand
import com.undefined.stellar.sub.brigadier.item.ItemPredicateSubCommand
import com.undefined.stellar.sub.brigadier.item.ItemSlotSubCommand
import com.undefined.stellar.sub.brigadier.item.ItemSlotsSubCommand
import com.undefined.stellar.sub.brigadier.item.ItemSubCommand
import com.undefined.stellar.sub.brigadier.math.*
import com.undefined.stellar.sub.brigadier.misc.NamespacedKeySubCommand
import com.undefined.stellar.sub.brigadier.player.GameModeSubCommand
import com.undefined.stellar.sub.brigadier.player.GameProfileSubCommand
import com.undefined.stellar.sub.brigadier.primitive.*
import com.undefined.stellar.sub.brigadier.scoreboard.*
import com.undefined.stellar.sub.brigadier.structure.MirrorSubCommand
import com.undefined.stellar.sub.brigadier.text.ColorSubCommand
import com.undefined.stellar.sub.brigadier.text.ComponentSubCommand
import com.undefined.stellar.sub.brigadier.text.MessageSubCommand
import com.undefined.stellar.sub.brigadier.text.StyleSubCommand
import com.undefined.stellar.sub.brigadier.world.*
import com.undefined.stellar.sub.custom.EnumSubCommand
import com.undefined.stellar.sub.custom.ListSubCommand
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.commands.CommandBuildContext
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
import org.bukkit.craftbukkit.CraftParticle
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.craftbukkit.block.data.CraftBlockData
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.DisplaySlot
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import java.util.function.Predicate

object ArgumentHelper {

    val COMMAND_BUILD_CONTEXT by lazy {
        val server = Bukkit.getServer()
        if (server !is CraftServer) throw ServerTypeMismatchException()
        CommandBuildContext.simple(
            server.server.registryAccess(),
            server.server.worldData.dataConfiguration.enabledFeatures()
        )
    }

    fun <T : BrigadierTypeSubCommand<*>> nativeSubCommandToArgument(subCommand: T): RequiredArgumentBuilder<CommandSourceStack, *> =
        when (subCommand) {
            is ListSubCommand<*> -> RequiredArgumentBuilder.argument<CommandSourceStack, String>(subCommand.name, StringArgumentType.word()).suggestStringList(subCommand.getStringList())
            is EnumSubCommand<*> -> RequiredArgumentBuilder.argument<CommandSourceStack, String>(subCommand.name, StringArgumentType.word()).suggestStringList(subCommand.getStringList())
            else -> RequiredArgumentBuilder.argument(subCommand.name, getArgumentTypeFromBrigadierSubCommand(subCommand))
        }

    fun <T : BrigadierTypeSubCommand<*>> handleNativeSubCommandExecutors(subCommand: T, context: CommandContext<CommandSourceStack>) =
        when (subCommand) {
            is ListSubCommand<*> -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, subCommand.parse.invoke(StringArgumentType.getString(context, subCommand.name))!!) }
            is EnumSubCommand<*> -> subCommand.customExecutions.forEach { execution ->
                val enum = subCommand.parse(StringArgumentType.getString(context, subCommand.name))
                enum?.let { execution.run(context.source.bukkitSender, enum) }
            }
            else -> {
                val argument = getArgumentFromBrigadierSubCommand(context, subCommand)
                subCommand.customExecutions.forEach {
                    it.run(context.source.bukkitSender, argument ?: return)
                }
            }
        }

    fun <T : BrigadierTypeSubCommand<*>> handleNativeSubCommandRunnables(subCommand: T, context: CommandContext<CommandSourceStack>): Boolean {
        when (subCommand) {
            is ListSubCommand<*> -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, subCommand.parse.invoke(StringArgumentType.getString(context, subCommand.name))!!)) return false }
            is EnumSubCommand<*> -> subCommand.customRunnables.forEach { runnable ->
                val enum = subCommand.parse(StringArgumentType.getString(context, subCommand.name))
                enum?.let { if (!runnable.run(context.source.bukkitSender, enum)) return false }
            }
            else -> {
                val argument = getArgumentFromBrigadierSubCommand(context, subCommand) ?: return true
                subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, argument)) return false }
            }
        }
        return true
    }

    private fun <T : BrigadierTypeSubCommand<*>> getArgumentTypeFromBrigadierSubCommand(subCommand: T): ArgumentType<*> =
        when (subCommand) {
            is StringSubCommand -> subCommand.type.brigadier()
            is IntegerSubCommand -> IntegerArgumentType.integer(subCommand.min, subCommand.max)
            is LongSubCommand -> LongArgumentType.longArg(subCommand.min, subCommand.max)
            is FloatSubCommand -> FloatArgumentType.floatArg(subCommand.min, subCommand.max)
            is DoubleSubCommand -> DoubleArgumentType.doubleArg(subCommand.min, subCommand.max)
            is BooleanSubCommand -> BoolArgumentType.bool()
            is EntitySubCommand -> subCommand.type.brigadier()
            is GameProfileSubCommand -> GameProfileArgument.gameProfile()
            is LocationSubCommand -> when (subCommand.type) {
                LocationType.LOCATION3D -> BlockPosArgument.blockPos()
                LocationType.LOCATION2D -> ColumnPosArgument.columnPos()
                LocationType.DOUBLE_LOCATION_3D -> Vec3Argument.vec3()
                LocationType.DOUBLE_LOCATION_2D -> Vec2Argument.vec2()
            }
            is BlockDataSubCommand -> BlockStateArgument.block(COMMAND_BUILD_CONTEXT)
            is BlockPredicateSubCommand -> BlockPredicateArgument.blockPredicate(COMMAND_BUILD_CONTEXT)
            is ItemSubCommand -> ItemArgument.item(COMMAND_BUILD_CONTEXT)
            is ItemPredicateSubCommand -> ItemPredicateArgument.itemPredicate(COMMAND_BUILD_CONTEXT)
            is ColorSubCommand -> ColorArgument.color()
            is ComponentSubCommand -> ComponentArgument.textComponent(COMMAND_BUILD_CONTEXT)
            is StyleSubCommand -> StyleArgument.style(COMMAND_BUILD_CONTEXT)
            is MessageSubCommand -> MessageArgument.message()
            is ObjectiveSubCommand -> ObjectiveArgument.objective()
            is ObjectiveCriteriaSubCommand -> ObjectiveCriteriaArgument.criteria()
            is OperationSubCommand -> OperationArgument.operation()
            is ParticleSubCommand -> ParticleArgument.particle(COMMAND_BUILD_CONTEXT)
            is AngleSubCommand -> AngleArgument.angle()
            is RotationSubCommand -> RotationArgument.rotation()
            is DisplaySlotSubCommand -> ScoreboardSlotArgument.displaySlot()
            is ScoreHolderSubCommand -> ScoreHolderArgument.scoreHolder()
            is ScoreHoldersSubCommand -> ScoreHolderArgument.scoreHolders()
            is AxisSubCommand -> SwizzleArgument.swizzle()
            is TeamSubCommand -> TeamArgument.team()
            is ItemSlotSubCommand -> SlotArgument.slot()
            is ItemSlotsSubCommand -> SlotsArgument.slots()
            is NamespacedKeySubCommand -> ResourceLocationArgument.id()
            is EntityAnchorSubCommand -> EntityAnchorArgument.anchor()
            is RangeSubCommand -> RangeArgument.intRange()
            is DimensionSubCommand -> DimensionArgument.dimension()
            is GameModeSubCommand -> GameModeArgument.gameMode()
            is TimeSubCommand -> TimeArgument.time(subCommand.minimum)
            is MirrorSubCommand -> TemplateMirrorArgument.templateMirror()
            else -> throw UnsupportedSubCommandException()
        }

    private fun <T : BrigadierTypeSubCommand<*>> getArgumentFromBrigadierSubCommand(context: CommandContext<CommandSourceStack>, subCommand: T): Any? =
        when (subCommand) {
            is StringSubCommand -> StringArgumentType.getString(context, subCommand.name)
            is IntegerSubCommand -> IntegerArgumentType.getInteger(context, subCommand.name)
            is FloatSubCommand -> FloatArgumentType.getFloat(context, subCommand.name)
            is DoubleSubCommand -> DoubleArgumentType.getDouble(context, subCommand.name)
            is BooleanSubCommand -> BoolArgumentType.getBool(context, subCommand.name)
            is ListSubCommand<*> -> subCommand.parse(StringArgumentType.getString(context, subCommand.name))
            is EnumSubCommand<*> -> subCommand.parse(StringArgumentType.getString(context, subCommand.name))
            is EntitySubCommand -> EntityArgument.getEntities(context, subCommand.name)
                .map { it.bukkitEntity }.toMutableList()
                .addAll(listOf(EntityArgument.getEntity(context, subCommand.name).bukkitEntity))
            is GameProfileSubCommand -> GameProfileArgument.getGameProfiles(context, subCommand.name)
            is LocationSubCommand -> getLocation(context, subCommand)
            is BlockDataSubCommand -> CraftBlockData.fromData(BlockStateArgument.getBlock(context, subCommand.name).state)
            is BlockPredicateSubCommand -> Predicate<Block> { block: Block ->
                BlockPredicateArgument.getBlockPredicate(context, subCommand.name).test(BlockInWorld(
                    context.source.level,
                    BlockPos(block.x, block.y, block.z), true
                ))
            }
            is ItemSubCommand -> CraftItemStack.asBukkitCopy(ItemArgument.getItem(context, subCommand.name).createItemStack(1, false))
            is ItemPredicateSubCommand -> Predicate<ItemStack> { item: ItemStack ->
                ItemPredicateArgument.getItemPredicate(context, subCommand.name).test(CraftItemStack.asNMSCopy(item))
            }
            is ColorSubCommand -> ColorArgument.getColor(context, subCommand.name).color?.let { Style.style(TextColor.color(it)) } ?: Style.empty()
            is ComponentSubCommand ->  GsonComponentSerializer.gson().deserialize(net.minecraft.network.chat.Component.Serializer.toJson(ComponentArgument.getComponent(context, subCommand.name), COMMAND_BUILD_CONTEXT))
            is StyleSubCommand ->  GsonComponentSerializer.gson().deserialize(getArgumentInput(context, subCommand.name)).style()
            is MessageSubCommand ->  GsonComponentSerializer.gson().deserialize(net.minecraft.network.chat.Component.Serializer.toJson(MessageArgument.getMessage(context, subCommand.name), COMMAND_BUILD_CONTEXT))
            is ObjectiveSubCommand ->  Bukkit.getScoreboardManager().mainScoreboard.getObjective(ObjectiveArgument.getObjective(context, subCommand.name).name)
            is ObjectiveCriteriaSubCommand ->  ObjectiveCriteriaArgument.getCriteria(context, subCommand.name).name
            is OperationSubCommand ->  Operation.getOperation(getArgumentInput(context, subCommand.name))
            is ParticleSubCommand ->  {
                val particleOptions = ParticleArgument.getParticle(context, subCommand.name)
                getParticleData(context, CraftParticle.minecraftToBukkit(particleOptions.type), particleOptions)
            }
            is AngleSubCommand -> AngleArgument.getAngle(context, subCommand.name)
            is RotationSubCommand -> {
                val rotation = RotationArgument.getRotation(context, subCommand.name).getPosition(context.source)
                Location(context.source.bukkitWorld, rotation.x, rotation.y, rotation.z)
            }
            is DisplaySlotSubCommand -> getBukkitDisplaySlot(ScoreboardSlotArgument.getDisplaySlot(context, subCommand.name))
            is ScoreHolderSubCommand -> ScoreHolderArgument.getName(context, subCommand.name).scoreboardName
            is ScoreHoldersSubCommand -> ScoreHolderArgument.getNames(context, subCommand.name).map { scoreholder -> scoreholder.scoreboardName }
            is AxisSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, getBukkitAxis(SwizzleArgument.getSwizzle(context, subCommand.name))) }
            is TeamSubCommand -> Bukkit.getScoreboardManager().mainScoreboard.getTeam(TeamArgument.getTeam(context, subCommand.name).name)
            is ItemSlotSubCommand -> SlotArgument.getSlot(context, subCommand.name)
            is ItemSlotsSubCommand -> SlotsArgument.getSlots(context, subCommand.name).slots().toList()
            is NamespacedKeySubCommand -> NamespacedKey(ResourceLocationArgument.getId(context, subCommand.name).namespace, ResourceLocationArgument.getId(context, subCommand.name).path)
            is EntityAnchorSubCommand -> Anchor.getFromName(getArgumentInput(context, subCommand.name))
            is RangeSubCommand -> {
                val range = RangeArgument.Ints.getRange(context, subCommand.name)
                IntRange(range.min.orElse(1), range.max.orElse(2))
            }
            is DimensionSubCommand -> DimensionArgument.getDimension(context, subCommand.name).world.environment
            is GameModeSubCommand -> GameMode.getByValue(GameModeArgument.getGameMode(context, subCommand.name).id)
            is TimeSubCommand -> Duration.ofSeconds(IntegerArgumentType.getInteger(context, subCommand.name).toLong() / 20)
            is MirrorSubCommand -> Mirror.valueOf(TemplateMirrorArgument.getMirror(context, subCommand.name).name)
            else -> throw UnsupportedSubCommandException()
        }

    private fun getArgumentInput(context: CommandContext<CommandSourceStack>, name: String): String {
        val field = CommandContext::class.java.getDeclaredField("arguments")
        field.isAccessible = true
        val arguments: Map<String, ParsedArgument<CommandSourceStack, *>> = field.get(context) as Map<String, ParsedArgument<CommandSourceStack, *>>
        val argument = arguments[name] ?: return ""
        val range = StringRange.between(argument.range.start, context.input.length)
        return range.get(context.input)
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
            val from = Location(level.world, origin.x, origin.y, origin.z)
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