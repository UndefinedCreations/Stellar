package com.undefined.stellar.v1_13_2

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
import com.undefined.stellar.argument.types.entity.EntityDisplayType
import com.undefined.stellar.argument.types.item.ItemSlotArgument
import com.undefined.stellar.argument.types.item.ItemSlotsArgument
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
import com.undefined.stellar.argument.types.world.HeightMapArgument
import com.undefined.stellar.argument.types.world.LocationArgument
import com.undefined.stellar.argument.types.world.LocationType
import com.undefined.stellar.data.argument.Anchor
import com.undefined.stellar.data.argument.Operation
import com.undefined.stellar.data.argument.ParticleData
import com.undefined.stellar.exception.ArgumentVersionMismatchException
import com.undefined.stellar.exception.LiteralArgumentMismatchException
import com.undefined.stellar.exception.UnsupportedArgumentException
import com.undefined.stellar.util.NMSVersion
import com.undefined.stellar.util.ReflectionUtil
import com.undefined.stellar.util.executePrivateMethod
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.server.v1_13_R2.*
import org.bukkit.*
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.craftbukkit.v1_13_R2.block.data.CraftBlockData
import org.bukkit.craftbukkit.v1_13_R2.CraftParticle
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.DisplaySlot
import java.time.Duration
import java.util.*
import java.util.function.Predicate

@Suppress("UNCHECKED_CAST", "DEPRECATION")
object ArgumentHelper {

    fun getLiteralArguments(argument: AbstractStellarArgument<*>): List<ArgumentBuilder<CommandListenerWrapper, *>> {
        val arguments: MutableList<ArgumentBuilder<CommandListenerWrapper, *>> = mutableListOf()
        for (name in argument.aliases + argument.name)
            arguments.add(LiteralArgumentBuilder.literal(name))
        return arguments
    }

    fun getRequiredArgumentBuilder(argument: AbstractStellarArgument<*>): RequiredArgumentBuilder<CommandListenerWrapper, *> {
        val argumentBuilder: RequiredArgumentBuilder<CommandListenerWrapper, *> = RequiredArgumentBuilder.argument(argument.name, getArgumentType(argument))
        getSuggestions(argument)?.let { argumentBuilder.suggests(it) }
        return argumentBuilder
    }

    private fun <T : AbstractStellarArgument<*>> getSuggestions(argument: T): SuggestionProvider<CommandListenerWrapper>? =
        when (argument) {
            is GameEventArgument -> throwArgumentVersionException(argument)
            is PotionEffectTypeArgument -> SuggestionProvider { _, builder ->
                ICompletionProvider.a(IRegistry.MOB_EFFECT.keySet(), builder)
            }
            is VillagerProfessionArgument -> throwArgumentVersionException(argument)
            is VillagerTypeArgument -> throwArgumentVersionException(argument)
            is BiomeArgument -> SuggestionProvider { context, builder ->
                CompletionProviders.d.getSuggestions(context, builder)
            }
            is EntityTypeArgument -> SuggestionProvider { _, builder ->
                ICompletionProvider.a(IRegistry.ENTITY_TYPE.keySet(), builder)
            }
            is MemoryKeyArgument -> throwArgumentVersionException(argument)
            else -> null
        }

    private fun <T : AbstractStellarArgument<*>> getArgumentType(argument: T): ArgumentType<*> =
        when (argument) {
            is ListArgument<*> -> getArgumentType(argument.type)
            is CustomArgument<*> -> getArgumentType(argument.type)
            is StringArgument -> brigadier(argument.type)
            is PhraseArgument -> brigadier(StringType.PHRASE)
            is IntegerArgument -> IntegerArgumentType.integer(argument.min, argument.max)
            is LongArgument -> throwArgumentVersionException(argument)
            is FloatArgument -> FloatArgumentType.floatArg(argument.min, argument.max)
            is DoubleArgument -> DoubleArgumentType.doubleArg(argument.min, argument.max)
            is BooleanArgument -> BoolArgumentType.bool()
            is com.undefined.stellar.argument.types.entity.EntityArgument -> brigadier(argument.type)
            is com.undefined.stellar.argument.types.player.GameProfileArgument -> ArgumentProfile.a()
            is LocationArgument -> when (argument.type) {
                LocationType.LOCATION_3D -> ArgumentPosition.a()
                LocationType.LOCATION_2D -> ArgumentVec2I.a()
                LocationType.PRECISE_LOCATION_2D -> ArgumentVec3.a()
                LocationType.PRECISE_LOCATION_3D -> ArgumentVec2.a()
            }
            is BlockDataArgument -> ArgumentTile.a()
            is com.undefined.stellar.argument.types.block.BlockPredicateArgument -> ArgumentBlockPredicate.a()
            is com.undefined.stellar.argument.types.item.ItemArgument -> ArgumentItemStack.a()
            is com.undefined.stellar.argument.types.item.ItemPredicateArgument -> ArgumentItemPredicate.a()
            is com.undefined.stellar.argument.types.text.ColorArgument -> ArgumentChatFormat.a()
            is com.undefined.stellar.argument.types.text.ComponentArgument -> ArgumentChatComponent.a()
            is com.undefined.stellar.argument.types.text.StyleArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.types.text.MessageArgument -> ArgumentChat.a()
            is com.undefined.stellar.argument.types.scoreboard.ObjectiveArgument -> ArgumentScoreboardObjective.a()
            is com.undefined.stellar.argument.types.scoreboard.ObjectiveCriteriaArgument -> ArgumentScoreboardCriteria.a()
            is com.undefined.stellar.argument.types.math.OperationArgument -> ArgumentMathOperation.a()
            is com.undefined.stellar.argument.types.world.ParticleArgument -> ArgumentParticle.a()
            is com.undefined.stellar.argument.types.math.AngleArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.types.math.RotationArgument -> ArgumentRotation.a()
            is DisplaySlotArgument -> ArgumentScoreboardSlot.a()
            is com.undefined.stellar.argument.types.scoreboard.ScoreHolderArgument -> when (argument.type) {
                ScoreHolderType.SINGLE -> ArgumentScoreholder.a()
                ScoreHolderType.MULTIPLE -> ArgumentScoreholder.b()
            }
            is AxisArgument -> ArgumentRotationAxis.a()
            is com.undefined.stellar.argument.types.scoreboard.TeamArgument -> ArgumentScoreboardTeam.a()
            is ItemSlotArgument -> ArgumentInventorySlot.a()
            is ItemSlotsArgument -> throwArgumentVersionException(argument)
            is NamespacedKeyArgument -> ArgumentMinecraftKeyRegistered.a()
            is com.undefined.stellar.argument.types.entity.EntityAnchorArgument -> ArgumentAnchor.a()
            is com.undefined.stellar.argument.types.math.RangeArgument -> ReflectionUtil.executePrivateMethod<ArgumentCriterionValue<*>, ArgumentCriterionValue<*>>("a")
            is com.undefined.stellar.argument.types.world.DimensionArgument -> ArgumentDimension.a()
            is GameModeArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.types.math.TimeArgument -> throwArgumentVersionException(argument)
            is MirrorArgument -> throwArgumentVersionException(argument)
            is StructureRotationArgument -> throwArgumentVersionException(argument)
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

    fun <T : AbstractStellarArgument<*>> getParsedArgument(context: CommandContext<CommandListenerWrapper>, argument: T): Any? {
        return when (argument) {
            is LiteralStellarArgument -> throw LiteralArgumentMismatchException()
            is CustomArgument<*> -> argument.parse(CommandContextAdapter.getStellarCommandContext(context))
            is StringArgument -> StringArgumentType.getString(context, argument.name)
            is IntegerArgument -> IntegerArgumentType.getInteger(context, argument.name)
            is FloatArgument -> FloatArgumentType.getFloat(context, argument.name)
            is DoubleArgument -> DoubleArgumentType.getDouble(context, argument.name)
            is BooleanArgument -> BoolArgumentType.getBool(context, argument.name)
            is ListArgument<*> -> argument.parse(getParsedArgument(context, argument))
            is com.undefined.stellar.argument.types.entity.EntityArgument -> ArgumentEntity.b(context, argument.name)
                .map { it.bukkitEntity }.toMutableList()
                .addAll(listOf(ArgumentEntity.a(context, argument.name).bukkitEntity))
            is com.undefined.stellar.argument.types.player.GameProfileArgument -> ArgumentProfile.a(context, argument.name)
            is LocationArgument -> getLocation(context, argument)
            is BlockDataArgument -> CraftBlockData.fromData(ArgumentTile.a(context, argument.name).a())
            is com.undefined.stellar.argument.types.block.BlockPredicateArgument -> Predicate<Block> { block: Block ->
                ArgumentBlockPredicate.a(context, argument.name).test(ShapeDetectorBlock(
                    context.source.world,
                    BlockPosition(block.x, block.y, block.z), true
                ))
            }
            is com.undefined.stellar.argument.types.item.ItemArgument -> CraftItemStack.asBukkitCopy(
                ArgumentItemStack.a(context, argument.name).a(1, false)
            )
            is com.undefined.stellar.argument.types.item.ItemPredicateArgument -> Predicate<ItemStack> { item: ItemStack ->
                ArgumentItemPredicate.a(context, argument.name).test(CraftItemStack.asNMSCopy(item))
            }
            is com.undefined.stellar.argument.types.text.ColorArgument -> ArgumentChatFormat.a(
                context,
                argument.name
            ).executePrivateMethod<Int>("e").let { Style.style(TextColor.color(it)) }
            is com.undefined.stellar.argument.types.text.ComponentArgument -> GsonComponentSerializer.gson()
                .deserialize(IChatBaseComponent.ChatSerializer.a(
                    ArgumentChatComponent.a(context, argument.name)
                ))
            is com.undefined.stellar.argument.types.text.StyleArgument -> GsonComponentSerializer.gson().deserialize(
                getArgumentInput(context, argument.name) ?: return null
            ).style()
            is com.undefined.stellar.argument.types.text.MessageArgument -> GsonComponentSerializer.gson().deserialize(
                IChatBaseComponent.ChatSerializer.a(ArgumentChat.a(context, argument.name))
            )
            is com.undefined.stellar.argument.types.scoreboard.ObjectiveArgument -> Bukkit.getScoreboardManager()!!.mainScoreboard.getObjective(
                ArgumentScoreboardObjective.a(context, argument.name).name
            )

            is com.undefined.stellar.argument.types.scoreboard.ObjectiveCriteriaArgument -> ArgumentScoreboardCriteria.a(
                context,
                argument.name
            ).name
            is com.undefined.stellar.argument.types.math.OperationArgument -> Operation.getOperation(
                getArgumentInput(context, argument.name) ?: return null
            )
            is com.undefined.stellar.argument.types.world.ParticleArgument ->  {
                val particleOptions = ArgumentParticle.a(context, argument.name)
                getParticleData(CraftParticle.toBukkit(particleOptions.b()), particleOptions)
            }
            is com.undefined.stellar.argument.types.math.AngleArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.types.math.RotationArgument -> {
                val rotation = ArgumentRotation.a(context, argument.name).a(context.source)
                Location(context.source.world.world, rotation.x, rotation.y, rotation.z)
            }
            is DisplaySlotArgument -> getBukkitDisplaySlot(ArgumentScoreboardSlot.a(context, argument.name))
            is com.undefined.stellar.argument.types.scoreboard.ScoreHolderArgument -> when (argument.type) {
                ScoreHolderType.SINGLE -> ArgumentScoreholder.a(context, argument.name)
                ScoreHolderType.MULTIPLE -> ArgumentScoreholder.b(context, argument.name)
            }
            is AxisArgument -> getBukkitAxis(ArgumentRotationAxis.a(context, argument.name))
            is com.undefined.stellar.argument.types.scoreboard.TeamArgument -> Bukkit.getScoreboardManager()!!.mainScoreboard.getTeam(
                ArgumentScoreboardTeam.a(context, argument.name).name
            )
            is ItemSlotArgument -> ArgumentInventorySlot.a(context, argument.name)
            is ItemSlotsArgument -> throwArgumentVersionException(argument)
            is NamespacedKeyArgument -> NamespacedKey(
                ArgumentMinecraftKeyRegistered.c(context, argument.name).b(),
                ArgumentMinecraftKeyRegistered.c(context, argument.name).key
            )
            is com.undefined.stellar.argument.types.entity.EntityAnchorArgument -> Anchor.getFromName(
                getArgumentInput(context, argument.name) ?: return null
            )
            is com.undefined.stellar.argument.types.math.RangeArgument -> {
                val range = ArgumentCriterionValue.b.a(context, argument.name)
                IntRange(range.a() ?: 1, range.b() ?: 2)
            }
            is com.undefined.stellar.argument.types.world.DimensionArgument -> World.Environment.getEnvironment(ArgumentDimension.a(context, argument.name).dimensionID)
            is GameModeArgument -> throwArgumentVersionException(argument)
            is com.undefined.stellar.argument.types.math.TimeArgument -> Duration.ofSeconds(IntegerArgumentType.getInteger(context, argument.name).toLong() / 20)
            is MirrorArgument -> throwArgumentVersionException(argument)
            is StructureRotationArgument -> throwArgumentVersionException(argument)
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
            is VillagerProfessionArgument -> throwArgumentVersionException(argument)
            is VillagerTypeArgument -> throwArgumentVersionException(argument)
            is MapDecorationTypeArgument -> throwArgumentVersionException(argument)
            is InventoryTypeArgument -> throwArgumentVersionException(argument)
            is AttributeArgument -> throwArgumentVersionException(argument)
            is FluidArgument -> throwArgumentVersionException(argument)
            is SoundArgument -> throwArgumentVersionException(argument)
            is BiomeArgument -> throwArgumentVersionException(argument)
            is StructureArgument -> throwArgumentVersionException(argument)
            is TrimMaterialArgument -> throwArgumentVersionException(argument)
            is TrimPatternArgument -> throwArgumentVersionException(argument)
            is DamageTypeArgument -> throwArgumentVersionException(argument)
            is WolfVariantArgument -> throwArgumentVersionException(argument)
            is PatternTypeArgument -> throwArgumentVersionException(argument)
            is ArtArgument -> throwArgumentVersionException(argument)
            is InstrumentArgument -> throwArgumentVersionException(argument)
            is EntityTypeArgument -> throwArgumentVersionException(argument)
            is PotionArgument -> throwArgumentVersionException(argument)
            is MemoryKeyArgument -> throwArgumentVersionException(argument)
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

    @Throws(CommandSyntaxException::class)
    private fun getId(
        context: CommandContext<CommandListenerWrapper>,
        name: String
    ): NamespacedKey {
        val key = ArgumentMinecraftKeyRegistered.c(context, name)
        return NamespacedKey(key.b(), key.key)
    }

    private fun brigadier(type: StringType): StringArgumentType = when (type) {
        StringType.WORD -> StringArgumentType.word()
        StringType.QUOTABLE_PHRASE -> StringArgumentType.string()
        StringType.PHRASE -> StringArgumentType.greedyString()
    }

    private fun brigadier(type: EntityDisplayType): ArgumentEntity = when (type) {
        EntityDisplayType.ENTITY -> ReflectionUtil.executePrivateMethod<ArgumentEntity, ArgumentEntity>("a")
        EntityDisplayType.ENTITIES -> ArgumentEntity.b()
        EntityDisplayType.PLAYER -> ArgumentEntity.c()
        EntityDisplayType.PLAYERS -> ArgumentEntity.d()
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
    private fun toLocation(world: World, position: ArgumentVec2I.a) =
        Location(world, position.a.toDouble(), 0.0, position.b.toDouble())
    private fun toLocation(world: World, vec: Vec3D) =
        Location(world, vec.x, vec.y, vec.z)
    private fun toLocation(world: World, vec: Vec2F) =
        Location(world, vec.i.toDouble(), 0.0, vec.j.toDouble())

    private fun throwArgumentVersionException(argument: AbstractStellarArgument<*>): Nothing =
        throw ArgumentVersionMismatchException(argument, NMSVersion.version)

}