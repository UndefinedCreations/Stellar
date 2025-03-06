package com.undefined.stellar.v1_21_4

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import com.undefined.stellar.AbstractStellarArgument
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
import com.undefined.stellar.data.argument.EntityAnchor
import com.undefined.stellar.data.argument.Operation
import com.undefined.stellar.data.exception.UnsupportedArgumentException
import com.undefined.stellar.nms.NMS
import com.undefined.stellar.nms.NMSHelper
import io.papermc.paper.adventure.PaperAdventure
import io.papermc.paper.registry.PaperRegistries
import io.papermc.paper.registry.RegistryAccess
import io.papermc.paper.registry.RegistryKey
import io.papermc.paper.registry.entry.RegistryEntry
import net.kyori.adventure.key.Key
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSource
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.*
import net.minecraft.commands.arguments.blocks.BlockStateArgument
import net.minecraft.commands.arguments.coordinates.SwizzleArgument
import net.minecraft.commands.arguments.item.ItemArgument
import net.minecraft.commands.arguments.item.ItemPredicateArgument
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.animal.CatVariant
import net.minecraft.world.level.block.state.pattern.BlockInWorld
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import org.bukkit.Keyed
import org.bukkit.block.Block
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.block.data.CraftBlockData
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.craftbukkit.util.CraftNamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.function.Predicate
import net.minecraft.commands.arguments.AngleArgument as BrigadierAngleArgument
import net.minecraft.commands.arguments.EntityAnchorArgument as BrigadierEntityAnchorArgument
import net.minecraft.commands.arguments.EntityArgument as BrigadierEntityArgument
import net.minecraft.commands.arguments.GameModeArgument as BrigadierGameModeArgument
import net.minecraft.commands.arguments.GameProfileArgument as BrigadierGameProfileArgument
import net.minecraft.commands.arguments.OperationArgument as BrigadierOperationArgument
import net.minecraft.commands.arguments.TimeArgument as BrigadierTimeArgument
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument as BrigadierBlockPredicateArgument
import net.minecraft.commands.arguments.coordinates.RotationArgument as BrigadierRotationArgument

@Suppress("UNCHECKED_CAST")
object NMS1_21_4 : NMS {

    private val COMMAND_BUILD_CONTEXT: CommandBuildContext by lazy {
        CommandBuildContext.simple(
            MinecraftServer.getServer().registryAccess(),
            MinecraftServer.getServer().worldData.dataConfiguration.enabledFeatures()
        )
    }

    override fun getCommandDispatcher(): CommandDispatcher<Any> = MinecraftServer.getServer().functions.dispatcher as CommandDispatcher<Any>

    override fun getArgumentType(argument: AbstractStellarArgument<*, *>): ArgumentType<*> = when (argument) {
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
        is RegistryArgument -> {
//            val registry = ResourceKey::class.java.getDeclaredConstructor(ResourceLocation::class.java, ResourceLocation::class.java)
//                .apply { isAccessible = true }
//                .newInstance(Registries.ROOT_REGISTRY_NAME, ResourceLocation.withDefaultNamespace(RegistryArgument.registryNames[argument.registry]!!)) as ResourceKey<Registry<CatVariant>>
//            println(registry.toString())
            val byRegistryKey = PaperRegistries::class.java.getDeclaredField("BY_REGISTRY_KEY").apply { isAccessible = true }[null] as Map<RegistryKey<*>, RegistryEntry<*, *>>
            val registry = (byRegistryKey[argument.registry] ?: throw IllegalArgumentException("${argument.registry} doesn't have an mc registry ResourceKey")).mcKey() as ResourceKey<out Registry<Any>>
            println(registry.toString())

            ResourceArgument.resource(COMMAND_BUILD_CONTEXT, registry)
        }
        is UUIDArgument -> UuidArgument.uuid()

        // Player
        is GameModeArgument -> BrigadierGameModeArgument.gameMode()
        is GameProfileArgument -> BrigadierGameProfileArgument.gameProfile()
        else -> throw UnsupportedArgumentException(argument)
    }

    override fun parseArgument(ctx: CommandContext<Any>, argument: AbstractStellarArgument<*, *>): Any? {
        val context: CommandContext<CommandSourceStack> = ctx as CommandContext<CommandSourceStack>
        return when (argument) {
            // Block
            is BlockDataArgument -> CraftBlockData.fromData(BlockStateArgument.getBlock(context, argument.name).state)
            is BlockPredicateArgument -> { block: Block ->
                BrigadierBlockPredicateArgument.getBlockPredicate(context, argument.name).test(
                    BlockInWorld(context.source.level, BlockPos(block.x, block.y, block.z), true)
                )
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
            is AxisArgument -> SwizzleArgument.getSwizzle(context, argument.name)
            is DoubleRangeArgument -> RangeArgument.Floats.getRange(context, argument.name).let { it.min.orElse(1.0)..it.max.orElse(2.0) }
            is IntRangeArgument -> RangeArgument.Ints.getRange(context, argument.name).let { it.min.orElse(1)..it.max.orElse(2) }
            is OperationArgument -> Operation.getOperation(NMSHelper.getArgumentInput(context, argument.name) ?: return null)
            is RotationArgument -> BrigadierRotationArgument.getRotation(context, argument.name)
            is TimeArgument -> IntegerArgumentType.getInteger(context, argument.name)

            // Misc
            is NamespacedKeyArgument -> CraftNamespacedKey.fromMinecraft(ResourceLocationArgument.getId(context, argument.name))
            is RegistryArgument -> RegistryAccess.registryAccess().getRegistry(argument.registry as RegistryKey<Keyed>).getOrThrow(Key.key(NMSHelper.getArgumentInput(context, argument.name)!!))

            is UUIDArgument -> UuidArgument.getUuid(context, argument.name)

            // Player
            is GameModeArgument -> BrigadierGameModeArgument.getGameMode(context, argument.name)
            is GameProfileArgument -> BrigadierGameProfileArgument.getGameProfiles(context, argument.name)
            else -> null
        }
    }

    override fun getBukkitSender(source: Any): CommandSender = (source as CommandSourceStack).bukkitSender

    override fun hasPermission(player: Player, level: Int): Boolean = (player as CraftPlayer).handle.hasPermissions(level)

    override fun getCommandSourceStack(sender: CommandSender): Any {
        val a: ClosedFloatingPointRange<Double> = 0.1..1.0
        val overworld = MinecraftServer.getServer().overworld()
        return CommandSourceStack(
            Source(sender),
            Vec3.atLowerCornerOf(overworld.sharedSpawnPos),
            Vec2.ZERO,
            overworld,
            (sender as CraftPlayer).handle.permissionLevel,
            sender.name,
            Component.literal(sender.name),
            MinecraftServer.getServer(),
            null
        )
    }

    private data class Source(val sender: CommandSender) : CommandSource {
        override fun sendSystemMessage(message: Component) = this.sender.sendMessage(PaperAdventure.asAdventure(message))
        override fun acceptsSuccess(): Boolean = true
        override fun acceptsFailure(): Boolean = true
        override fun shouldInformAdmins(): Boolean = false
        override fun getBukkitSender(stack: CommandSourceStack): CommandSender = this.sender
    }

}