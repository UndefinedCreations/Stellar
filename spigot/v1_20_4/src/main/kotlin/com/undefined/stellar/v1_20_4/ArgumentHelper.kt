package com.undefined.stellar.v1_20_4

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
import com.undefined.stellar.argument.basic.*
import com.undefined.stellar.argument.block.BlockDataArgument
import com.undefined.stellar.argument.entity.*
import com.undefined.stellar.argument.item.*
import com.undefined.stellar.argument.misc.NamespacedKeyArgument
import com.undefined.stellar.argument.misc.UUIDArgument
import com.undefined.stellar.argument.registry.*
import com.undefined.stellar.argument.structure.MirrorArgument
import com.undefined.stellar.argument.world.*
import com.undefined.stellar.data.argument.EntityAnchor
import com.undefined.stellar.data.argument.Operation
import com.undefined.stellar.data.argument.ParticleData
import com.undefined.stellar.exception.ArgumentVersionMismatchException
import com.undefined.stellar.exception.LiteralArgumentMismatchException
import com.undefined.stellar.exception.UnsupportedArgumentException
import com.undefined.stellar.util.NMSVersion
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
import org.bukkit.craftbukkit.v1_20_R3.CraftParticle
import org.bukkit.craftbukkit.v1_20_R3.block.data.CraftBlockData
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.DisplaySlot
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
                LocationType.PRECISE_LOCATION_3D -> Vec3Argument.vec3()
                LocationType.PRECISE_LOCATION_2D -> Vec2Argument.vec2()
            }
            is BlockDataArgument -> BlockStateArgument.block(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.block.BlockPredicateArgument -> BlockPredicateArgument.blockPredicate(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.item.ItemArgument -> ItemArgument.item(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.item.ItemPredicateArgument -> ItemPredicateArgument.itemPredicate(COMMAND_BUILD_CONTEXT)
            is com.undefined.stellar.argument.text.ColorArgument -> ColorArgument.color()
            is com.undefined.stellar.argument.text.ComponentArgument -> ComponentArgument.textComponent()
            is com.undefined.stellar.argument.text.StyleArgument -> StyleArgument.style()
            is com.undefined.stellar.argument.text.MessageArgument -> MessageArgument.message()
            is com.undefined.stellar.argument.scoreboard.ObjectiveArgument -> ObjectiveArgument.objective()
            is com.undefined.stellar.argument.scoreboard.ObjectiveCriteriaArgument -> ObjectiveCriteriaArgument.criteria()
            is com.undefined.stellar.argument.math.OperationArgument -> OperationArgument.operation()
            is com.undefined.stellar.argument.world.ParticleArgument -> ParticleArgument.particle(COMMAND_BUILD_CONTEXT)
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
            is com.undefined.stellar.argument.world.EnvironmentArgument -> DimensionArgument.dimension()
            is com.undefined.stellar.argument.player.GameModeArgument -> GameModeArgument.gameMode()
            is com.undefined.stellar.argument.math.TimeArgument -> TimeArgument.time(argument.minimum)
            is MirrorArgument -> TemplateMirrorArgument.templateMirror()
            is com.undefined.stellar.argument.structure.StructureRotationArgument -> TemplateRotationArgument.templateRotation()
            is HeightMapArgument -> HeightmapTypeArgument.heightmap()
            is com.undefined.stellar.argument.structure.LootTableArgument -> throwArgumentVersionException(argument)
            is UUIDArgument -> UuidArgument.uuid()
            is GameEventArgument -> ResourceKeyArgument.key(Registries.GAME_EVENT)
            is StructureTypeArgument -> ResourceKeyArgument.key(Registries.STRUCTURE_TYPE)
            is PotionEffectTypeArgument -> ResourceKeyArgument.key(Registries.MOB_EFFECT)
            is BlockTypeArgument -> ResourceKeyArgument.key(Registries.BLOCK_TYPE)
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
                getParticleData(context, CraftParticle.minecraftToBukkit(particleOptions.type), particleOptions)
            }
            is com.undefined.stellar.argument.math.AngleArgument -> AngleArgument.getAngle(context, argument.name)
            is com.undefined.stellar.argument.math.RotationArgument -> {
                val coordinates = RotationArgument.getRotation(context, argument.name)
                val position = coordinates.getPosition(context.source)
                val rotation = coordinates.getRotation(context.source)
                Location(context.source.level.world, position.x, position.y, position.z, rotation.x, rotation.y)
            }
            is com.undefined.stellar.argument.scoreboard.DisplaySlotArgument -> getBukkitDisplaySlot(ScoreboardSlotArgument.getDisplaySlot(context, argument.name))
            is com.undefined.stellar.argument.scoreboard.ScoreHolderArgument -> when (argument.type) {
                com.undefined.stellar.argument.scoreboard.ScoreHolderType.SINGLE -> ScoreHolderArgument.getName(context, argument.name).scoreboardName
                com.undefined.stellar.argument.scoreboard.ScoreHolderType.MULTIPLE -> ScoreHolderArgument.getNames(context, argument.name).map { it.scoreboardName }
            }
            is com.undefined.stellar.argument.math.AxisArgument -> getBukkitAxis(SwizzleArgument.getSwizzle(context, argument.name))
            is com.undefined.stellar.argument.scoreboard.TeamArgument -> Bukkit.getScoreboardManager()!!.mainScoreboard.getTeam(TeamArgument.getTeam(context, argument.name).name)
            is ItemSlotArgument -> SlotArgument.getSlot(context, argument.name)
            is ItemSlotsArgument -> throwArgumentVersionException(argument)
            is NamespacedKeyArgument -> NamespacedKey(ResourceLocationArgument.getId(context, argument.name).namespace, ResourceLocationArgument.getId(context, argument.name).path)
            is com.undefined.stellar.argument.entity.EntityAnchorArgument -> EntityAnchor.getFromName(getArgumentInput(context, argument.name) ?: return null)
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
            is com.undefined.stellar.argument.structure.LootTableArgument -> throwArgumentVersionException(argument)
            is UUIDArgument -> UuidArgument.getUuid(context, argument.name)
            is GameEventArgument -> org.bukkit.Registry.GAME_EVENT.get(getId(context, argument.name, Registries.GAME_EVENT))
            is StructureTypeArgument -> org.bukkit.Registry.STRUCTURE_TYPE.get(getId(context, argument.name, Registries.STRUCTURE_TYPE))
            is PotionEffectTypeArgument -> org.bukkit.Registry.EFFECT.get(getId(context, argument.name, Registries.MOB_EFFECT))
            is BlockTypeArgument -> throwArgumentVersionException(argument)
            is ItemTypeArgument -> throwArgumentVersionException(argument)
            is CatTypeArgument -> org.bukkit.Registry.CAT_VARIANT.get(getId(context, argument.name, Registries.CAT_VARIANT))
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
            is DamageTypeArgument -> org.bukkit.Registry.DAMAGE_TYPE.get(getId(context, argument.name, Registries.DAMAGE_TYPE))
            is WolfVariantArgument -> throwArgumentVersionException(argument)
            is PatternTypeArgument -> org.bukkit.Registry.BANNER_PATTERN.get(getId(context, argument.name, Registries.BANNER_PATTERN))
            is ArtArgument -> org.bukkit.Registry.ART.get(getId(context, argument.name, Registries.PAINTING_VARIANT))
            is InstrumentArgument -> org.bukkit.Registry.INSTRUMENT.get(getId(context, argument.name, Registries.INSTRUMENT))
            is EntityTypeArgument -> org.bukkit.Registry.ENTITY_TYPE.get(getId(context, argument.name, Registries.ENTITY_TYPE))
            is PotionArgument -> org.bukkit.Registry.POTION.get(getId(context, argument.name, Registries.POTION))
            is MemoryKeyArgument -> org.bukkit.Registry.MEMORY_MODULE_TYPE.get(getId(context, argument.name, Registries.MEMORY_MODULE_TYPE))
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
        MenuType.CRAFTER_3x3 -> InventoryType.CRAFTER
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
            Component.translatableEscape("argument.resource_or_id.invalid", argument)
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

    private fun getBukkitDisplaySlot(slot: net.minecraft.world.scores.DisplaySlot): DisplaySlot = when (slot) {
        net.minecraft.world.scores.DisplaySlot.LIST -> DisplaySlot.PLAYER_LIST
        net.minecraft.world.scores.DisplaySlot.SIDEBAR -> DisplaySlot.SIDEBAR
        net.minecraft.world.scores.DisplaySlot.BELOW_NAME -> DisplaySlot.BELOW_NAME
        net.minecraft.world.scores.DisplaySlot.TEAM_BLACK -> DisplaySlot.SIDEBAR_BLACK
        net.minecraft.world.scores.DisplaySlot.TEAM_DARK_BLUE -> DisplaySlot.SIDEBAR_DARK_BLUE
        net.minecraft.world.scores.DisplaySlot.TEAM_DARK_GREEN -> DisplaySlot.SIDEBAR_DARK_GREEN
        net.minecraft.world.scores.DisplaySlot.TEAM_DARK_AQUA -> DisplaySlot.SIDEBAR_DARK_AQUA
        net.minecraft.world.scores.DisplaySlot.TEAM_DARK_RED -> DisplaySlot.SIDEBAR_DARK_RED
        net.minecraft.world.scores.DisplaySlot.TEAM_DARK_PURPLE -> DisplaySlot.SIDEBAR_DARK_PURPLE
        net.minecraft.world.scores.DisplaySlot.TEAM_GOLD -> DisplaySlot.SIDEBAR_GOLD
        net.minecraft.world.scores.DisplaySlot.TEAM_GRAY -> DisplaySlot.SIDEBAR_GRAY
        net.minecraft.world.scores.DisplaySlot.TEAM_DARK_GRAY -> DisplaySlot.SIDEBAR_DARK_GRAY
        net.minecraft.world.scores.DisplaySlot.TEAM_BLUE -> DisplaySlot.SIDEBAR_BLUE
        net.minecraft.world.scores.DisplaySlot.TEAM_GREEN -> DisplaySlot.SIDEBAR_GREEN
        net.minecraft.world.scores.DisplaySlot.TEAM_AQUA -> DisplaySlot.SIDEBAR_AQUA
        net.minecraft.world.scores.DisplaySlot.TEAM_RED -> DisplaySlot.SIDEBAR_RED
        net.minecraft.world.scores.DisplaySlot.TEAM_LIGHT_PURPLE -> DisplaySlot.SIDEBAR_LIGHT_PURPLE
        net.minecraft.world.scores.DisplaySlot.TEAM_YELLOW -> DisplaySlot.SIDEBAR_YELLOW
        net.minecraft.world.scores.DisplaySlot.TEAM_WHITE -> DisplaySlot.SIDEBAR_WHITE
        else -> DisplaySlot.SIDEBAR
    }

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
            Particle.DustOptions(Color.fromRGB(
                (options.color.x() * 255.0f).toInt(),
                (options.color.y() * 255.0f).toInt(), (options.color.z() * 255.0f).toInt()
            ), options.scale)
        )
        is ItemParticleOption -> ParticleData<ItemStack>(
            particle,
            CraftItemStack.asBukkitCopy(options.item)
        )
        is VibrationParticleOption -> {
            val level: Level = context.source.level
            if (options.destination is BlockPositionSource) {
                val to: Vec3 = options.destination.getPosition(level).get()
                val location = Location(level.world, to.x(), to.y(), to.z())
                val destination = Vibration.Destination.BlockDestination(location)
                ParticleData(particle, Vibration(location, destination, options.arrivalInTicks))
            } else {
                ParticleData(particle, null)
            }
        }
        is ShriekParticleOption -> ParticleData(particle, options.delay)
        is SculkChargeParticleOptions -> ParticleData(particle, options.roll())
        else -> ParticleData(particle, null)
    }

    private fun getLocation(context: CommandContext<CommandSourceStack>, command: LocationArgument): Location {
        val world = context.source.level.world
        return when (command.type) {
            LocationType.LOCATION_3D -> BlockPosArgument.getBlockPos(context, command.name).toLocation(world)
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