package com.undefined.stellar.v1_21_4

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.ParsedArgument
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.undefined.stellar.argument.entity.EntityDisplayType
import com.undefined.stellar.argument.primitive.StringType
import com.undefined.stellar.argument.world.LocationArgument
import com.undefined.stellar.argument.world.LocationType
import com.undefined.stellar.data.argument.ParticleData
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument
import net.minecraft.commands.arguments.coordinates.Vec2Argument
import net.minecraft.commands.arguments.coordinates.Vec3Argument
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.particles.*
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ColumnPos
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.BlockPositionSource
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import org.bukkit.*
import org.bukkit.block.data.BlockData
import org.bukkit.craftbukkit.block.data.CraftBlockData
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.scoreboard.DisplaySlot
import java.util.*

object ArgumentHelper {

    @Suppress("UNCHECKED_CAST")
    fun getArgumentInput(context: CommandContext<CommandSourceStack>, name: String): String? {
        val field = CommandContext::class.java.getDeclaredField("arguments")
        field.isAccessible = true
        val arguments: Map<String, ParsedArgument<CommandSourceStack, *>> = field.get(context) as Map<String, ParsedArgument<CommandSourceStack, *>>
        val argument = arguments[name] ?: return null
        val range = StringRange.between(argument.range.start, context.input.length)
        return range.get(context.input)
    }

    fun getInventoryType(menu: MenuType<*>): InventoryType = when (menu) {
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
    fun <T> getRegistryKey(
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

    fun <T> getRegistry(
        context: CommandContext<CommandSourceStack>,
        registryRef: ResourceKey<out Registry<T>>
    ): Registry<T> {
        return context.source.server.registryAccess().lookupOrThrow(registryRef)
    }

    @Throws(CommandSyntaxException::class)
    fun <T> resolveKey(
        context: CommandContext<CommandSourceStack>,
        name: String,
        registryRef: ResourceKey<Registry<T>>
    ): Holder.Reference<T> {
        val invalidException = DynamicCommandExceptionType { argument ->
            Component.translatableEscape("argument.resource_or_id.invalid", argument)
        }
        val resourceKey = getRegistryKey(context, name, registryRef, invalidException)
        return getRegistry(context, registryRef).get(resourceKey).orElseThrow { invalidException.create(resourceKey.location()) }
    }

    @Throws(CommandSyntaxException::class)
    fun <T> getId(
        context: CommandContext<CommandSourceStack>,
        name: String,
        registryRef: ResourceKey<Registry<T>>
    ): NamespacedKey {
        val key = resolveKey(context, name, registryRef).key().location()
        return NamespacedKey(key.namespace, key.path)
    }

    fun brigadier(type: StringType): StringArgumentType = when (type) {
        StringType.WORD -> StringArgumentType.word()
        StringType.QUOTABLE_PHRASE -> StringArgumentType.string()
        StringType.PHRASE -> StringArgumentType.greedyString()
    }

    fun brigadier(type: EntityDisplayType): EntityArgument = when (type) {
        EntityDisplayType.ENTITY -> EntityArgument.entity()
        EntityDisplayType.ENTITIES -> EntityArgument.entities()
        EntityDisplayType.PLAYER -> EntityArgument.player()
        EntityDisplayType.PLAYERS -> EntityArgument.players()
    }

    fun getBukkitAxis(argument: EnumSet<Direction.Axis>): EnumSet<Axis> =
        argument.mapTo(EnumSet.noneOf(Axis::class.java)) {
            when (it) {
                Direction.Axis.X -> Axis.X
                Direction.Axis.Y -> Axis.Y
                Direction.Axis.Z -> Axis.Z
                null -> Axis.X
            }
        }

    fun getBukkitDisplaySlot(slot: net.minecraft.world.scores.DisplaySlot): DisplaySlot = when (slot) {
        net.minecraft.world.scores.DisplaySlot.LIST -> DisplaySlot.PLAYER_LIST
        net.minecraft.world.scores.DisplaySlot.SIDEBAR -> DisplaySlot.SIDEBAR
        net.minecraft.world.scores.DisplaySlot.BELOW_NAME -> DisplaySlot.BELOW_NAME
        net.minecraft.world.scores.DisplaySlot.TEAM_BLACK -> DisplaySlot.SIDEBAR_TEAM_BLACK
        net.minecraft.world.scores.DisplaySlot.TEAM_DARK_BLUE -> DisplaySlot.SIDEBAR_TEAM_DARK_BLUE
        net.minecraft.world.scores.DisplaySlot.TEAM_DARK_GREEN -> DisplaySlot.SIDEBAR_TEAM_GREEN
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

    fun getParticleData(context: CommandContext<CommandSourceStack>, particle: Particle, options: ParticleOptions): ParticleData<*> = when (options) {
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
        is ItemParticleOption -> ParticleData<ItemStack>(
            particle,
            CraftItemStack.asBukkitCopy(options.item)
        )
        is VibrationParticleOption -> {
            val level: Level = context.source.level
            val destination: Vibration.Destination

            if (options.destination is BlockPositionSource) {
                val to: Vec3 = options.destination.getPosition(level).get()
                destination = Vibration.Destination.BlockDestination(Location(level.world, to.x(), to.y(), to.z()))
                ParticleData(particle, Vibration(destination, options.arrivalInTicks))
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

    fun getLocation(context: CommandContext<CommandSourceStack>, command: LocationArgument): Location =
        when (command.type) {
            LocationType.LOCATION_3D -> blockPosToLocation(BlockPosArgument.getBlockPos(context, command.name), context.source.level.world)
            LocationType.LOCATION_2D -> columnPosToLocation(ColumnPosArgument.getColumnPos(context, command.name), context.source.level.world)
            LocationType.PRECISE_LOCATION_3D -> vec3ToLocation(Vec3Argument.getVec3(context, command.name), context.source.level.world)
            LocationType.PRECISE_LOCATION_2D -> vec2ToLocation(Vec2Argument.getVec2(context, command.name), context.source.level.world)
        }

    fun blockPosToLocation(block: BlockPos, world: World) = Location(world, block.x.toDouble(), block.y.toDouble(), block.z.toDouble())
    fun columnPosToLocation(column: ColumnPos, world: World) = Location(world, column.x.toDouble(), 0.0, column.z.toDouble())
    fun vec3ToLocation(vec: Vec3, world: World) = Location(world, vec.x, vec.y, vec.z)
    fun vec2ToLocation(vec: Vec2, world: World) = Location(world, vec.x.toDouble(), 0.0, vec.y.toDouble())

}