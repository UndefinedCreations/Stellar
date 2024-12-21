package com.undefined.stellar.v1_20_1

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.ParsedArgument
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.undefined.stellar.argument.AbstractStellarArgument
import com.undefined.stellar.argument.LiteralStellarArgument
import com.undefined.stellar.argument.types.registry.ArtArgument
import com.undefined.stellar.argument.types.block.BlockDataArgument
import com.undefined.stellar.argument.types.registry.BlockTypeArgument
import com.undefined.stellar.argument.types.registry.FluidArgument
import com.undefined.stellar.argument.types.custom.CustomArgument
import com.undefined.stellar.argument.types.custom.ListArgument
import com.undefined.stellar.argument.types.entity.*
import com.undefined.stellar.argument.types.item.*
import com.undefined.stellar.argument.types.math.AxisArgument
import com.undefined.stellar.argument.types.misc.NamespacedKeyArgument
import com.undefined.stellar.argument.types.misc.UUIDArgument
import com.undefined.stellar.argument.types.primitive.*
import com.undefined.stellar.argument.types.registry.*
import com.undefined.stellar.argument.types.scoreboard.DisplaySlotArgument
import com.undefined.stellar.argument.types.scoreboard.ScoreHoldersArgument
import com.undefined.stellar.argument.types.registry.InstrumentArgument
import com.undefined.stellar.argument.types.registry.SoundArgument
import com.undefined.stellar.argument.types.structure.MirrorArgument
import com.undefined.stellar.argument.types.registry.StructureArgument
import com.undefined.stellar.argument.types.structure.StructureRotationArgument
import com.undefined.stellar.argument.types.registry.StructureTypeArgument
import com.undefined.stellar.argument.types.world.*
import com.undefined.stellar.data.argument.Anchor
import com.undefined.stellar.data.argument.Operation
import com.undefined.stellar.data.argument.ParticleData
import com.undefined.stellar.exception.ArgumentVersionMismatchException
import com.undefined.stellar.exception.LiteralArgumentMismatchException
import com.undefined.stellar.exception.UnsupportedArgumentException
import com.undefined.stellar.util.NMSVersion
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
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
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.particles.*
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
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
import org.bukkit.block.structure.Mirror
import org.bukkit.block.structure.StructureRotation
import org.bukkit.craftbukkit.v1_20_R1.CraftParticle
import org.bukkit.craftbukkit.v1_20_R1.block.data.CraftBlockData
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.DisplaySlot
import java.time.Duration
import java.util.*
import java.util.function.Predicate

@Suppress("UNCHECKED_CAST")
object ArgumentHelper {

    private val COMMAND_BUILD_CONTEXT: CommandBuildContext by lazy {
        CommandBuildContext.simple(
            MinecraftServer.getServer().registryAccess(),
            MinecraftServer.getServer().worldData.dataConfiguration.enabledFeatures()
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
                LocationType.LOCATION3D -> BlockPosArgument.blockPos()
                LocationType.LOCATION2D -> ColumnPosArgument.columnPos()
                LocationType.DOUBLE_LOCATION_3D -> Vec3Argument.vec3()
                LocationType.DOUBLE_LOCATION_2D -> Vec2Argument.vec2()
            }
            is BlockDataArgument -> BlockStateArgument.block(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.types.block.BlockPredicateArgument -> BlockPredicateArgument.blockPredicate(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.types.item.ItemArgument -> ItemArgument.item(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.types.item.ItemPredicateArgument -> ItemPredicateArgument.itemPredicate(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.types.text.ColorArgument -> ColorArgument.color()
            is com.undefined.stellar.argument.types.text.ComponentArgument -> ComponentArgument.textComponent()
            is com.undefined.stellar.argument.types.text.StyleArgument -> throwArgumentVersionException(argument)
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
            is ItemSlotsArgument -> throwArgumentVersionException(argument)
            is NamespacedKeyArgument -> ResourceLocationArgument.id()
            is com.undefined.stellar.argument.types.entity.EntityAnchorArgument -> EntityAnchorArgument.anchor()
            is com.undefined.stellar.argument.types.math.RangeArgument -> RangeArgument.intRange()
            is com.undefined.stellar.argument.types.world.DimensionArgument -> DimensionArgument.dimension()
            is com.undefined.stellar.argument.types.player.GameModeArgument -> GameModeArgument.gameMode()
            is com.undefined.stellar.argument.types.math.TimeArgument -> TimeArgument.time(argument.minimum)
            is MirrorArgument -> TemplateMirrorArgument.templateMirror()
            is StructureRotationArgument -> TemplateRotationArgument.templateRotation()
            is HeightMapArgument -> HeightmapTypeArgument.heightmap()
            is com.undefined.stellar.argument.types.structure.LootTableArgument -> throwArgumentVersionException(argument)
            is UUIDArgument -> UuidArgument.uuid()
            is GameEventArgument -> ResourceKeyArgument.key(Registries.GAME_EVENT)
            is StructureTypeArgument -> ResourceKeyArgument.key(Registries.STRUCTURE_TYPE)
            is PotionEffectTypeArgument -> ResourceKeyArgument.key(Registries.MOB_EFFECT)
            is BlockTypeArgument -> throwArgumentVersionException(argument)
            is ItemTypeArgument -> ResourceKeyArgument.key(Registries.ITEM)
            is CatTypeArgument -> ResourceKeyArgument.key(Registries.CAT_VARIANT)
            is FrogVariantArgument -> ResourceKeyArgument.key(Registries.FROG_VARIANT)
            is VillagerProfessionArgument -> ResourceKeyArgument.key(Registries.VILLAGER_PROFESSION)
            is VillagerTypeArgument -> ResourceKeyArgument.key(Registries.VILLAGER_TYPE)
            is MapDecorationTypeArgument -> throwArgumentVersionException(argument)
            is InventoryTypeArgument -> ResourceKeyArgument.key(Registries.MENU)
            is AttributeArgument -> ResourceKeyArgument.key(Registries.ATTRIBUTE)
            is FluidArgument -> ResourceKeyArgument.key(Registries.FLUID)
            is SoundArgument -> ResourceKeyArgument.key(Registries.SOUND_EVENT)
            is BiomeArgument -> ResourceKeyArgument.key(Registries.BIOME)
            is StructureArgument -> ResourceKeyArgument.key(Registries.STRUCTURE)
            is TrimMaterialArgument -> ResourceKeyArgument.key(Registries.TRIM_MATERIAL)
            is TrimPatternArgument -> ResourceKeyArgument.key(Registries.TRIM_PATTERN)
            is DamageTypeArgument -> ResourceKeyArgument.key(Registries.DAMAGE_TYPE)
            is WolfVariantArgument -> throwArgumentVersionException(argument)
            is PatternTypeArgument -> ResourceKeyArgument.key(Registries.BANNER_PATTERN)
            is ArtArgument -> ResourceKeyArgument.key(Registries.PAINTING_VARIANT)
            is InstrumentArgument -> ResourceKeyArgument.key(Registries.INSTRUMENT)
            is EntityTypeArgument -> ResourceKeyArgument.key(Registries.ENTITY_TYPE)
            is PotionArgument -> ResourceKeyArgument.key(Registries.POTION)
            is MemoryKeyArgument -> ResourceKeyArgument.key(Registries.MEMORY_MODULE_TYPE)
            else -> throw UnsupportedArgumentException()
        }

    fun <T : AbstractStellarArgument<*>> getParsedArgument(context: CommandContext<CommandSourceStack>, argument: T): Any? {
        return when (argument) {
            is LiteralStellarArgument -> throw LiteralArgumentMismatchException()
            is CustomArgument<*> -> argument.parse(CommandContextAdapter.getStellarCommandContext(context))
            is StringArgument -> StringArgumentType.getString(context, argument.name)
            is IntegerArgument -> IntegerArgumentType.getInteger(context, argument.name)
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
            is com.undefined.stellar.argument.types.text.ColorArgument -> ColorArgument.getColor(context, argument.name).color?.let { Style.style(TextColor.color(it)) } ?: Style.empty()
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
                Location(context.source.bukkitWorld, rotation.x, rotation.y, rotation.z)
            }
            is DisplaySlotArgument -> getBukkitDisplaySlot(ScoreboardSlotArgument.getDisplaySlot(context, argument.name))
            is com.undefined.stellar.argument.types.scoreboard.ScoreHolderArgument -> ScoreHolderArgument.getName(context, argument.name)
            is ScoreHoldersArgument -> ScoreHolderArgument.getNames(context, argument.name)
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
            is com.undefined.stellar.argument.types.player.GameModeArgument -> GameMode.getByValue(GameModeArgument.getGameMode(context, argument.name).id)
            is com.undefined.stellar.argument.types.math.TimeArgument -> Duration.ofSeconds(IntegerArgumentType.getInteger(context, argument.name).toLong() / 20)
            is MirrorArgument -> Mirror.valueOf(TemplateMirrorArgument.getMirror(context, argument.name).name)
            is StructureRotationArgument -> StructureRotation.valueOf(TemplateRotationArgument.getRotation(context, argument.name).name)
            is HeightMapArgument -> HeightMap.valueOf(HeightmapTypeArgument.getHeightmap(context, argument.name).name)
            is com.undefined.stellar.argument.types.structure.LootTableArgument -> throwArgumentVersionException(argument)
            is UUIDArgument -> UuidArgument.getUuid(context, argument.name)
            is GameEventArgument -> org.bukkit.Registry.GAME_EVENT.get(getId(context, argument.name, Registries.GAME_EVENT))
            is StructureTypeArgument -> org.bukkit.Registry.STRUCTURE_TYPE.get(getId(context, argument.name, Registries.STRUCTURE_TYPE))
            is PotionEffectTypeArgument -> org.bukkit.Registry.POTION_EFFECT_TYPE.get(getId(context, argument.name, Registries.MOB_EFFECT))
            is BlockTypeArgument -> throwArgumentVersionException(argument)
            is ItemTypeArgument -> throwArgumentVersionException(argument)
            is CatTypeArgument -> throwArgumentVersionException(argument)
            is FrogVariantArgument -> org.bukkit.Registry.FROG_VARIANT.get(getId(context, argument.name, Registries.FROG_VARIANT))
            is VillagerProfessionArgument -> org.bukkit.Registry.VILLAGER_PROFESSION.get(getId(context, argument.name, Registries.VILLAGER_PROFESSION))
            is VillagerTypeArgument -> org.bukkit.Registry.VILLAGER_TYPE.get(getId(context, argument.name, Registries.VILLAGER_TYPE))
            is MapDecorationTypeArgument -> throwArgumentVersionException(argument)
            is InventoryTypeArgument -> getInventoryType(resolveKey(context, argument.name, Registries.MENU).value())
            is AttributeArgument -> org.bukkit.Registry.ATTRIBUTE.get(getId(context, argument.name, Registries.ATTRIBUTE))
            is FluidArgument -> org.bukkit.Registry.FLUID.get(getId(context, argument.name, Registries.FLUID))
            is SoundArgument -> org.bukkit.Registry.SOUNDS.get(getId(context, argument.name, Registries.SOUND_EVENT))
            is BiomeArgument -> org.bukkit.Registry.BIOME.get(getId(context, argument.name, Registries.BIOME))
            is StructureArgument -> org.bukkit.Registry.STRUCTURE.get(getId(context, argument.name, Registries.STRUCTURE))
            is TrimMaterialArgument -> org.bukkit.Registry.TRIM_MATERIAL.get(getId(context, argument.name, Registries.TRIM_MATERIAL))
            is TrimPatternArgument -> org.bukkit.Registry.TRIM_PATTERN.get(getId(context, argument.name, Registries.TRIM_PATTERN))
            is DamageTypeArgument -> throwArgumentVersionException(argument)
            is WolfVariantArgument -> throwArgumentVersionException(argument)
            is PatternTypeArgument -> throwArgumentVersionException(argument)
            is ArtArgument -> org.bukkit.Registry.ART.get(getId(context, argument.name, Registries.PAINTING_VARIANT))
            is InstrumentArgument -> org.bukkit.Registry.INSTRUMENT.get(getId(context, argument.name, Registries.INSTRUMENT))
            is EntityTypeArgument -> org.bukkit.Registry.ENTITY_TYPE.get(getId(context, argument.name, Registries.ENTITY_TYPE))
            is PotionArgument -> throwArgumentVersionException(argument)
            is MemoryKeyArgument -> org.bukkit.Registry.MEMORY_MODULE_TYPE.get(getId(context, argument.name, Registries.MEMORY_MODULE_TYPE))
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
    private fun <T> getRegistryKey(
        context: CommandContext<CommandSourceStack>,
        name: String,
        registryRef: ResourceKey<Registry<T>>,
        invalidException: DynamicCommandExceptionType
    ): ResourceKey<T> {
        val resourceKey = context.getArgument(name, ResourceKey::class.java)
        val optional = resourceKey.cast(registryRef)
        return optional.orElseThrow {
            invalidException.create(resourceKey)
        }
    }

    private fun <T> getRegistry(
        context: CommandContext<CommandSourceStack>,
        registryRef: ResourceKey<out Registry<T>>
    ): Registry<T> = context.source.server.registryAccess().registryOrThrow(registryRef)

    @Throws(CommandSyntaxException::class)
    private fun <T> resolveKey(
        context: CommandContext<CommandSourceStack>,
        name: String,
        registryRef: ResourceKey<Registry<T>>
    ): Holder.Reference<T> {
        val invalidException = DynamicCommandExceptionType { argument ->
            Component.translatable("argument.resource_or_id.invalid", argument)
        }
        val resourceKey = getRegistryKey(context, name, registryRef, invalidException)
        return getRegistry(context, registryRef).getHolder(resourceKey).orElseThrow { invalidException.create(resourceKey.location()) }
    }

    @Throws(CommandSyntaxException::class)
    private fun <T> getId(
        context: CommandContext<CommandSourceStack>,
        name: String,
        registryRef: ResourceKey<Registry<T>>
    ): NamespacedKey {
        val key = resolveKey(context, name, registryRef).key().location()
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
        1 -> DisplaySlot.SIDEBAR
        2 -> DisplaySlot.BELOW_NAME
        3 -> DisplaySlot.SIDEBAR_TEAM_BLACK
        4 -> DisplaySlot.SIDEBAR_TEAM_DARK_BLUE
        5 -> DisplaySlot.SIDEBAR_TEAM_DARK_GREEN
        6 -> DisplaySlot.SIDEBAR_TEAM_DARK_AQUA
        7 -> DisplaySlot.SIDEBAR_TEAM_DARK_RED
        8 -> DisplaySlot.SIDEBAR_TEAM_DARK_PURPLE
        9 -> DisplaySlot.SIDEBAR_TEAM_GOLD
        10 -> DisplaySlot.SIDEBAR_TEAM_GRAY
        11 -> DisplaySlot.SIDEBAR_TEAM_DARK_GRAY
        12 -> DisplaySlot.SIDEBAR_TEAM_BLUE
        13 -> DisplaySlot.SIDEBAR_TEAM_GREEN
        14 -> DisplaySlot.SIDEBAR_TEAM_AQUA
        15 -> DisplaySlot.SIDEBAR_TEAM_RED
        16 -> DisplaySlot.SIDEBAR_TEAM_LIGHT_PURPLE
        17 -> DisplaySlot.SIDEBAR_TEAM_YELLOW
        18 -> DisplaySlot.SIDEBAR_TEAM_WHITE
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

    private fun throwArgumentVersionException(argument: AbstractStellarArgument<*>): Nothing =
        throw ArgumentVersionMismatchException(argument, NMSVersion.version)

}