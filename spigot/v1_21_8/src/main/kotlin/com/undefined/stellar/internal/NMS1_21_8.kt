package com.undefined.stellar.internal

import com.google.gson.*
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.undefined.stellar.ParameterArgument
import com.undefined.stellar.argument.basic.StringArgument
import com.undefined.stellar.argument.block.BlockDataArgument
import com.undefined.stellar.argument.block.BlockPredicateArgument
import com.undefined.stellar.argument.entity.EntityAnchorArgument
import com.undefined.stellar.argument.entity.EntityArgument
import com.undefined.stellar.argument.entity.EntityDisplayType
import com.undefined.stellar.argument.item.ItemSlotArgument
import com.undefined.stellar.argument.item.ItemStackArgument
import com.undefined.stellar.argument.item.ItemStackPredicateArgument
import com.undefined.stellar.argument.math.*
import com.undefined.stellar.argument.misc.NamespacedKeyArgument
import com.undefined.stellar.argument.misc.RegistryArgument
import com.undefined.stellar.argument.misc.UUIDArgument
import com.undefined.stellar.argument.player.GameModeArgument
import com.undefined.stellar.argument.player.GameProfileArgument
import com.undefined.stellar.argument.scoreboard.*
import com.undefined.stellar.argument.structure.LootTableArgument
import com.undefined.stellar.argument.structure.MirrorArgument
import com.undefined.stellar.argument.structure.StructureRotationArgument
import com.undefined.stellar.argument.text.*
import com.undefined.stellar.argument.text.ColorArgument
import com.undefined.stellar.argument.text.ComponentArgument
import com.undefined.stellar.argument.text.MessageArgument
import com.undefined.stellar.argument.text.StyleArgument
import com.undefined.stellar.argument.world.*
import com.undefined.stellar.data.argument.EntityAnchor
import com.undefined.stellar.data.argument.Operation
import com.undefined.stellar.data.argument.ParticleData
import com.undefined.stellar.data.exception.UnsupportedArgumentException
import com.undefined.stellar.nms.NMS
import com.undefined.stellar.nms.NMSHelper
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSource
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.*
import net.minecraft.commands.arguments.blocks.BlockStateArgument
import net.minecraft.commands.arguments.coordinates.*
import net.minecraft.commands.arguments.item.ItemArgument
import net.minecraft.commands.arguments.item.ItemPredicateArgument
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.particles.*
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
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
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_21_R5.CraftParticle
import org.bukkit.craftbukkit.v1_21_R5.block.data.CraftBlockData
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_21_R5.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_21_R5.util.CraftNamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot
import java.util.*
import java.util.function.Predicate
import net.minecraft.commands.arguments.AngleArgument as BrigadierAngleArgument
import net.minecraft.commands.arguments.ColorArgument as BrigadierColorArgument
import net.minecraft.commands.arguments.ComponentArgument as BrigadierComponentArgument
import net.minecraft.commands.arguments.EntityAnchorArgument as BrigadierEntityAnchorArgument
import net.minecraft.commands.arguments.EntityArgument as BrigadierEntityArgument
import net.minecraft.commands.arguments.GameModeArgument as BrigadierGameModeArgument
import net.minecraft.commands.arguments.GameProfileArgument as BrigadierGameProfileArgument
import net.minecraft.commands.arguments.MessageArgument as BrigadierMessageArgument
import net.minecraft.commands.arguments.ObjectiveArgument as BrigadierObjectiveArgument
import net.minecraft.commands.arguments.ObjectiveCriteriaArgument as BrigadierObjectiveCriteriaArgument
import net.minecraft.commands.arguments.OperationArgument as BrigadierOperationArgument
import net.minecraft.commands.arguments.ParticleArgument as BrigadierParticleArgument
import net.minecraft.commands.arguments.ResourceOrIdArgument.LootTableArgument as BrigadierLootTableArgument
import net.minecraft.commands.arguments.ScoreHolderArgument as BrigadierScoreHolderArgument
import net.minecraft.commands.arguments.StyleArgument as BrigadierStyleArgument
import net.minecraft.commands.arguments.TeamArgument as BrigadierTeamArgument
import net.minecraft.commands.arguments.TimeArgument as BrigadierTimeArgument
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument as BrigadierBlockPredicateArgument
import net.minecraft.commands.arguments.coordinates.RotationArgument as BrigadierRotationArgument

@Suppress("UNCHECKED_CAST", "DEPRECATION")
object NMS1_21_8 : NMS {

    private val COMMAND_BUILD_CONTEXT: CommandBuildContext by lazy {
        CommandBuildContext.simple(
            MinecraftServer.getServer().registryAccess(),
            MinecraftServer.getServer().worldData.dataConfiguration.enabledFeatures()
        )
    }

    override fun getCommandDispatcher(): CommandDispatcher<Any> = MinecraftServer.getServer().functions.dispatcher as CommandDispatcher<Any>

    override fun getArgumentType(argument: ParameterArgument<*, *>, plugin: JavaPlugin): ArgumentType<*> = when (argument) {
        // Basic
        is StringArgument -> BrigadierScoreHolderArgument.scoreHolder()

        // Block
        is BlockDataArgument -> BlockStateArgument.block(COMMAND_BUILD_CONTEXT)
        is BlockPredicateArgument -> BrigadierBlockPredicateArgument.blockPredicate(COMMAND_BUILD_CONTEXT)

        // Entity
        is EntityAnchorArgument -> BrigadierEntityAnchorArgument.anchor()
        is EntityArgument -> when (argument.type) {
            EntityDisplayType.ENTITY -> BrigadierEntityArgument.entity()
            EntityDisplayType.ENTITIES -> BrigadierEntityArgument.entities()
            EntityDisplayType.PLAYER -> BrigadierEntityArgument.player()
            EntityDisplayType.PLAYERS -> BrigadierEntityArgument.players()
        }

        // Item
        is ItemStackArgument -> ItemArgument.item(COMMAND_BUILD_CONTEXT)
        is ItemStackPredicateArgument -> ItemPredicateArgument.itemPredicate(COMMAND_BUILD_CONTEXT)
        is ItemSlotArgument -> if (argument.multiple) SlotsArgument.slots() else SlotArgument.slot()

        // Math
        is AngleArgument -> BrigadierAngleArgument.angle()
        is AxisArgument -> SwizzleArgument.swizzle()
        is DoubleRangeArgument -> RangeArgument.floatRange()
        is IntRangeArgument -> RangeArgument.intRange()
        is OperationArgument -> BrigadierOperationArgument.operation()
        is RotationArgument -> BrigadierRotationArgument.rotation()
        is TimeArgument -> BrigadierTimeArgument.time(argument.minimum)

        // Misc
        is NamespacedKeyArgument -> ResourceLocationArgument.id()
        is RegistryArgument -> ResourceArgument.resource(COMMAND_BUILD_CONTEXT, ResourceKey.createRegistryKey<Keyed>(ResourceLocation.withDefaultNamespace(RegistryArgument.registryNames[argument.registry] ?: throw IllegalArgumentException("Could not find registry!"))))
        is UUIDArgument -> UuidArgument.uuid()

        // Player
        is GameModeArgument -> BrigadierGameModeArgument.gameMode()
        is GameProfileArgument -> BrigadierGameProfileArgument.gameProfile()

        // Scoreboard
        is DisplaySlotArgument -> ScoreboardSlotArgument.displaySlot()
        is ObjectiveArgument -> BrigadierObjectiveArgument.objective()
        is ObjectiveCriteriaArgument -> BrigadierObjectiveCriteriaArgument.criteria()
        is ScoreHolderArgument -> when (argument.type) {
            ScoreHolderType.SINGLE -> BrigadierScoreHolderArgument.scoreHolder()
            ScoreHolderType.MULTIPLE -> BrigadierScoreHolderArgument.scoreHolders()
        }
        is TeamArgument -> BrigadierTeamArgument.team()

        // Structure
        is LootTableArgument -> BrigadierLootTableArgument.lootTable(COMMAND_BUILD_CONTEXT)
        is MirrorArgument -> TemplateMirrorArgument.templateMirror()
        is StructureRotationArgument -> TemplateRotationArgument.templateRotation()

        // Text
        is ColorArgument -> BrigadierColorArgument.color()
        is ComponentArgument -> BrigadierComponentArgument.textComponent(COMMAND_BUILD_CONTEXT)
        is HexArgument -> HexColorArgument.hexColor()
        is MessageArgument -> BrigadierMessageArgument.message()
        is StyleArgument -> BrigadierStyleArgument.style(COMMAND_BUILD_CONTEXT)

        // World
        is EnvironmentArgument -> DimensionArgument.dimension()
        is HeightMapArgument -> HeightmapTypeArgument.heightmap()
        is LocationArgument -> when (argument.type) {
            LocationType.LOCATION_3D -> BlockPosArgument.blockPos()
            LocationType.LOCATION_2D -> ColumnPosArgument.columnPos()
            LocationType.PRECISE_LOCATION_3D -> Vec3Argument.vec3()
            LocationType.PRECISE_LOCATION_2D -> Vec2Argument.vec2()
        }
        is ParticleArgument -> BrigadierParticleArgument.particle(COMMAND_BUILD_CONTEXT)
        else -> throw UnsupportedArgumentException(argument)
    }

    override fun parseArgument(ctx: CommandContext<Any>, argument: ParameterArgument<*, *>): Any? {
        val context: CommandContext<CommandSourceStack> = ctx as CommandContext<CommandSourceStack>
        return when (argument) {
            // Basic
            is StringArgument -> NMSHelper.getArgumentInput(context, argument.name)

            // Block
            is BlockDataArgument -> CraftBlockData.fromData(BlockStateArgument.getBlock(context, argument.name).state)
            is BlockPredicateArgument -> Predicate<Block> { block: Block ->
                BrigadierBlockPredicateArgument.getBlockPredicate(context, argument.name).test(BlockInWorld(context.source.level, BlockPos(block.x, block.y, block.z), true))
            }

            // Entity
            is EntityAnchorArgument -> EntityAnchor.getFromName(NMSHelper.getArgumentInput(context, argument.name) ?: return null)
            is EntityArgument -> BrigadierEntityArgument.getEntities(context, argument.name).map { it.bukkitEntity }.takeIf { it.isNotEmpty() }
                ?: BrigadierEntityArgument.getEntity(context, argument.name).bukkitEntity

            // Item
            is ItemStackArgument -> CraftItemStack.asBukkitCopy(ItemArgument.getItem(context, argument.name).createItemStack(1, false))
            is ItemStackPredicateArgument -> Predicate<ItemStack> { ItemPredicateArgument.getItemPredicate(context, argument.name).test(CraftItemStack.asNMSCopy(it)) }
            is ItemSlotArgument -> if (argument.multiple) SlotsArgument.getSlots(context, argument.name).slots().toList() else SlotArgument.getSlot(context, argument.name)

            // Math
            is AngleArgument -> BrigadierAngleArgument.getAngle(context, argument.name)
            is AxisArgument -> SwizzleArgument.getSwizzle(context, argument.name).mapTo(EnumSet.noneOf(Axis::class.java)) { Axis.valueOf(it.name) }
            is DoubleRangeArgument -> RangeArgument.Floats.getRange(context, argument.name).let { it.min.orElse(1.0)..it.max.orElse(2.0) }
            is IntRangeArgument -> RangeArgument.Ints.getRange(context, argument.name).let { it.min.orElse(1)..it.max.orElse(2) }
            is OperationArgument -> Operation.getOperation(NMSHelper.getArgumentInput(context, argument.name) ?: return null)
            is RotationArgument -> BrigadierRotationArgument.getRotation(context, argument.name).getRotation(context.source).let {
                Location(context.source.level.world, 0.0, 0.0, 0.0, it.x, it.y)
            }
            is TimeArgument -> IntegerArgumentType.getInteger(context, argument.name)

            // Misc
            is NamespacedKeyArgument -> CraftNamespacedKey.fromMinecraft(ResourceLocationArgument.getId(context, argument.name))
            is RegistryArgument -> argument.registry.getOrThrow(NamespacedKey.fromString(NMSHelper.getArgumentInput(context, argument.name)!!)!!)
            is UUIDArgument -> UuidArgument.getUuid(context, argument.name)

            // Player
            is GameModeArgument -> GameMode.getByValue(BrigadierGameModeArgument.getGameMode(context, argument.name).id)
            is GameProfileArgument -> BrigadierGameProfileArgument.getGameProfiles(context, argument.name)

            // Scoreboard
            is DisplaySlotArgument -> DisplaySlot.valueOf(ScoreboardSlotArgument.getDisplaySlot(context, argument.name).serializedName)
            is ObjectiveArgument -> Bukkit.getScoreboardManager()!!.mainScoreboard.getObjective(BrigadierObjectiveArgument.getObjective(context, argument.name).name)
            is ObjectiveCriteriaArgument -> BrigadierObjectiveCriteriaArgument.getCriteria(context, argument.name).name
            is ScoreHolderArgument -> when (argument.type) {
                ScoreHolderType.SINGLE -> BrigadierScoreHolderArgument.getName(context, argument.name).scoreboardName
                ScoreHolderType.MULTIPLE -> BrigadierScoreHolderArgument.getNames(context, argument.name).map { it.scoreboardName }
            }
            is TeamArgument -> Bukkit.getScoreboardManager()!!.mainScoreboard.getTeam(BrigadierTeamArgument.getTeam(context, argument.name).name)

            // Structure
            is LootTableArgument -> BrigadierLootTableArgument.getLootTable(context, argument.name).value().craftLootTable
            is MirrorArgument -> Mirror.valueOf(TemplateMirrorArgument.getMirror(context, argument.name).name)
            is StructureRotationArgument -> StructureRotation.valueOf(TemplateRotationArgument.getRotation(context, argument.name).name)

            // Text
            is ColorArgument -> ChatColor.getByChar(BrigadierColorArgument.getColor(context, argument.name).char)
            is ComponentArgument -> GsonComponentSerializer.gson().deserialize(ComponentSerializer.toJson(BrigadierComponentArgument.getRawComponent(context, argument.name), COMMAND_BUILD_CONTEXT))
            is HexArgument -> HexColorArgument.getHexColor(context, argument.name)
            is MessageArgument -> GsonComponentSerializer.gson().deserialize(ComponentSerializer.toJson(BrigadierMessageArgument.getMessage(context, argument.name), COMMAND_BUILD_CONTEXT))
            is StyleArgument -> {
                ComponentSerialization.CODEC
                Codec.LONG_STREAM
                GsonComponentSerializer.gson().deserialize(
                    ComponentSerializer.toJson(
                        Component.empty().withStyle(BrigadierStyleArgument.getStyle(context, argument.name)),
                        COMMAND_BUILD_CONTEXT
                    )
                ).style()
            }

            // World
            is EnvironmentArgument -> DimensionArgument.getDimension(context, argument.name).world.environment
            is HeightMapArgument -> HeightMap.valueOf(HeightmapTypeArgument.getHeightmap(context, argument.name).name)
            is LocationArgument -> when (argument.type) {
                LocationType.LOCATION_3D -> blockPosToLocation(BlockPosArgument.getBlockPos(context, argument.name), context.source.level.world)
                LocationType.LOCATION_2D -> columnPosToLocation(ColumnPosArgument.getColumnPos(context, argument.name), context.source.level.world)
                LocationType.PRECISE_LOCATION_3D -> vec3ToLocation(Vec3Argument.getVec3(context, argument.name), context.source.level.world)
                LocationType.PRECISE_LOCATION_2D -> vec2ToLocation(Vec2Argument.getVec2(context, argument.name), context.source.level.world)
            }
            is ParticleArgument -> BrigadierParticleArgument.getParticle(context, argument.name).also { getParticleData(context, CraftParticle.minecraftToBukkit(it.type), it) }
            else -> null
        }
    }

    override fun getCommandSourceStack(sender: CommandSender): Any {
        val overworld = MinecraftServer.getServer().overworld()
        return CommandSourceStack(
            Source(sender),
            Vec3.atLowerCornerOf(overworld.sharedSpawnPos),
            Vec2.ZERO,
            overworld,
            (sender as? CraftPlayer)?.handle?.permissionLevel ?: 4,
            sender.name,
            Component.literal(sender.name),
            MinecraftServer.getServer(),
            null
        )
    }

    private data class Source(val sender: CommandSender) : CommandSource {
        override fun sendSystemMessage(message: Component) = this.sender.sendMessage(LegacyComponentSerializer.legacySection().serialize(asAdventure(message)))
        override fun acceptsSuccess(): Boolean = true
        override fun acceptsFailure(): Boolean = true
        override fun shouldInformAdmins(): Boolean = false
        override fun getBukkitSender(stack: CommandSourceStack): CommandSender = this.sender
    }

    fun asAdventure(component: Component): net.kyori.adventure.text.Component =
        GsonComponentSerializer.gson().deserialize(ComponentSerializer.toJson(component, COMMAND_BUILD_CONTEXT))

    private fun blockPosToLocation(block: BlockPos, world: World) = Location(world, block.x.toDouble(), block.y.toDouble(), block.z.toDouble())
    private fun columnPosToLocation(column: ColumnPos, world: World) = Location(world, column.x.toDouble(), 0.0, column.z.toDouble())
    private fun vec3ToLocation(vec: Vec3, world: World) = Location(world, vec.x, vec.y, vec.z)
    private fun vec2ToLocation(vec: Vec2, world: World) = Location(world, vec.x.toDouble(), 0.0, vec.y.toDouble())

    private fun getParticleData(context: CommandContext<CommandSourceStack>, particle: Particle, options: ParticleOptions): ParticleData<*> = when (options) {
        is SimpleParticleType -> ParticleData(particle, null)
        is BlockParticleOption -> ParticleData<BlockData>(particle, CraftBlockData.fromData(options.state))
        is DustColorTransitionOptions -> {
            val fromColor = Color.fromRGB(
                (options.fromColor.x() * 255.0f).toInt(),
                (options.fromColor.y() * 255.0f).toInt(),
                (options.fromColor.z() * 255.0f).toInt()
            )
            val toColor = Color.fromRGB(
                (options.toColor.x() * 255.0f).toInt(),
                (options.toColor.y() * 255.0f).toInt(),
                (options.toColor.z() * 255.0f).toInt()
            )
            ParticleData(particle, Particle.DustTransition(fromColor, toColor, options.scale))
        }
        is DustParticleOptions -> ParticleData(
            particle,
            Particle.DustOptions(
                Color.fromRGB(
                    (options.color.x() * 255.0f).toInt(),
                    (options.color.y() * 255.0f).toInt(), (options.color.z() * 255.0f).toInt()
                ), options.scale)
        )
        is ItemParticleOption -> ParticleData(
            particle,
            CraftItemStack.asBukkitCopy(options.item)
        )
        is VibrationParticleOption -> {
            val level: Level = context.source.level
            val destination: Vibration.Destination

            if (options.destination is BlockPositionSource) {
                val to: Vec3 = options.destination.getPosition(level).get()
                destination = Vibration.Destination.BlockDestination(Location(level.world, to.x(), to.y(), to.z()))
                ParticleData(particle, Vibration(Location(null, 0.0, 0.0, 0.0), destination, options.arrivalInTicks))
            } else {
                ParticleData(particle, null)
            }
        }
        is ShriekParticleOption -> ParticleData(particle, options.delay)
        is SculkChargeParticleOptions -> ParticleData(particle, options.roll())
        is ColorParticleOption -> {
            val color = Color.fromARGB(
                (options.alpha * 255.0f).toInt(),
                (options.red * 255.0f).toInt(),
                (options.green * 255.0f).toInt(),
                (options.blue * 255.0f).toInt())
            ParticleData(particle, color)
        }
        else -> ParticleData(particle, null)
    }

    object ComponentSerializer {
        private val GSON: Gson = (GsonBuilder()).disableHtmlEscaping().create()

        fun serialize(component: Component, holder: HolderLookup.Provider): JsonElement = ComponentSerialization.CODEC.encodeStart(
            holder.createSerializationContext(
                JsonOps.INSTANCE
            ), component
        ).getOrThrow { JsonParseException(it) } as JsonElement

        fun toJson(component: Component, holderLookupProvider: HolderLookup.Provider): String = GSON.toJson(serialize(component, holderLookupProvider))
    }

}