@file:Suppress("DEPRECATION")

package com.undefined.stellar.v1_17_1

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.ParsedArgument
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralStellarArgument
import com.undefined.stellar.argument.types.block.BlockDataArgument
import com.undefined.stellar.argument.types.custom.CustomArgument
import com.undefined.stellar.argument.types.custom.ListArgument
import com.undefined.stellar.argument.types.entity.*
import com.undefined.stellar.argument.types.item.*
import com.undefined.stellar.argument.types.math.AxisArgument
import com.undefined.stellar.argument.types.misc.NamespacedKeyArgument
import com.undefined.stellar.argument.types.misc.UUIDArgument
import com.undefined.stellar.argument.types.player.GameModeArgument
import com.undefined.stellar.argument.types.primitive.*
import com.undefined.stellar.argument.types.registry.*
import com.undefined.stellar.argument.types.scoreboard.DisplaySlotArgument
import com.undefined.stellar.argument.types.scoreboard.ScoreHolderType
import com.undefined.stellar.argument.types.structure.LootTableArgument
import com.undefined.stellar.argument.types.structure.MirrorArgument
import com.undefined.stellar.argument.types.structure.StructureRotationArgument
import com.undefined.stellar.argument.types.world.*
import com.undefined.stellar.data.argument.Anchor
import com.undefined.stellar.data.argument.Operation
import com.undefined.stellar.data.argument.ParticleData
import com.undefined.stellar.exception.ArgumentVersionMismatchException
import com.undefined.stellar.exception.LiteralArgumentMismatchException
import com.undefined.stellar.exception.UnsupportedArgumentException
import com.undefined.stellar.util.NMSVersion
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.*
import net.minecraft.commands.arguments.DimensionArgument
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.ParticleArgument
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument
import net.minecraft.commands.arguments.blocks.BlockStateArgument
import net.minecraft.commands.arguments.coordinates.*
import net.minecraft.commands.arguments.item.ItemArgument
import net.minecraft.commands.arguments.item.ItemPredicateArgument
import net.minecraft.commands.synchronization.SuggestionProviders
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Registry
import net.minecraft.core.particles.*
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ColumnPos
import net.minecraft.world.inventory.MenuType
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
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.DisplaySlot
import java.util.*
import java.util.function.Predicate

@Suppress("UNCHECKED_CAST")
object ArgumentHelper {

    fun getLiteralArguments(argument: AbstractStellarArgument<*>): List<ArgumentBuilder<CommandSourceStack, *>> {
        val arguments: MutableList<ArgumentBuilder<CommandSourceStack, *>> = mutableListOf()
        for (name in argument.aliases + argument.name)
            arguments.add(LiteralArgumentBuilder.literal(name))
        return arguments
    }

    fun getRequiredArgumentBuilder(argument: AbstractStellarArgument<*>): RequiredArgumentBuilder<CommandSourceStack, *> {
        val argumentBuilder: RequiredArgumentBuilder<CommandSourceStack, *> = RequiredArgumentBuilder.argument(argument.name, getArgumentType(argument))
        getSuggestions(argument)?.let { argumentBuilder.suggests(it) }
        return argumentBuilder
    }

    private fun <T : AbstractStellarArgument<*>> getSuggestions(argument: T): SuggestionProvider<CommandSourceStack>? =
        when (argument) {
            is GameEventArgument -> SuggestionProvider { _, builder ->
                SharedSuggestionProvider.suggestResource(Registry.GAME_EVENT.keySet(), builder)
            }
            is PotionEffectTypeArgument -> SuggestionProvider { _, builder ->
                SharedSuggestionProvider.suggestResource(Registry.MOB_EFFECT.keySet(), builder)
            }
            is VillagerProfessionArgument -> SuggestionProvider { _, builder ->
                SharedSuggestionProvider.suggestResource(Registry.VILLAGER_PROFESSION.keySet(), builder)
            }
            is VillagerTypeArgument -> SuggestionProvider { _, builder ->
                SharedSuggestionProvider.suggestResource(Registry.VILLAGER_TYPE.keySet(), builder)
            }
            is AttributeArgument -> SuggestionProvider { _, builder ->
                SharedSuggestionProvider.suggestResource(Registry.ATTRIBUTE.keySet(), builder)
            }
            is FluidArgument -> SuggestionProvider { _, builder ->
                SharedSuggestionProvider.suggestResource(Registry.FLUID.keySet(), builder)
            }
            is SoundArgument -> SuggestionProvider { context, builder ->
                SuggestionProviders.AVAILABLE_SOUNDS.getSuggestions(context, builder)
            }
            is BiomeArgument -> SuggestionProvider { context, builder ->
                SuggestionProviders.AVAILABLE_BIOMES.getSuggestions(context, builder)
            }
            is EntityTypeArgument -> SuggestionProvider { _, builder ->
                SharedSuggestionProvider.suggestResource(Registry.ENTITY_TYPE.keySet(), builder)
            }
            is MemoryKeyArgument -> SuggestionProvider { _, builder ->
                SharedSuggestionProvider.suggestResource(Registry.MEMORY_MODULE_TYPE.keySet(), builder)
            }
            else -> null
        }

    private fun <T : AbstractStellarArgument<*>> getArgumentType(argument: T): ArgumentType<*> =
        when (argument) {
            is ListArgument<*> -> getArgumentType(argument.type)
            is CustomArgument<*> -> getArgumentType(argument.type)
            is StringArgument -> brigadier(argument.type)
            is PhraseArgument -> brigadier(StringType.PHRASE)
            is IntegerArgument -> IntegerArgumentType.integer(argument.min, argument.max)
            is LongArgument -> LongArgumentType.longArg(argument.min, argument.max)
            is FloatArgument -> FloatArgumentType.floatArg(argument.min, argument.max)
            is DoubleArgument -> DoubleArgumentType.doubleArg(argument.min, argument.max)
            is BooleanArgument -> BoolArgumentType.bool()
            is com.undefined.stellar.argument.types.entity.EntityArgument -> brigadier(argument.type)
            is com.undefined.stellar.argument.types.player.GameProfileArgument -> GameProfileArgument.gameProfile()
            is LocationArgument -> when (argument.type) {
                LocationType.LOCATION_3D -> BlockPosArgument.blockPos()
                LocationType.LOCATION_2D -> ColumnPosArgument.columnPos()
                LocationType.PRECISE_LOCATION_2D -> Vec3Argument.vec3()
                LocationType.PRECISE_LOCATION_3D -> Vec2Argument.vec2()
            }
            is BlockDataArgument -> BlockStateArgument.block()
            is com.undefined.stellar.argument.types.block.BlockPredicateArgument -> BlockPredicateArgument.blockPredicate()
            is com.undefined.stellar.argument.types.item.ItemArgument -> ItemArgument.item()
            is com.undefined.stellar.argument.types.item.ItemPredicateArgument -> ItemPredicateArgument.itemPredicate()
            is com.undefined.stellar.argument.types.text.ColorArgument -> ColorArgument.color()
            is com.undefined.stellar.argument.types.text.ComponentArgument -> ComponentArgument.textComponent()
            is com.undefined.stellar.argument.types.text.StyleArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.types.text.MessageArgument -> MessageArgument.message()
            is com.undefined.stellar.argument.types.scoreboard.ObjectiveArgument -> ObjectiveArgument.objective()
            is com.undefined.stellar.argument.types.scoreboard.ObjectiveCriteriaArgument -> ObjectiveCriteriaArgument.criteria()
            is com.undefined.stellar.argument.types.math.OperationArgument -> OperationArgument.operation()
            is com.undefined.stellar.argument.types.world.ParticleArgument -> ParticleArgument.particle()
            is com.undefined.stellar.argument.types.math.AngleArgument -> AngleArgument.angle()
            is com.undefined.stellar.argument.types.math.RotationArgument -> RotationArgument.rotation()
            is DisplaySlotArgument -> ScoreboardSlotArgument.displaySlot()
            is com.undefined.stellar.argument.types.scoreboard.ScoreHolderArgument -> when (argument.type) {
                ScoreHolderType.SINGLE -> ScoreHolderArgument.scoreHolder()
                ScoreHolderType.MULTIPLE -> ScoreHolderArgument.scoreHolders()
            }
            is AxisArgument -> SwizzleArgument.swizzle()
            is com.undefined.stellar.argument.types.scoreboard.TeamArgument -> TeamArgument.team()
            is ItemSlotArgument -> SlotArgument.slot()
            is ItemSlotsArgument -> throwArgumentVersionException(argument)
            is NamespacedKeyArgument -> ResourceLocationArgument.id()
            is com.undefined.stellar.argument.types.entity.EntityAnchorArgument -> EntityAnchorArgument.anchor()
            is com.undefined.stellar.argument.types.math.RangeArgument -> RangeArgument.intRange()
            is com.undefined.stellar.argument.types.world.DimensionArgument -> DimensionArgument.dimension()
            is GameModeArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.types.math.TimeArgument -> TimeArgument.time()
            is MirrorArgument -> throwArgumentVersionException(argument)
            is StructureRotationArgument -> throwArgumentVersionException(argument)
            is HeightMapArgument -> throwArgumentVersionException(argument)
            is LootTableArgument -> throwArgumentVersionException(argument)
            is UUIDArgument -> UuidArgument.uuid()
            is GameEventArgument -> ResourceLocationArgument.id()
            is StructureTypeArgument -> throwArgumentVersionException(argument)
            is PotionEffectTypeArgument -> throwArgumentVersionException(argument)
            is BlockTypeArgument -> throwArgumentVersionException(argument)
            is ItemTypeArgument -> throwArgumentVersionException(argument)
            is CatTypeArgument -> throwArgumentVersionException(argument)
            is FrogVariantArgument -> throwArgumentVersionException(argument)
            is VillagerProfessionArgument -> ResourceLocationArgument.id()
            is VillagerTypeArgument -> ResourceLocationArgument.id()
            is MapDecorationTypeArgument -> throwArgumentVersionException(argument)
            is InventoryTypeArgument -> throwArgumentVersionException(argument)
            is AttributeArgument -> ResourceLocationArgument.id()
            is FluidArgument -> ResourceLocationArgument.id()
            is SoundArgument -> ResourceLocationArgument.id()
            is BiomeArgument -> ResourceLocationArgument.id()
            is StructureArgument -> throwArgumentVersionException(argument)
            is TrimMaterialArgument -> throwArgumentVersionException(argument)
            is TrimPatternArgument -> throwArgumentVersionException(argument)
            is DamageTypeArgument -> throwArgumentVersionException(argument)
            is WolfVariantArgument -> throwArgumentVersionException(argument)
            is PatternTypeArgument -> throwArgumentVersionException(argument)
            is ArtArgument -> throwArgumentVersionException(argument)
            is InstrumentArgument -> throwArgumentVersionException(argument)
            is EntityTypeArgument -> ResourceLocationArgument.id()
            is PotionArgument -> throwArgumentVersionException(argument)
            is MemoryKeyArgument -> ResourceLocationArgument.id()
            else -> throw UnsupportedArgumentException(argument)
        }

    fun <T : AbstractStellarArgument<*>> getParsedArgument(context: CommandContext<CommandSourceStack>, argument: T): Any? {
        return when (argument) {
            is LiteralStellarArgument -> throw LiteralArgumentMismatchException()
            is CustomArgument<*> -> argument.parse(CommandContextAdapter.getStellarCommandContext(context))
            is StringArgument -> StringArgumentType.getString(context, argument.name)
            is IntegerArgument -> IntegerArgumentType.getInteger(context, argument.name)
            is LongArgument -> LongArgumentType.getLong(context, argument.name)
            is FloatArgument -> FloatArgumentType.getFloat(context, argument.name)
            is DoubleArgument -> DoubleArgumentType.getDouble(context, argument.name)
            is BooleanArgument -> BoolArgumentType.getBool(context, argument.name)
            is ListArgument<*> -> argument.parse(getParsedArgument(context, argument))
            is com.undefined.stellar.argument.types.entity.EntityArgument -> EntityArgument.getEntities(context, argument.name)
                .map { it.bukkitEntity }.toMutableList()
                .addAll(listOf(EntityArgument.getEntity(context, argument.name).bukkitEntity))
            is com.undefined.stellar.argument.types.player.GameProfileArgument -> GameProfileArgument.getGameProfiles(context, argument.name)
            is LocationArgument -> getLocation(context, argument)
            is BlockDataArgument -> CraftBlockData.fromData(BlockStateArgument.getBlock(context, argument.name).state)
            is com.undefined.stellar.argument.types.block.BlockPredicateArgument -> Predicate<Block> { block: Block ->
                BlockPredicateArgument.getBlockPredicate(context, argument.name).test(BlockInWorld(
                    context.source.level,
                    BlockPos(block.x, block.y, block.z), true
                ))
            }
            is com.undefined.stellar.argument.types.item.ItemArgument -> CraftItemStack.asBukkitCopy(ItemArgument.getItem(context, argument.name).createItemStack(1, false))
            is com.undefined.stellar.argument.types.item.ItemPredicateArgument -> Predicate<ItemStack> { item: ItemStack ->
                ItemPredicateArgument.getItemPredicate(context, argument.name).test(CraftItemStack.asNMSCopy(item))
            }
            is com.undefined.stellar.argument.types.text.ColorArgument -> ChatColor.getByChar(ColorArgument.getColor(context, argument.name).char)
            is com.undefined.stellar.argument.types.text.ComponentArgument ->  GsonComponentSerializer.gson().deserialize(Component.Serializer.toJson(ComponentArgument.getComponent(context, argument.name)))
            is com.undefined.stellar.argument.types.text.StyleArgument ->  GsonComponentSerializer.gson().deserialize(
                getArgumentInput(context, argument.name) ?: return null).style()
            is com.undefined.stellar.argument.types.text.MessageArgument ->  GsonComponentSerializer.gson().deserialize(Component.Serializer.toJson(MessageArgument.getMessage(context, argument.name)))
            is com.undefined.stellar.argument.types.scoreboard.ObjectiveArgument ->  Bukkit.getScoreboardManager().mainScoreboard.getObjective(ObjectiveArgument.getObjective(context, argument.name).name)
            is com.undefined.stellar.argument.types.scoreboard.ObjectiveCriteriaArgument ->  ObjectiveCriteriaArgument.getCriteria(context, argument.name).name
            is com.undefined.stellar.argument.types.math.OperationArgument -> Operation.getOperation(getArgumentInput(context, argument.name) ?: return null)
            is com.undefined.stellar.argument.types.world.ParticleArgument ->  {
                val particleOptions = ParticleArgument.getParticle(context, argument.name)
                getParticleData(context, CraftParticle.toBukkit(particleOptions.type), particleOptions)
            }
            is com.undefined.stellar.argument.types.math.AngleArgument -> AngleArgument.getAngle(context, argument.name)
            is com.undefined.stellar.argument.types.math.RotationArgument -> {
                val rotation = RotationArgument.getRotation(context, argument.name).getPosition(context.source)
                Location(context.source.level.world, rotation.x, rotation.y, rotation.z)
            }
            is DisplaySlotArgument -> getBukkitDisplaySlot(ScoreboardSlotArgument.getDisplaySlot(context, argument.name))
            is com.undefined.stellar.argument.types.scoreboard.ScoreHolderArgument -> when (argument.type) {
                ScoreHolderType.SINGLE -> ScoreHolderArgument.getName(context, argument.name)
                ScoreHolderType.MULTIPLE -> ScoreHolderArgument.getNames(context, argument.name)
            }
            is AxisArgument -> getBukkitAxis(SwizzleArgument.getSwizzle(context, argument.name))
            is com.undefined.stellar.argument.types.scoreboard.TeamArgument -> Bukkit.getScoreboardManager().mainScoreboard.getTeam(TeamArgument.getTeam(context, argument.name).name)
            is ItemSlotArgument -> SlotArgument.getSlot(context, argument.name)
            is ItemSlotsArgument -> throwArgumentVersionException(argument)
            is NamespacedKeyArgument -> NamespacedKey(ResourceLocationArgument.getId(context, argument.name).namespace, ResourceLocationArgument.getId(context, argument.name).path)
            is com.undefined.stellar.argument.types.entity.EntityAnchorArgument -> Anchor.getFromName(getArgumentInput(context, argument.name) ?: return null)
            is com.undefined.stellar.argument.types.math.RangeArgument -> {
                val range = RangeArgument.Ints.getRange(context, argument.name)
                IntRange(range.min ?: 1, range.max ?: 2)
            }
            is com.undefined.stellar.argument.types.world.DimensionArgument -> DimensionArgument.getDimension(context, argument.name).world.environment
            is GameModeArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.types.math.TimeArgument -> IntegerArgumentType.getInteger(context, argument.name).toLong()
            is MirrorArgument -> throwArgumentVersionException(argument)
            is StructureRotationArgument -> throwArgumentVersionException(argument)
            is HeightMapArgument -> throwArgumentVersionException(argument)
            is LootTableArgument -> throwArgumentVersionException(argument)
            is UUIDArgument -> UuidArgument.getUuid(context, argument.name)
            is GameEventArgument -> org.bukkit.Registry.GAME_EVENT.get(getId(context, argument.name))
            is StructureTypeArgument -> throwArgumentVersionException(argument)
            is PotionEffectTypeArgument -> throwArgumentVersionException(argument)
            is BlockTypeArgument -> throwArgumentVersionException(argument)
            is ItemTypeArgument -> throwArgumentVersionException(argument)
            is CatTypeArgument -> throwArgumentVersionException(argument)
            is FrogVariantArgument -> throwArgumentVersionException(argument)
            is VillagerProfessionArgument -> org.bukkit.Registry.VILLAGER_PROFESSION.get(getId(context, argument.name))
            is VillagerTypeArgument -> org.bukkit.Registry.VILLAGER_TYPE.get(getId(context, argument.name))
            is MapDecorationTypeArgument -> throwArgumentVersionException(argument)
            is InventoryTypeArgument -> getInventoryType(Registry.MENU.getOrThrow(ResourceKey.create(Registry.MENU_REGISTRY, ResourceLocationArgument.getId(context, argument.name))))
            is AttributeArgument -> org.bukkit.Registry.ATTRIBUTE.get(getId(context, argument.name))
            is FluidArgument -> org.bukkit.Registry.FLUID.get(getId(context, argument.name))
            is SoundArgument -> org.bukkit.Registry.SOUNDS.get(getId(context, argument.name))
            is BiomeArgument -> org.bukkit.Registry.BIOME.get(getId(context, argument.name))
            is StructureArgument -> throwArgumentVersionException(argument)
            is TrimMaterialArgument -> throwArgumentVersionException(argument)
            is TrimPatternArgument -> throwArgumentVersionException(argument)
            is DamageTypeArgument -> throwArgumentVersionException(argument)
            is WolfVariantArgument -> throwArgumentVersionException(argument)
            is PatternTypeArgument -> throwArgumentVersionException(argument)
            is ArtArgument -> throwArgumentVersionException(argument)
            is InstrumentArgument -> throwArgumentVersionException(argument)
            is EntityTypeArgument -> org.bukkit.Registry.ENTITY_TYPE.get(getId(context, argument.name))
            is PotionArgument -> throwArgumentVersionException(argument)
            is MemoryKeyArgument -> org.bukkit.Registry.MEMORY_MODULE_TYPE.get(getId(context, argument.name))
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

    private fun getInventoryType(menu: MenuType<*>): InventoryType = when (menu) {
        MenuType.GENERIC_9x1 -> InventoryType.CHEST
        MenuType.GENERIC_9x2 -> InventoryType.CHEST
        MenuType.GENERIC_9x3 -> InventoryType.CHEST
        MenuType.GENERIC_9x4 -> InventoryType.CHEST
        MenuType.GENERIC_9x5 -> InventoryType.CHEST
        MenuType.GENERIC_9x6 -> InventoryType.CHEST
        MenuType.GENERIC_3x3 -> InventoryType.WORKBENCH
        MenuType.ANVIL -> InventoryType.ANVIL
        MenuType.BEACON -> InventoryType.BEACON
        MenuType.BLAST_FURNACE -> InventoryType.BLAST_FURNACE
        MenuType.BREWING_STAND -> InventoryType.BREWING
        MenuType.CRAFTING -> InventoryType.CRAFTING
        MenuType.ENCHANTMENT -> InventoryType.ENCHANTING
        MenuType.FURNACE -> InventoryType.FURNACE
        MenuType.GRINDSTONE -> InventoryType.GRINDSTONE
        MenuType.HOPPER -> InventoryType.HOPPER
        MenuType.LECTERN -> InventoryType.LECTERN
        MenuType.LOOM -> InventoryType.LOOM
        MenuType.MERCHANT -> InventoryType.MERCHANT
        MenuType.SHULKER_BOX -> InventoryType.SHULKER_BOX
        MenuType.SMITHING -> InventoryType.SMITHING
        MenuType.SMOKER -> InventoryType.SMOKER
        MenuType.CARTOGRAPHY_TABLE -> InventoryType.CARTOGRAPHY
        MenuType.STONECUTTER -> InventoryType.STONECUTTER
        else -> throw IllegalStateException("No inventory type found! This is not intentional behaviour, please contact the developers.")
    }

    @Throws(CommandSyntaxException::class)
    private fun getId(
        context: CommandContext<CommandSourceStack>,
        name: String
    ): NamespacedKey {
        val key = ResourceLocationArgument.getId(context, name)
        return NamespacedKey(key.namespace, key.path)
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
            LocationType.LOCATION_3D -> context.getArgument(command.name, Coordinates::class.java).getBlockPos(context.source).toLocation(world);
            LocationType.LOCATION_2D -> ColumnPosArgument.getColumnPos(context, command.name).toLocation(world)
            LocationType.PRECISE_LOCATION_3D -> Vec3Argument.getVec3(context, command.name).toLocation(world)
            LocationType.PRECISE_LOCATION_2D -> Vec2Argument.getVec2(context, command.name).toLocation(world)
        }
    }

    private fun BlockPos.toLocation(world: World?) = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    private fun ColumnPos.toLocation(world: World?) = Location(world, x.toDouble(), 0.0, z.toDouble())
    private fun Vec3.toLocation(world: World?) = Location(world, x, y, z)
    private fun Vec2.toLocation(world: World?) = Location(world, x.toDouble(), 0.0, y.toDouble())

    private fun throwArgumentVersionException(argument: AbstractStellarArgument<*>): Nothing =
        throw ArgumentVersionMismatchException(argument, NMSVersion.version)

}