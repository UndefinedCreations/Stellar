package com.undefined.stellar.v1_15_2

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
import com.undefined.stellar.argument.basic.*
import com.undefined.stellar.argument.block.BlockDataArgument
import com.undefined.stellar.argument.item.ItemSlotArgument
import com.undefined.stellar.argument.item.ItemSlotsArgument
import com.undefined.stellar.argument.misc.NamespacedKeyArgument
import com.undefined.stellar.argument.misc.UUIDArgument
import com.undefined.stellar.argument.player.GameModeArgument
import com.undefined.stellar.argument.registry.*
import com.undefined.stellar.argument.structure.LootTableArgument
import com.undefined.stellar.argument.structure.MirrorArgument
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
import com.undefined.stellar.util.ReflectionUtil
import com.undefined.stellar.util.executePrivateMethod
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.server.v1_15_R1.*
import org.bukkit.*
import org.bukkit.Particle
import org.bukkit.Registry
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.craftbukkit.v1_15_R1.CraftParticle
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.DisplaySlot
import java.util.*
import java.util.function.Predicate

@Suppress("UNCHECKED_CAST", "DEPRECATION")
object ArgumentHelper {

    fun getLiteralArguments(argument: AbstractStellarArgument<*, *>): List<ArgumentBuilder<CommandListenerWrapper, *>> {
        val arguments: MutableList<ArgumentBuilder<CommandListenerWrapper, *>> = mutableListOf()
        for (name in argument.aliases + argument.name)
            arguments.add(LiteralArgumentBuilder.literal(name))
        return arguments
    }

    fun getRequiredArgumentBuilder(argument: AbstractStellarArgument<*, *>): RequiredArgumentBuilder<CommandListenerWrapper, *> {
        val argumentBuilder: RequiredArgumentBuilder<CommandListenerWrapper, *> = RequiredArgumentBuilder.argument(argument.name, getArgumentType(argument))
        getSuggestions(argument)?.let { argumentBuilder.suggests(it) }
        return argumentBuilder
    }

    private fun <T : AbstractStellarArgument<*, *>> getSuggestions(argument: T): SuggestionProvider<CommandListenerWrapper>? =
        when (argument) {
            is GameEventArgument -> throwArgumentVersionException(argument)
            is PotionEffectTypeArgument -> SuggestionProvider { _, builder ->
                ICompletionProvider.a(IRegistry.MOB_EFFECT.keySet(), builder)
            }
            is VillagerProfessionArgument -> SuggestionProvider { _, builder ->
                ICompletionProvider.a(IRegistry.VILLAGER_PROFESSION.keySet(), builder)
            }
            is VillagerTypeArgument -> SuggestionProvider { _, builder ->
                ICompletionProvider.a(IRegistry.VILLAGER_TYPE.keySet(), builder)
            }
            is BiomeArgument -> SuggestionProvider { context, builder ->
                CompletionProviders.d.getSuggestions(context, builder)
            }
            is EntityTypeArgument -> SuggestionProvider { _, builder ->
                ICompletionProvider.a(IRegistry.ENTITY_TYPE.keySet(), builder)
            }
            is MemoryKeyArgument -> SuggestionProvider { _, builder ->
                ICompletionProvider.a(IRegistry.MEMORY_MODULE_TYPE.keySet(), builder)
            }
            else -> null
        }

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
            is com.undefined.stellar.argument.player.GameProfileArgument -> ArgumentProfile.a()
            is LocationArgument -> when (argument.type) {
                LocationType.LOCATION_3D -> ArgumentPosition.a()
                LocationType.LOCATION_2D -> ArgumentVec2I.a()
                LocationType.PRECISE_LOCATION_2D -> ArgumentVec3.a()
                LocationType.PRECISE_LOCATION_3D -> ArgumentVec2.a()
            }
            is BlockDataArgument -> ArgumentTile.a()
            is com.undefined.stellar.argument.block.BlockPredicateArgument -> ArgumentBlockPredicate.a()
            is com.undefined.stellar.argument.item.ItemArgument -> ArgumentItemStack.a()
            is com.undefined.stellar.argument.item.ItemPredicateArgument -> ArgumentItemPredicate.a()
            is com.undefined.stellar.argument.text.ColorArgument -> ArgumentChatFormat.a()
            is com.undefined.stellar.argument.text.ComponentArgument -> ArgumentChatComponent.a()
            is com.undefined.stellar.argument.text.StyleArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.text.MessageArgument -> ArgumentChat.a()
            is com.undefined.stellar.argument.scoreboard.ObjectiveArgument -> ArgumentScoreboardObjective.a()
            is com.undefined.stellar.argument.scoreboard.ObjectiveCriteriaArgument -> ArgumentScoreboardCriteria.a()
            is com.undefined.stellar.argument.math.OperationArgument -> ArgumentMathOperation.a()
            is com.undefined.stellar.argument.world.ParticleArgument -> ArgumentParticle.a()
            is com.undefined.stellar.argument.math.AngleArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.math.RotationArgument -> ArgumentRotation.a()
            is com.undefined.stellar.argument.scoreboard.DisplaySlotArgument -> ArgumentScoreboardSlot.a()
            is com.undefined.stellar.argument.scoreboard.ScoreHolderArgument -> when (argument.type) {
                com.undefined.stellar.argument.scoreboard.ScoreHolderType.SINGLE -> ArgumentScoreholder.a()
                com.undefined.stellar.argument.scoreboard.ScoreHolderType.MULTIPLE -> ArgumentScoreholder.b()
            }
            is com.undefined.stellar.argument.math.AxisArgument -> ArgumentRotationAxis.a()
            is com.undefined.stellar.argument.scoreboard.TeamArgument -> ArgumentScoreboardTeam.a()
            is ItemSlotArgument -> ArgumentInventorySlot.a()
            is ItemSlotsArgument -> throwArgumentVersionException(argument)
            is NamespacedKeyArgument -> ArgumentMinecraftKeyRegistered.a()
            is com.undefined.stellar.argument.entity.EntityAnchorArgument -> ArgumentAnchor.a()
            is com.undefined.stellar.argument.math.RangeArgument -> ReflectionUtil.executePrivateMethod<ArgumentCriterionValue<*>, ArgumentCriterionValue<*>>("a")
            is com.undefined.stellar.argument.world.EnvironmentArgument -> ArgumentDimension.a()
            is GameModeArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.math.TimeArgument -> ArgumentTime.a()
            is MirrorArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.structure.StructureRotationArgument -> throwArgumentVersionException(argument)
            is HeightMapArgument -> throwArgumentVersionException(argument)
            is LootTableArgument -> throwArgumentVersionException(argument)
            is UUIDArgument -> throwArgumentVersionException(argument)
            is GameEventArgument -> ArgumentMinecraftKeyRegistered.a()
            is StructureTypeArgument -> throwArgumentVersionException(argument)
            is PotionEffectTypeArgument -> throwArgumentVersionException(argument)
            is BlockTypeArgument -> throwArgumentVersionException(argument)
            is ItemTypeArgument -> throwArgumentVersionException(argument)
            is CatTypeArgument -> throwArgumentVersionException(argument)
            is FrogVariantArgument -> throwArgumentVersionException(argument)
            is VillagerProfessionArgument -> ArgumentMinecraftKeyRegistered.a()
            is VillagerTypeArgument -> ArgumentMinecraftKeyRegistered.a()
            is MapDecorationTypeArgument -> throwArgumentVersionException(argument)
            is InventoryTypeArgument -> throwArgumentVersionException(argument)
            is AttributeArgument -> ArgumentMinecraftKeyRegistered.a()
            is FluidArgument -> throwArgumentVersionException(argument)
            is SoundArgument -> throwArgumentVersionException(argument)
            is BiomeArgument -> ArgumentMinecraftKeyRegistered.a()
            is StructureArgument -> throwArgumentVersionException(argument)
            is TrimMaterialArgument -> throwArgumentVersionException(argument)
            is TrimPatternArgument -> throwArgumentVersionException(argument)
            is DamageTypeArgument -> throwArgumentVersionException(argument)
            is WolfVariantArgument -> throwArgumentVersionException(argument)
            is PatternTypeArgument -> throwArgumentVersionException(argument)
            is ArtArgument -> throwArgumentVersionException(argument)
            is InstrumentArgument -> throwArgumentVersionException(argument)
            is EntityTypeArgument -> ArgumentMinecraftKeyRegistered.a()
            is PotionArgument -> throwArgumentVersionException(argument)
            is MemoryKeyArgument -> ArgumentMinecraftKeyRegistered.a()
            else -> throw UnsupportedArgumentException(argument)
        }

    fun <T : AbstractStellarArgument<*, *>> getParsedArgument(context: CommandContext<CommandListenerWrapper>, argument: T): Any? {
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
            is com.undefined.stellar.argument.entity.EntityArgument -> ArgumentEntity.b(context, argument.name)
                .map { it.bukkitEntity }.toMutableList()
                .addAll(listOf(ArgumentEntity.a(context, argument.name).bukkitEntity))
            is com.undefined.stellar.argument.player.GameProfileArgument -> ArgumentProfile.a(context, argument.name)
            is LocationArgument -> getLocation(context, argument)
            is BlockDataArgument -> CraftBlockData.fromData(ArgumentTile.a(context, argument.name).a())
            is com.undefined.stellar.argument.block.BlockPredicateArgument -> Predicate<Block> { block: Block ->
                ArgumentBlockPredicate.a(context, argument.name).test(ShapeDetectorBlock(
                    context.source.world,
                    BlockPosition(block.x, block.y, block.z), true
                ))
            }
            is com.undefined.stellar.argument.item.ItemArgument -> CraftItemStack.asBukkitCopy(
                ArgumentItemStack.a(context, argument.name).a(1, false)
            )
            is com.undefined.stellar.argument.item.ItemPredicateArgument -> Predicate<ItemStack> { item: ItemStack ->
                ArgumentItemPredicate.a(context, argument.name).test(CraftItemStack.asNMSCopy(item))
            }
            is com.undefined.stellar.argument.text.ColorArgument -> ChatColor.getByChar(ArgumentChatFormat.a(
                context,
                argument.name
            ).character)
            is com.undefined.stellar.argument.text.ComponentArgument -> GsonComponentSerializer.gson()
                .deserialize(IChatBaseComponent.ChatSerializer.a(
                    ArgumentChatComponent.a(context, argument.name)
                ))
            is com.undefined.stellar.argument.text.StyleArgument -> GsonComponentSerializer.gson().deserialize(
                getArgumentInput(context, argument.name) ?: return null
            ).style()
            is com.undefined.stellar.argument.text.MessageArgument -> GsonComponentSerializer.gson().deserialize(
                IChatBaseComponent.ChatSerializer.a(ArgumentChat.a(context, argument.name))
            )
            is com.undefined.stellar.argument.scoreboard.ObjectiveArgument -> Bukkit.getScoreboardManager()!!.mainScoreboard.getObjective(
                ArgumentScoreboardObjective.a(context, argument.name).name
            )

            is com.undefined.stellar.argument.scoreboard.ObjectiveCriteriaArgument -> ArgumentScoreboardCriteria.a(
                context,
                argument.name
            ).name
            is com.undefined.stellar.argument.math.OperationArgument -> Operation.getOperation(
                getArgumentInput(context, argument.name) ?: return null
            )
            is com.undefined.stellar.argument.world.ParticleArgument ->  {
                val particleOptions = ArgumentParticle.a(context, argument.name)
                getParticleData(CraftParticle.toBukkit(particleOptions.particle), particleOptions)
            }
            is com.undefined.stellar.argument.math.AngleArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.math.RotationArgument -> {
                val rotation = ArgumentRotation.a(context, argument.name).a(context.source)
                Location(context.source.world.world, rotation.x, rotation.y, rotation.z)
            }
            is com.undefined.stellar.argument.scoreboard.DisplaySlotArgument -> getBukkitDisplaySlot(ArgumentScoreboardSlot.a(context, argument.name))
            is com.undefined.stellar.argument.scoreboard.ScoreHolderArgument -> when (argument.type) {
                com.undefined.stellar.argument.scoreboard.ScoreHolderType.SINGLE -> ArgumentScoreholder.a(context, argument.name)
                com.undefined.stellar.argument.scoreboard.ScoreHolderType.MULTIPLE -> ArgumentScoreholder.b(context, argument.name)
            }
            is com.undefined.stellar.argument.math.AxisArgument -> getBukkitAxis(ArgumentRotationAxis.a(context, argument.name))
            is com.undefined.stellar.argument.scoreboard.TeamArgument -> Bukkit.getScoreboardManager()!!.mainScoreboard.getTeam(
                ArgumentScoreboardTeam.a(context, argument.name).name
            )
            is ItemSlotArgument -> ArgumentInventorySlot.a(context, argument.name)
            is ItemSlotsArgument -> throwArgumentVersionException(argument)
            is NamespacedKeyArgument -> NamespacedKey(
                ArgumentMinecraftKeyRegistered.d(context, argument.name).namespace,
                ArgumentMinecraftKeyRegistered.d(context, argument.name).key
            )
            is com.undefined.stellar.argument.entity.EntityAnchorArgument -> EntityAnchor.getFromName(
                getArgumentInput(context, argument.name) ?: return null
            )
            is com.undefined.stellar.argument.math.RangeArgument -> {
                val range = ArgumentCriterionValue.b.a(context, argument.name)
                IntRange(range.a() ?: 1, range.b() ?: 2)
            }
            is com.undefined.stellar.argument.world.EnvironmentArgument -> World.Environment.getEnvironment(ArgumentDimension.a(context, argument.name).dimensionID)
            is GameModeArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.math.TimeArgument -> IntegerArgumentType.getInteger(context, argument.name).toLong()
            is MirrorArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.structure.StructureRotationArgument -> throwArgumentVersionException(argument)
            is HeightMapArgument -> throwArgumentVersionException(argument)
            is LootTableArgument -> throwArgumentVersionException(argument)
            is UUIDArgument -> throwArgumentVersionException(argument)
            is GameEventArgument -> throwArgumentVersionException(argument)
            is StructureTypeArgument -> throwArgumentVersionException(argument)
            is PotionEffectTypeArgument -> throwArgumentVersionException(argument)
            is BlockTypeArgument -> throwArgumentVersionException(argument)
            is ItemTypeArgument -> throwArgumentVersionException(argument)
            is CatTypeArgument -> throwArgumentVersionException(argument)
            is FrogVariantArgument -> throwArgumentVersionException(argument)
            is VillagerProfessionArgument -> Registry.VILLAGER_PROFESSION.get(getId(context, argument.name))
            is VillagerTypeArgument -> Registry.VILLAGER_TYPE.get(getId(context, argument.name))
            is MapDecorationTypeArgument -> throwArgumentVersionException(argument)
            is InventoryTypeArgument -> getInventoryType(
                IRegistry.MENU[ArgumentMinecraftKeyRegistered.d(context, argument.name)]
            )
            is AttributeArgument -> throwArgumentVersionException(argument)
            is FluidArgument -> throwArgumentVersionException(argument)
            is SoundArgument -> throwArgumentVersionException(argument)
            is BiomeArgument -> Registry.BIOME.get(getId(context, argument.name))
            is StructureArgument -> throwArgumentVersionException(argument)
            is TrimMaterialArgument -> throwArgumentVersionException(argument)
            is TrimPatternArgument -> throwArgumentVersionException(argument)
            is DamageTypeArgument -> throwArgumentVersionException(argument)
            is WolfVariantArgument -> throwArgumentVersionException(argument)
            is PatternTypeArgument -> throwArgumentVersionException(argument)
            is ArtArgument -> throwArgumentVersionException(argument)
            is InstrumentArgument -> throwArgumentVersionException(argument)
            is EntityTypeArgument -> Registry.ENTITY_TYPE.get(getId(context, argument.name))
            is PotionArgument -> throwArgumentVersionException(argument)
            is MemoryKeyArgument -> Registry.MEMORY_MODULE_TYPE.get(getId(context, argument.name))
            else -> throw UnsupportedArgumentException(argument)
        }
    }

    fun getArgumentInput(context: CommandContext<CommandListenerWrapper>, name: String): String? {
        val field = CommandContext::class.java.getDeclaredField("arguments")
        field.isAccessible = true
        val arguments: Map<String, ParsedArgument<CommandListenerWrapper, *>> = field.get(context) as Map<String, ParsedArgument<CommandListenerWrapper, *>>
        val argument = arguments[name] ?: return null
        val range = StringRange.between(argument.range.start, context.input.length)
        return range.get(context.input)
    }

    private fun getInventoryType(menu: Containers<*>?): InventoryType = when (menu) {
        Containers.GENERIC_9X1 -> InventoryType.CHEST
        Containers.GENERIC_9X2 -> InventoryType.CHEST
        Containers.GENERIC_9X3 -> InventoryType.CHEST
        Containers.GENERIC_9X4 -> InventoryType.CHEST
        Containers.GENERIC_9X5 -> InventoryType.CHEST
        Containers.GENERIC_9X6 -> InventoryType.CHEST
        Containers.GENERIC_3X3 -> InventoryType.WORKBENCH
        Containers.ANVIL -> InventoryType.ANVIL
        Containers.BEACON -> InventoryType.BEACON
        Containers.BLAST_FURNACE -> InventoryType.BLAST_FURNACE
        Containers.BREWING_STAND -> InventoryType.BREWING
        Containers.CRAFTING -> InventoryType.CRAFTING
        Containers.ENCHANTMENT -> InventoryType.ENCHANTING
        Containers.FURNACE -> InventoryType.FURNACE
        Containers.GRINDSTONE -> InventoryType.GRINDSTONE
        Containers.HOPPER -> InventoryType.HOPPER
        Containers.LECTERN -> InventoryType.LECTERN
        Containers.LOOM -> InventoryType.LOOM
        Containers.MERCHANT -> InventoryType.MERCHANT
        Containers.SHULKER_BOX -> InventoryType.SHULKER_BOX
        Containers.SMOKER -> InventoryType.SMOKER
        Containers.CARTOGRAPHY_TABLE -> InventoryType.CARTOGRAPHY
        Containers.STONECUTTER -> InventoryType.STONECUTTER
        else -> throw IllegalStateException("No inventory type found! This is not intentional behaviour, please contact the developers.")
    }

    @Throws(CommandSyntaxException::class)
    private fun getId(
        context: CommandContext<CommandListenerWrapper>,
        name: String
    ): NamespacedKey {
        val key = ArgumentMinecraftKeyRegistered.d(context, name)
        return NamespacedKey(key.namespace, key.key)
    }

    private fun brigadier(type: StringType): StringArgumentType = when (type) {
        StringType.WORD -> StringArgumentType.word()
        StringType.QUOTABLE_PHRASE -> StringArgumentType.string()
        StringType.PHRASE -> StringArgumentType.greedyString()
    }

    private fun brigadier(type: com.undefined.stellar.argument.entity.EntityDisplayType): ArgumentEntity = when (type) {
        com.undefined.stellar.argument.entity.EntityDisplayType.ENTITY -> ReflectionUtil.executePrivateMethod<ArgumentEntity, ArgumentEntity>("a")
        com.undefined.stellar.argument.entity.EntityDisplayType.ENTITIES -> ArgumentEntity.multipleEntities()
        com.undefined.stellar.argument.entity.EntityDisplayType.PLAYER -> ArgumentEntity.c()
        com.undefined.stellar.argument.entity.EntityDisplayType.PLAYERS -> ArgumentEntity.d()
    }

    private fun getBukkitAxis(argument: EnumSet<EnumDirection.EnumAxis>): EnumSet<Axis> =
        argument.mapTo(EnumSet.noneOf(Axis::class.java)) {
            when (it) {
                EnumDirection.EnumAxis.X -> Axis.X
                EnumDirection.EnumAxis.Y -> Axis.Y
                EnumDirection.EnumAxis.Z -> Axis.Z
                null -> Axis.X
            }
        }

    private fun getBukkitDisplaySlot(slot: Int): DisplaySlot = when (slot) {
        0 -> DisplaySlot.PLAYER_LIST
        2 -> DisplaySlot.BELOW_NAME
        else -> DisplaySlot.SIDEBAR
    }

    private fun getParticleData(
        particle: Particle,
        particleOptions: ParticleParam
    ): ParticleData<*> = when (particleOptions) {
        is ParticleType -> ParticleData(particle, null)
        is ParticleParamBlock -> ParticleData<BlockData>(particle, CraftBlockData.fromData(particleOptions.executePrivateMethod("c")))
        is ParticleParamRedstone -> {
            val colors = particleOptions.a().split(" ")
            val red = colors[1].toFloat()
            val green = colors[2].toFloat()
            val blue = colors[3].toFloat()
            val scale = colors[4].toFloat()
            ParticleData(
                particle,
                Particle.DustOptions(
                    Color.fromRGB(
                        (red * 255.0f).toInt(),
                        (green * 255.0f).toInt(), (blue * 255.0f).toInt()
                    ), scale
                )
            )
        }
        is ParticleParamItem -> ParticleData<ItemStack>(
            particle,
            CraftItemStack.asBukkitCopy(particleOptions.executePrivateMethod("c"))
        )
        else -> ParticleData(particle, null)
    }

    private fun getLocation(context: CommandContext<CommandListenerWrapper>, command: LocationArgument): Location {
        val world = context.source.world.world
        return when (command.type) {
            LocationType.LOCATION_3D -> toLocation(world, context.getArgument(command.name, IVectorPosition::class.java).c(context.source))
            LocationType.LOCATION_2D -> toLocation(world, ArgumentVec2I.a(context, command.name))
            LocationType.PRECISE_LOCATION_3D -> toLocation(world, ArgumentVec3.a(context, command.name))
            LocationType.PRECISE_LOCATION_2D -> toLocation(world, ArgumentVec2.a(context, command.name))
        }
    }

    private fun toLocation(world: World, position: BlockPosition) =
        Location(world, position.x.toDouble(), position.y.toDouble(), position.z.toDouble())
    private fun toLocation(world: World, position: BlockPosition2D) =
        Location(world, position.a.toDouble(), 0.0, position.b.toDouble())
    private fun toLocation(world: World, vec: Vec3D) =
        Location(world, vec.x, vec.y, vec.z)
    private fun toLocation(world: World, vec: Vec2F) =
        Location(world, vec.i.toDouble(), 0.0, vec.j.toDouble())

    private fun throwArgumentVersionException(argument: AbstractStellarArgument<*, *>): Nothing =
        throw ArgumentVersionMismatchException(argument, NMSVersion.version)

}