package com.undefined.stellar.v1_20_6

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.ParsedArgument
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.undefined.stellar.data.Operation
import com.undefined.stellar.data.ParticleData
import com.undefined.stellar.exception.ServerTypeMismatchException
import com.undefined.stellar.exception.UnsupportedSubCommandException
import com.undefined.stellar.sub.brigadier.BrigadierTypeSubCommand
import com.undefined.stellar.sub.brigadier.entity.EntityDisplayType
import com.undefined.stellar.sub.brigadier.entity.EntitySubCommand
import com.undefined.stellar.sub.brigadier.item.ItemPredicateSubCommand
import com.undefined.stellar.sub.brigadier.item.ItemSubCommand
import com.undefined.stellar.sub.brigadier.math.OperationSubCommand
import com.undefined.stellar.sub.brigadier.player.GameProfileSubCommand
import com.undefined.stellar.sub.brigadier.primitive.*
import com.undefined.stellar.sub.brigadier.scoreboard.ObjectiveCriteriaSubCommand
import com.undefined.stellar.sub.brigadier.scoreboard.ObjectiveSubCommand
import com.undefined.stellar.sub.brigadier.text.*
import com.undefined.stellar.sub.brigadier.world.*
import com.undefined.stellar.sub.custom.EnumSubCommand
import com.undefined.stellar.sub.custom.ListSubCommand
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSource
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.*
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument
import net.minecraft.commands.arguments.blocks.BlockStateArgument
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument
import net.minecraft.commands.arguments.coordinates.Vec2Argument
import net.minecraft.commands.arguments.coordinates.Vec3Argument
import net.minecraft.commands.arguments.item.ItemArgument
import net.minecraft.commands.arguments.item.ItemPredicateArgument
import net.minecraft.core.BlockPos
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
import org.bukkit.craftbukkit.CraftParticle
import org.bukkit.craftbukkit.CraftServer
import org.bukkit.craftbukkit.block.data.CraftBlockData
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack
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
            is StringSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, subCommand.type.brigadier())
            is IntegerSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, IntegerArgumentType.integer(subCommand.min, subCommand.max))
            is LongSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, LongArgumentType.longArg(subCommand.min, subCommand.max))
            is FloatSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, FloatArgumentType.floatArg(subCommand.min, subCommand.max))
            is DoubleSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, DoubleArgumentType.doubleArg(subCommand.min, subCommand.max))
            is BooleanSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, BoolArgumentType.bool())
            is ListSubCommand<*> -> RequiredArgumentBuilder.argument<CommandSourceStack, String>(subCommand.name, StringArgumentType.word()).suggestStringList(subCommand.getStringList())
            is EnumSubCommand<*> -> RequiredArgumentBuilder.argument<CommandSourceStack, String>(subCommand.name, StringArgumentType.word()).suggestStringList(subCommand.getStringList())
            is EntitySubCommand -> RequiredArgumentBuilder.argument(subCommand.name, subCommand.type.brigadier())
            is GameProfileSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, GameProfileArgument.gameProfile())
            is LocationSubCommand -> when (subCommand.type) {
                    LocationType.LOCATION3D -> RequiredArgumentBuilder.argument(subCommand.name, BlockPosArgument.blockPos())
                    LocationType.LOCATION2D -> RequiredArgumentBuilder.argument(subCommand.name, ColumnPosArgument.columnPos())
                    LocationType.DOUBLE_LOCATION_3D -> RequiredArgumentBuilder.argument(subCommand.name, Vec3Argument.vec3())
                    LocationType.DOUBLE_LOCATION_2D -> RequiredArgumentBuilder.argument(subCommand.name, Vec2Argument.vec2())
            }
            is BlockDataSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, BlockStateArgument.block(COMMAND_BUILD_CONTEXT))
            is BlockPredicateSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, BlockPredicateArgument.blockPredicate(COMMAND_BUILD_CONTEXT))
            is ItemSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, ItemArgument.item(COMMAND_BUILD_CONTEXT))
            is ItemPredicateSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, ItemPredicateArgument.itemPredicate(COMMAND_BUILD_CONTEXT))
            is ColorSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, ColorArgument.color())
            is ComponentSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, ComponentArgument.textComponent(COMMAND_BUILD_CONTEXT))
            is StyleSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, StyleArgument.style(COMMAND_BUILD_CONTEXT))
            is MessageSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, MessageArgument.message())
            is ObjectiveSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, ObjectiveArgument.objective())
            is ObjectiveCriteriaSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, ObjectiveCriteriaArgument.criteria())
            is OperationSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, OperationArgument.operation())
            is ParticleSubCommand -> RequiredArgumentBuilder.argument(subCommand.name, ParticleArgument.particle(COMMAND_BUILD_CONTEXT))
            else -> throw UnsupportedSubCommandException()
        }

    fun <T : BrigadierTypeSubCommand<*>> handleNativeSubCommandExecutors(subCommand: T, context: CommandContext<CommandSourceStack>) =
        when (subCommand) {
            is StringSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, StringArgumentType.getString(context, subCommand.name)) }
            is IntegerSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, IntegerArgumentType.getInteger(context, subCommand.name)) }
            is FloatSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, FloatArgumentType.getFloat(context, subCommand.name)) }
            is DoubleSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, DoubleArgumentType.getDouble(context, subCommand.name)) }
            is BooleanSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, BoolArgumentType.getBool(context, subCommand.name)) }
            is ListSubCommand<*> -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, subCommand.parse.invoke(StringArgumentType.getString(context, subCommand.name))!!) }
            is EnumSubCommand<*> -> subCommand.customExecutions.forEach { execution ->
                val enum = subCommand.parse(StringArgumentType.getString(context, subCommand.name))
                enum?.let { execution.run(context.source.bukkitSender, enum) }
            }
            is EntitySubCommand -> {
                subCommand.pluralEntitiesExecutions.forEach {
                    it.run(context.source.bukkitSender, EntityArgument.getEntities(context, subCommand.name).map { it.bukkitEntity })
                }
                subCommand.singularEntityExecutions.forEach {
                    it.run(context.source.bukkitSender, EntityArgument.getEntity(context, subCommand.name).bukkitEntity)
                }
            }
            is GameProfileSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, GameProfileArgument.getGameProfiles(context, subCommand.name)) }
            is LocationSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, getLocation(context, subCommand)) }
            is BlockDataSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, CraftBlockData.fromData(BlockStateArgument.getBlock(context, subCommand.name).state)) }
            is BlockPredicateSubCommand -> subCommand.customExecutions.forEach {
                it.run(context.source.bukkitSender, Predicate<Block> { block: Block ->
                    BlockPredicateArgument.getBlockPredicate(context, subCommand.name).test(BlockInWorld(
                        context.source.level,
                        BlockPos(block.x, block.y, block.z), true
                    ))
                })
            }
            is ItemSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, CraftItemStack.asBukkitCopy(ItemArgument.getItem(context, subCommand.name).createItemStack(1, false))) }
            is ItemPredicateSubCommand -> subCommand.customExecutions.forEach {
                it.run(context.source.bukkitSender, Predicate<ItemStack> { item: ItemStack ->
                    ItemPredicateArgument.getItemPredicate(context, subCommand.name).test(CraftItemStack.asNMSCopy(item))
                })
            }
            is ColorSubCommand -> subCommand.customExecutions.forEach { execution ->
                val color = ColorArgument.getColor(context, subCommand.name)
                execution.run(context.source.bukkitSender, color.color?.let { Style.style(TextColor.color(it)) } ?: Style.empty())
            }
            is ComponentSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, GsonComponentSerializer.gson().deserialize(net.minecraft.network.chat.Component.Serializer.toJson(ComponentArgument.getComponent(context, subCommand.name), COMMAND_BUILD_CONTEXT))) }
            is StyleSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, GsonComponentSerializer.gson().deserialize(context.input.substringAfter(' ')).style()) } // TODO("Broken")
            is MessageSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, GsonComponentSerializer.gson().deserialize(net.minecraft.network.chat.Component.Serializer.toJson(MessageArgument.getMessage(context, subCommand.name), COMMAND_BUILD_CONTEXT))) }
            is ObjectiveSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, Bukkit.getScoreboardManager().mainScoreboard.getObjective(ObjectiveArgument.getObjective(context, subCommand.name).name) ?: return@forEach) }
            is ObjectiveCriteriaSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, ObjectiveCriteriaArgument.getCriteria(context, subCommand.name).name) }
            is OperationSubCommand -> subCommand.customExecutions.forEach { it.run(context.source.bukkitSender, Operation.getOperation(getArgumentInput(context, subCommand.name)) ?: return) }
            is ParticleSubCommand -> subCommand.customExecutions.forEach {
                val particleOptions = ParticleArgument.getParticle(context, subCommand.name)
                val particle = CraftParticle.minecraftToBukkit(particleOptions.type)
                it.run(context.source.bukkitSender, getParticleData(context, particle, particleOptions))
            }
            else -> throw UnsupportedSubCommandException()
        }

    fun <T : BrigadierTypeSubCommand<*>> handleNativeSubCommandRunnables(subCommand: T, context: CommandContext<CommandSourceStack>): Boolean {
        when (subCommand) {
            is StringSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, StringArgumentType.getString(context, subCommand.name))) return false }
            is IntegerSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, IntegerArgumentType.getInteger(context, subCommand.name))) return false }
            is FloatSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, FloatArgumentType.getFloat(context, subCommand.name))) return false }
            is DoubleSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, DoubleArgumentType.getDouble(context, subCommand.name))) return false }
            is BooleanSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, BoolArgumentType.getBool(context, subCommand.name))) return false }
            is ListSubCommand<*> -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, subCommand.parse.invoke(StringArgumentType.getString(context, subCommand.name))!!)) return false }
            is EnumSubCommand<*> -> subCommand.customRunnables.forEach { runnable ->
                val enum = subCommand.parse(StringArgumentType.getString(context, subCommand.name))
                enum?.let { if (!runnable.run(context.source.bukkitSender, enum)) return false }
            }
            is EntitySubCommand -> {
                subCommand.pluralEntitiesRunnables.forEach { runnable ->
                    if (!runnable.run(context.source.bukkitSender, EntityArgument.getEntities(context, subCommand.name).map { it.bukkitEntity })) return false
                }
                subCommand.singularEntityRunnables.forEach { runnable ->
                    if (!runnable.run(context.source.bukkitSender, EntityArgument.getEntity(context, subCommand.name).bukkitEntity)) return false
                }
            }
            is GameProfileSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, GameProfileArgument.getGameProfiles(context, subCommand.name))) return false }
            is LocationSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, getLocation(context, subCommand))) return false }
            is BlockDataSubCommand -> {
                subCommand.customRunnables.forEach {
                    val state = BlockStateArgument.getBlock(context, subCommand.name).state
                    if (!it.run(context.source.bukkitSender, CraftBlockData.fromData(state))) return false
                }
            }
            is BlockPredicateSubCommand -> {
                subCommand.customRunnables.forEach {
                    val continueOtherExecutions = it.run(context.source.bukkitSender, Predicate<Block> { block: Block ->
                            BlockPredicateArgument.getBlockPredicate(context, subCommand.name).test(BlockInWorld(
                                context.source.level,
                                BlockPos(block.x, block.y, block.z), true)
                            )
                        })
                    if (!continueOtherExecutions) return false
                }
            }
            is ItemSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, CraftItemStack.asBukkitCopy(ItemArgument.getItem(context, subCommand.name).createItemStack(1, false)))) return false }
            is ItemPredicateSubCommand -> subCommand.customRunnables.forEach {
                if (!it.run(context.source.bukkitSender, Predicate<ItemStack> { item: ItemStack ->
                    ItemPredicateArgument.getItemPredicate(context, subCommand.name).test(CraftItemStack.asNMSCopy(item))
                })) return false
            }
            is ColorSubCommand -> subCommand.customRunnables.forEach { runnable ->
                val color = ColorArgument.getColor(context, subCommand.name)
                if (!runnable.run(context.source.bukkitSender, color.color?.let { Style.style(TextColor.color(it)) } ?: Style.empty())) return false
            }
            is ComponentSubCommand -> subCommand.customRunnables.forEach { runnable -> if (!runnable.run(context.source.bukkitSender, GsonComponentSerializer.gson().deserialize(net.minecraft.network.chat.Component.Serializer.toJson(ComponentArgument.getComponent(context, subCommand.name), COMMAND_BUILD_CONTEXT)))) return false }
            is StyleSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, GsonComponentSerializer.gson().deserialize(context.input.substringAfter(' ')).style())) return false }
            is MessageSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, GsonComponentSerializer.gson().deserialize(net.minecraft.network.chat.Component.Serializer.toJson(MessageArgument.getMessage(context, subCommand.name), COMMAND_BUILD_CONTEXT)))) return false }
            is ObjectiveSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, Bukkit.getScoreboardManager().mainScoreboard.getObjective(ObjectiveArgument.getObjective(context, subCommand.name).name) ?: return false)) return false }
            is ObjectiveCriteriaSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, ObjectiveCriteriaArgument.getCriteria(context, subCommand.name).name)) return false }
            is OperationSubCommand -> subCommand.customRunnables.forEach { if (!it.run(context.source.bukkitSender, Operation.getOperation(getArgumentInput(context, subCommand.name)) ?: return false)) return false }
            is ParticleSubCommand -> subCommand.customRunnables.forEach {
                val particleOptions = ParticleArgument.getParticle(context, subCommand.name)
                val particle = CraftParticle.minecraftToBukkit(particleOptions.type)
                if (!it.run(context.source.bukkitSender, getParticleData(context, particle, particleOptions))) return false
            }
            else -> throw UnsupportedSubCommandException()
        }
        return true
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