package com.undefined.stellar.internal

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
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
import com.undefined.stellar.argument.player.GameProfileArgument
import com.undefined.stellar.argument.scoreboard.*
import com.undefined.stellar.argument.text.ColorArgument
import com.undefined.stellar.argument.text.ComponentArgument
import com.undefined.stellar.argument.text.MessageArgument
import com.undefined.stellar.argument.world.EnvironmentArgument
import com.undefined.stellar.argument.world.LocationArgument
import com.undefined.stellar.argument.world.LocationType
import com.undefined.stellar.argument.world.ParticleArgument
import com.undefined.stellar.data.argument.EntityAnchor
import com.undefined.stellar.data.argument.Operation
import com.undefined.stellar.data.argument.ParticleData
import com.undefined.stellar.data.exception.UnsupportedArgumentException
import com.undefined.stellar.nms.NMS
import com.undefined.stellar.nms.NMSHelper
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minecraft.server.v1_13_R2.*
import org.bukkit.*
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_13_R2.CraftParticle
import org.bukkit.craftbukkit.v1_13_R2.block.data.CraftBlockData
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_13_R2.util.CraftNamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.DisplaySlot
import java.util.*
import java.util.function.Predicate

@Suppress("UNCHECKED_CAST", "DEPRECATION")
object NMS1_13_2 : NMS {

    override fun getCommandDispatcher(): CommandDispatcher<Any> = MinecraftServer.getServer().functionData.d() as CommandDispatcher<Any>

    override fun getArgumentType(argument: ParameterArgument<*, *>, plugin: JavaPlugin): ArgumentType<*> = when (argument) {
        // Basic
        is StringArgument -> ArgumentScoreholder.a()

        // Block
        is BlockDataArgument -> ArgumentTile.a()
        is BlockPredicateArgument -> ArgumentBlockPredicate.a()

        // Entity
        is EntityAnchorArgument -> ArgumentAnchor.a()
        is EntityArgument -> when (argument.type) {
            EntityDisplayType.ENTITY -> ArgumentEntity::class.java.getDeclaredMethod("a")(null) as ArgumentEntity
            EntityDisplayType.ENTITIES -> ArgumentEntity.b()
            EntityDisplayType.PLAYER -> ArgumentEntity.c()
            EntityDisplayType.PLAYERS -> ArgumentEntity.d()
        }

        // Item
        is ItemStackArgument -> ArgumentItemStack.a()
        is ItemStackPredicateArgument -> ArgumentItemPredicate.a()
        is ItemSlotArgument -> ArgumentInventorySlot.a()

        // Math
        is AxisArgument -> ArgumentRotationAxis.a()
        is DoubleRangeArgument -> ArgumentCriterionValue::class.java.getDeclaredMethod("a")(null) as ArgumentCriterionValue<*>
        is IntRangeArgument -> ArgumentCriterionValue::class.java.getDeclaredMethod("b")(null) as ArgumentCriterionValue<*>
        is OperationArgument -> ArgumentMathOperation.a()
        is RotationArgument -> ArgumentRotation.a()

        // Misc
        is NamespacedKeyArgument -> ArgumentMinecraftKeyRegistered.a()

        // Player
        is GameProfileArgument -> ArgumentProfile.a()

        // Scoreboard
        is DisplaySlotArgument -> ArgumentScoreboardSlot.a()
        is ObjectiveArgument -> ArgumentScoreboardObjective.a()
        is ObjectiveCriteriaArgument -> ArgumentScoreboardCriteria.a()
        is ScoreHolderArgument -> when (argument.type) {
            ScoreHolderType.SINGLE -> ArgumentScoreholder.a()
            ScoreHolderType.MULTIPLE -> ArgumentScoreholder.b()
        }
        is TeamArgument -> ArgumentScoreboardTeam.a()

        // Text
        is ColorArgument -> ArgumentChatFormat.a()
        is ComponentArgument -> ArgumentChatComponent.a()
        is MessageArgument -> ArgumentChat.a()

        // World
        is EnvironmentArgument -> ArgumentDimension.a()
        is LocationArgument -> when (argument.type) {
            LocationType.LOCATION_3D -> ArgumentPosition.a()
            LocationType.LOCATION_2D -> ArgumentVec2I.a()
            LocationType.PRECISE_LOCATION_3D -> ArgumentVec3.a()
            LocationType.PRECISE_LOCATION_2D -> ArgumentVec2.a()
        }
        is ParticleArgument -> ArgumentParticle.a()
        else -> throw UnsupportedArgumentException(argument)
    }

    override fun parseArgument(ctx: CommandContext<Any>, argument: ParameterArgument<*, *>): Any? {
        val context: CommandContext<CommandListenerWrapper> = ctx as CommandContext<CommandListenerWrapper>
        return when (argument) {
            // Basic
            is StringArgument -> NMSHelper.getArgumentInput(context, argument.name)

            // Block
            is BlockDataArgument -> CraftBlockData.fromData(ArgumentTile.a(context, argument.name).a())
            is BlockPredicateArgument -> Predicate<Block> { block: Block ->
                ArgumentBlockPredicate.a(context, argument.name).test(ShapeDetectorBlock(context.source.world, BlockPosition(block.x, block.y, block.z), true))
            }

            // Entity
            is EntityAnchorArgument -> EntityAnchor.getFromName(NMSHelper.getArgumentInput(context, argument.name) ?: return null)
            is EntityArgument -> ArgumentEntity.b(context, argument.name).map { it.bukkitEntity }.takeIf { it.isNotEmpty() }
                ?: ArgumentEntity.a(context, argument.name).bukkitEntity

            // Item
            is ItemStackArgument -> CraftItemStack.asBukkitCopy(ArgumentItemStack.a(context, argument.name).a(1, false))
            is ItemStackPredicateArgument -> Predicate<ItemStack> { ArgumentItemPredicate.a(context, argument.name).test(CraftItemStack.asNMSCopy(it)) }
            is ItemSlotArgument -> ArgumentInventorySlot.a(context, argument.name)

            // Math
            is AxisArgument -> ArgumentRotationAxis.a(context, argument.name).mapTo(EnumSet.noneOf(Axis::class.java)) { Axis.valueOf(it.name) }
            is DoubleRangeArgument -> context.getArgument(argument.name, CriterionConditionValue.FloatRange::class.java).let { (it.a() ?: 1.0F)..(it.b() ?: 2.0F) }
            is IntRangeArgument -> ArgumentCriterionValue.b.a(context, argument.name).let { (it.a() ?: 1)..(it.b() ?: 2) }
            is OperationArgument -> Operation.getOperation(NMSHelper.getArgumentInput(context, argument.name) ?: return null)
            is RotationArgument -> ArgumentRotation.a(context, argument.name).a(context.source).let {
                Location(context.source.world.world, 0.0, 0.0, 0.0, it.x.toFloat(), it.y.toFloat())
            }

            // Misc
            is NamespacedKeyArgument -> CraftNamespacedKey.fromMinecraft(ArgumentMinecraftKeyRegistered.c(context, argument.name))

            // Player
            is GameProfileArgument -> ArgumentProfile.a(context, argument.name)

            // Scoreboard
            is DisplaySlotArgument -> DisplaySlot.valueOf(Scoreboard.getSlotName(ArgumentScoreboardSlot.a(context, argument.name)))
            is ObjectiveArgument -> Bukkit.getScoreboardManager()!!.mainScoreboard.getObjective(ArgumentScoreboardObjective.a(context, argument.name).name)
            is ObjectiveCriteriaArgument -> ArgumentScoreboardCriteria.a(context, argument.name).name
            is ScoreHolderArgument -> when (argument.type) {
                ScoreHolderType.SINGLE -> ArgumentScoreholder.a(context, argument.name)
                ScoreHolderType.MULTIPLE -> ArgumentScoreholder.b(context, argument.name)
            }
            is TeamArgument -> Bukkit.getScoreboardManager()!!.mainScoreboard.getTeam(ArgumentScoreboardTeam.a(context, argument.name).name)

            // Text
            is ColorArgument -> ChatColor.getByChar(ArgumentChatFormat.a(context, argument.name).character)
            is ComponentArgument -> GsonComponentSerializer.gson().deserialize(IChatBaseComponent.ChatSerializer.a(ArgumentChatComponent.a(context, argument.name)))
            is MessageArgument -> GsonComponentSerializer.gson().deserialize(IChatBaseComponent.ChatSerializer.a(ArgumentChat.a(context, argument.name)))

            // World
            is EnvironmentArgument -> World.Environment.getEnvironment(ArgumentDimension.a(context, argument.name).dimensionID)
            is LocationArgument -> when (argument.type) {
                LocationType.LOCATION_3D -> blockPosToLocation(context.getArgument(argument.name, IVectorPosition::class.java).c(context.source), context.source.world.world)
                LocationType.LOCATION_2D -> columnPosToLocation(ArgumentVec2I.a(context, argument.name), context.source.world.world)
                LocationType.PRECISE_LOCATION_3D -> vec3ToLocation(ArgumentVec3.a(context, argument.name), context.source.world.world)
                LocationType.PRECISE_LOCATION_2D -> vec2ToLocation(ArgumentVec2.a(context, argument.name), context.source.world.world)
            }
            is ParticleArgument -> ArgumentParticle.a(context, argument.name).also { getParticleData(CraftParticle.toBukkit(it.b()), it) }
            else -> null
        }
    }

    override fun getCommandSourceStack(sender: CommandSender): Any {
        val overworld = MinecraftServer.getServer().getWorldServer(DimensionManager.OVERWORLD)
        val serverPlayer: EntityPlayer? = (sender as? CraftPlayer)?.handle
        val permissionLevel = serverPlayer?.profile?.let { serverPlayer.server.a(it) } ?: 4
        return CommandListenerWrapper(
            Source(sender),
            Vec3D(overworld.spawn),
            Vec2F.a,
            overworld,
            permissionLevel,
            sender.name,
            ChatComponentText(sender.name),
            MinecraftServer.getServer(),
            null
        )
    }

    private data class Source(val sender: CommandSender) : ICommandListener {
        override fun sendMessage(message: IChatBaseComponent) =
            this.sender.sendMessage(LegacyComponentSerializer.legacySection().serialize(asAdventure(message)))
        override fun a(): Boolean = true
        override fun b(): Boolean = true
        override fun B_(): Boolean = false
        override fun getBukkitSender(stack: CommandListenerWrapper): CommandSender = this.sender
    }

    fun asAdventure(component: IChatBaseComponent): net.kyori.adventure.text.Component =
        GsonComponentSerializer.gson().deserialize(IChatBaseComponent.ChatSerializer.a(component))

    private fun blockPosToLocation(block: BlockPosition, world: World) = Location(world, block.x.toDouble(), block.y.toDouble(), block.z.toDouble())
    private fun columnPosToLocation(column: ArgumentVec2I.a, world: World) = Location(world, column.a.toDouble(), 0.0, column.b.toDouble())
    private fun vec3ToLocation(vec: Vec3D, world: World) = Location(world, vec.x, vec.y, vec.z)
    private fun vec2ToLocation(vec: Vec2F, world: World) = Location(world, vec.i.toDouble(), 0.0, vec.j.toDouble())

    private fun getParticleData(particle: Particle, options: ParticleParam): ParticleData<*> = when (options) {
        is ParticleType -> ParticleData(particle, null)
        is ParticleParamBlock -> ParticleData<BlockData>(particle, CraftBlockData.fromData(options::class.java.getDeclaredMethod("c").also { it.isAccessible = true }(null) as IBlockData))
        is ParticleParamRedstone -> {
            val colors = options.a().split(" ")
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
        is ParticleParamItem -> ParticleData(
            particle,
            CraftItemStack.asBukkitCopy(options::class.java.getDeclaredMethod("c").also { it.isAccessible = true }(null) as net.minecraft.server.v1_13_R2.ItemStack)
        )
        else -> ParticleData(particle, null)
    }

}