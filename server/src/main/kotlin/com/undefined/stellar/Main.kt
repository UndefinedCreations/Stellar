package com.undefined.stellar

import com.undefined.stellar.argument.basic.StringType
import com.undefined.stellar.argument.misc.RegistryArgument
import com.undefined.stellar.argument.scoreboard.ScoreHolderType
import com.undefined.stellar.data.argument.CommandContext
import com.undefined.stellar.util.unregisterCommand
import org.bukkit.ChatColor
import org.bukkit.Registry
import org.bukkit.block.structure.StructureRotation
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.loot.LootTable
import org.bukkit.loot.LootTables
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scoreboard.Team
import java.util.concurrent.TimeUnit

class Main : JavaPlugin() {

    override fun onEnable() {
        StellarCommand("message")
            .addCooldown(5, TimeUnit.SECONDS) { remaining ->
                sender.sendMessage("${ChatColor.RED}Please wait ${TimeUnit.MILLISECONDS.toSeconds(remaining)} more seconds!") // this is also the default message
            }
            .addOnlinePlayersArgument("target")
            .addStringArgument("message", StringType.PHRASE)
            .addExecution<Player> {
                val target: Player by args
                val message: String by args
                target.sendMessage("${sender.name} -> $message.")
            }
            .register(this, "prefix") // or can be specified here

        // literal
//        StellarCommand("server")
//             .addArgument(name = "reset")
//            .register(this)

//        // string
//        StellarCommand("server")
//            .addStringArgument("string", StringType.ALPHANUMERIC_WORD)
//            .register(this)
//        Bukkit.getScheduler().runTask(this, Runnable {
//            val dispatcher = NMSManager.nms.getCommandDispatcher()
//            dispatcher.root.children.remove(dispatcher.root.getChild("data"))
//        })

        unregisterCommand("enchant")

        val unit = TimeUnit.MILLISECONDS
        unit.convert(100, TimeUnit.SECONDS)

        StellarCommand("enchant")
            .addArgument(RegistryArgument("enchant", Registry.ENCHANTMENT))
            .addIntegerArgument("level", 0, 255)
            .addExecution(Player::class.java) { context: CommandContext<Player> ->
                val enchantment = context.getArgument<Enchantment>("enchant")
                val level = context.getArgument<Int>("level")
                context.sender.inventory.itemInMainHand.addUnsafeEnchantment(enchantment, level)
            }
            .register(this, "stellar")

        StellarCommand("fly")
            .addExecution(Player::class.java) { context: CommandContext<Player> ->
                context.sender.isFlying = !context.sender.isFlying
                context.sender.sendMessage(ChatColor.GREEN.toString() + "You are now " + (if (context.sender.isFlying) "flying." else "not flying."))
            }
            .register(this, "stellar")

        StellarCommand("get-uuid")
            .addOnlinePlayersArgument("target")
            .addExecution<Player> {
                val target: Player by args
                sender.sendMessage(target.uniqueId.toString())
            }
            .register(this)

        StellarCommand("enum")
            .addEnumArgument<EntityType>("type")
            .addExecution<Player> {
                sender.sendMessage(getArgument<EntityType>("type").name)
            }
            .register(this)

        unregisterCommand("list")

        val list = listOf("minecraft:a", "minecraft:b", "minecraft:c")
        StellarCommand("list")
            .addListArgument("name", list, tooltip = { it.replaceFirst("[a-zA-Z]+:".toRegex(), "") })
            .addExecution<Player> {
                val name: String by args
                sender.sendMessage(name)
            }
            .register(this)

        StellarCommand("server")
            .addMessageCooldown(5, TimeUnit.SECONDS) { remaining ->
                "<red>Please wait ${TimeUnit.MILLISECONDS.toSeconds(remaining)} more seconds!"
            }
            .addStringArgument("string", StringType.WORD)
            .addExecution<Player> {
                val string: String by args
                sender.sendMessage(string)
            }
            .register(this, "test")
//        StellarCommand("server")
//            .addStringArgument("string", StringType.QUOTABLE_PHRASE)
//            .register(this)
//        StellarCommand("server")
//            .addStringArgument("string", StringType.PHRASE)
//            .register(this)
//
//        // number
//        StellarCommand("server")
//            .addDoubleArgument("double")
//            .addExecution<Player> {
//                sender.sendMessage(getArgument<Double>("double").toString()) // added .toString()
//            }
//            .register(this)
//
//        // boolean
//        StellarCommand("server")
//            .addBooleanArgument("boolean") // renamed from bool to boolean
//            .addExecution<Player> {
//                sender.sendMessage(getArgument<Boolean>("boolean").toString()) // added .toString() & renamed from bool to boolean
//            }
//            .register(this)

//        // entity selector
//        StellarCommand("select")
//            .addEntityArgument(
//                name = "target",
//                type = EntityDisplayType.ENTITY
//            )
//            .register(this)
//
//        // entity anchor
//        StellarCommand("calculate-position") // renamed calculate-position
//            .addEntityAnchorArgument(name = "anchor") // renamed `addEntityAnchor` to `addEntityAnchorArgument`
//            .addExecution<Player> {
//                val anchor = getArgument<EntityAnchor>("anchor") // renamed `Anchor` to `EntityAnchor`
//                sender.sendMessage(
//                    anchor.apply(sender).toString() // returns location from eyes // added toString()
//                    // or anchor.apply(location, sender)
//                )
//            }
//            .register(this)

//        // game mode
//        StellarCommand("gm") // renamed to gm
//            .addGameModeArgument("mode") // capitalize the second m from `Gamemode`
//            .addExecution<Player> {
//                val mode = getArgument<GameMode>("mode")
//                sender.gameMode = mode // capitalize the second m from `gamemode`
//            }
//            .register(this)

//        // location
//        StellarCommand("teleport-to")
//            .addLocationArgument(
//                name = "location",
//                type = LocationType.LOCATION_3D // default
//            )
//            .addExecution<Player> {
//                val location = getArgument<Location>("location")
//                sender.teleport(location)
//            }
//            .register(this)

//        // particle
//        StellarCommand("spawn-particle")
//            .addParticleArgument(name = "particle")
//            .addExecution<Player> {
//                val data = getArgument<ParticleData<*>>("particle")
//                sender.spawnParticle(
//                    data.particle,
//                    sender.eyeLocation,
//                    10,
//                    data.options
//                )
//            }
//            .register(this)

//        // dimension, renamed to `environment`
//        StellarCommand("isNether")
//            .addEnvironmentArgument(name = "environment")
//            .addExecution<Player> {
//                val environment = getArgument<Environment>("environment")
//                val isNether = environment == Environment.NETHER
//                sender.sendMessage(isNether.toString())
//            }
//            .register(this)
//
//        // height map
//        StellarCommand("top")
//            .addHeightMapArgument(name = "heightmap")
//            .addExecution<Player> {
//                val environment = getArgument<HeightMap>("heightmap")
//                val highestYBlock = sender.world.getHighestBlockAt(sender.location, environment)
//                sender.teleport(highestYBlock.location.add(0.0, 1.0, 0.0))
//            }
//            .register(this)
//
//        // item argument
//        unregisterCommand("give") // added this line
//        StellarCommand("give")
//            .addItemStackArgument(name = "item") // renamed `addItemArgument` to `addItemStackArgument`
//            .addExecution<Player> {
//                val item = getArgument<ItemStack>("item")
//                sender.inventory.addItem(item)
//            }
//            .register(this)
//
//        // item predicate
//        StellarCommand("isDiamond") // renamed to `isDiamond`
//            .addItemPredicateArgument(name = "item") // rename `addItemArgument` to `addItemPredicateArgument`
//            .addExecution<Player> {
//                val predicate = getArgument<Predicate<ItemStack>>("item") // added a >
//                val isItem = predicate.test(ItemStack(Material.DIAMOND)) // replaced material variable into `Material.DIAMOND`
//                sender.sendMessage(isItem.toString()) // added toString()
//            }
//            .register(this)
//
//        // item slots
//        StellarCommand("clearItem") // capitalized I
//            .addItemSlotArgument(name = "slot")
//            .addExecution<Player> {
//                val slot = getArgument<Int>("slot")
//                sender.inventory.setItem(slot, null)
//            }
//            .register(this)
//
//        // block data
//        StellarCommand("spawn")
//            .addBlockDataArgument(name = "block")
//            .addExecution<Player> {
//                val data = getArgument<BlockData>("block")
//                sender.world.setBlockData(
//                    sender.location.subtract(0.0, 1.0, 0.0), // from -1 to 1
//                    data
//                )
//            }
//            .register(this)
//
//        // block predicate
//        StellarCommand("isBlock") // capitalize `B`
//            .addBlockPredicateArgument(name = "predicate")
//            .addExecution<Player> {
//                val block = sender.world.getBlockAt(1, 0, 1) // testing
//
//                val data = getArgument<Predicate<Block>>("predicate")
//                val isBlock = data.test(block)
//                sender.sendMessage(isBlock.toString())
//            }
//            .register(this)
//
//        // uuid
//        var currentUUID = UUID.randomUUID()
//        StellarCommand("uuid")
//            .addUUIDArgument(name = "uuid")
//            .addExecution<Player> {
//                val uuid = getArgument<UUID>("uuid")
//                currentUUID = uuid // separate variable
//            }
//            .register(this)
//
//        // namespaced key
//        var currentKey = NamespacedKey.minecraft("test")
//        StellarCommand("key")
//            .addNamespacedKeyArgument(name = "key")
//            .addExecution<Player> {
//                val key = getArgument<NamespacedKey>("key")
//                currentKey = key // separate variable
//            }
//            .register(this)
//
//        // angle
//        StellarCommand("setAngle") // capitalize A
//            .addAngleArgument(name = "angle")
//            .addExecution<Player> {
//                val angle = getArgument<Float>("angle")
//                sender.teleport(
//                    sender.location.apply { yaw = angle }
//                )
//            }
//            .register(this)
//
        // axis
//        StellarCommand("test")
//            .addAxisArgument(name = "axis")
//            .addExecution<Player> {
//                val axes = getArgument<EnumSet<Axis>>("axis")
//                sender.sendMessage(axes.joinToString(", ") { it.name })
//            }
//            .register(this)
//
        // operation
//        StellarCommand("calculate")
//            .addOperationArgument(name = "operation")
//            .addFloatArgument("num_one")
//            .addFloatArgument("num_two")
//            .addExecution<Player> {
//                val operation = getArgument<Operation>("operation")
//                val numOne = getArgument<Float>("num_one")
//                val numTwo = getArgument<Float>("num_two")
//                val result = operation.apply(numOne, numTwo)
//                sender.sendMessage("The final result is: $result")
//            }
//            .register(this)
//
        // int range
//        StellarCommand("roll")
//            .addIntRangeArgument("range") // or addDoubleRangeArgument // rename `addRangeArgument` to `addIntRangeArgument`
//            .addExecution<Player> {
//                val range = getArgument<IntRange>("range")
//                sender.sendMessage("The random roll was: ${range.random()}")
//            }
//            .register(this)
//
        // rotation
//        StellarCommand("rotation") // rename to set-rotation to rotation
//            .addRotationArgument("rotation")
//            .addExecution<Player> {
//                val rotation = getArgument<Location>("rotation")
//                sender.teleport(sender.location.apply {
//                    yaw = rotation.yaw
//                    pitch = rotation.pitch
//                }) // indent
//            }
//            .register(this)
//
        // time
//        unregisterCommand("time") // add this
//        StellarCommand("time") // rename
//            .addTimeArgument("time")
//            .addExecution<Player> {
//                val time = getArgument<Int>("time")
//                sender.world.time = time.toLong()
//            }
//            .register(this)
//
        // color
//        StellarCommand("choose")
//            .addColorArgument(name = "color")
//            .addExecution<Player> {
//                val color = getArgument<ChatColor>("color") // convert to ChatColor
//                sender.sendMessage("Color Chosen: ${color}Test") // redo this line
//            }
//            .register(this)
//
        // component
//        val audience = BukkitAudiences.create(this)
//        StellarCommand("talk-to-self") // rename send to talk
//            .addComponentArgument(name = "component")
//            .addExecution<Player> {
//                val component = getArgument<Component>("component")
//                audience.sender(sender).sendMessage(component)
//            }
//            .register(this)
//
        // message
        // val audience = BukkitAudiences.create(this) // commented for testing, needs to be added in docs again.
//        StellarCommand("talk-to-self") // rename to talk-to-self
//            .addMessageArgument(name = "message")
//            .addExecution<Player> {
//                val component = getArgument<Component>("message")
//                audience.sender(sender).sendMessage(component)
//            }
//            .register(this)
//
        // style
//        val audience = BukkitAudiences.create(this) // commented for testing, needs to be added in docs again.
//        StellarCommand("reminder")
//            .addStyleArgument(name = "style")
//            .addExecution<Player> {
//                val style = getArgument<Style>("style")
//                val message = Component.text("This is a reminder").style(style)
//                audience.sender(sender).sendMessage(message)
//            }
//            .register(this)
//
        // display slot
//        val scoreboardManager = Bukkit.getScoreboardManager()!!.mainScoreboard
//        val scoreboardObjective = if (scoreboardManager.getObjective("health") == null) scoreboardManager.registerNewObjective(
//            "health",
//            Criteria.HEALTH,
//            "health"
//        ) else scoreboardManager.getObjective("health")!!
//        StellarCommand("displayslot") // change S to s
//            .addDisplaySlotArgument(name = "slot")
//            .addExecution<Player> {
//                val slot = getArgument<DisplaySlot>("slot")
//                scoreboardObjective.displaySlot = slot
//            }
//            .register(this)
//
        // objective
//        StellarCommand("setamount")
//            .addObjectiveArgument(name = "objective")
//            .addIntegerArgument(name = "amount")
//            .addExecution<Player> {
//                val objective = getArgument<Objective>("objective")
//                val amount = getArgument<Int>("amount")
//                objective.getScore("amount").score = amount
//            }
//            .register(this)
//
        // objective criteria
//        StellarCommand("objective")
//            .addArgument("create")
//            .addStringArgument(name = "objective")
//            .addObjectiveCriteriaArgument(name = "criterion")
//            .addExecution<Player> {
//                val name = getArgument<String>("objective")
//                val criteria = getArgument<Criteria>("criterion")
//                Bukkit.getScoreboardManager()!!.mainScoreboard.registerNewObjective(name, criteria, name)
//            }
//            .register(this)
//
        // score holders
        StellarCommand("holder") // rename objective to holder
            .addScoreHolderArgument(name = "holder", type = ScoreHolderType.SINGLE) // added type param
            .addExecution<Player> {
                val holder = getArgument<String>("holder")
//                val item = ItemStack(Material.DIAMOND) // delete
                sender.sendMessage("Holder Name: $holder") // change the entire line
            }
            .register(this)

        StellarCommand("holders") // rename objective to holders
            .addScoreHolderArgument(name = "holders", type = ScoreHolderType.MULTIPLE) // added type param
            .addExecution<Player> {
                val holders = getArgument<List<String>>("holders") // change the entire line
//                val item = ItemStack(Material.DIAMOND) // delete
                sender.sendMessage("Holder Names: $holders") // change the entire line
            }
            .register(this)

        // team
        StellarCommand("setcolor")
            .addTeamArgument(name = "team")
            .addColorArgument(name = "color")
            .addExecution<Player> {
                val team = getArgument<Team>("team")
                val color = getArgument<ChatColor>("color")
                team.color = color
            }
            .register(this)

        // loot table
        var selectedLootTable = LootTables.ABANDONED_MINESHAFT.lootTable
        StellarCommand("loot-table")
            .addArgument("get")
            .addLootTableArgument(name = "table")
            .addExecution<Player> {
                val table = getArgument<LootTable>("table")
                selectedLootTable = table
            }
            .register(this)

        // mirror
//        fun spawnStructure(mirror: Mirror) {} // testing
//        StellarCommand("structure")
//            .addArgument("spawn")
//            .addMirrorArgument(name = "mirror")
//            .addExecution<Player> {
//                val mirror = getArgument<Mirror>("mirror")
//                spawnStructure(mirror) // custom method
//            }
//            .register(this)

        // structure rotation
        fun spawnStructure(mirror: StructureRotation) {} // testing
        StellarCommand("structure")
            .addArgument("spawn")
            .addStructureRotationArgument(name = "rotation")
            .addExecution<Player> {
                val rotation = getArgument<StructureRotation>("rotation")
                spawnStructure(rotation) // custom method
            }
            .register(this)
    }

}