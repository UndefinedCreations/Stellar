package com.undefined.stellar;

import com.undefined.stellar.argument.basic.StringType;
import com.undefined.stellar.argument.misc.RegistryArgument;
import com.undefined.stellar.util.CommandUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

public class JavaTest extends JavaPlugin {

    private final MiniMessage miniMessage = MiniMessage.builder().build();

    @Override
    public void onEnable() {
        new StellarCommand("message")
                .addComponentMessageCooldown(
                        5,
                        TimeUnit.SECONDS,
                        (context, remaining) -> Component.text("Please wait " + TimeUnit.MILLISECONDS.toSeconds(remaining) + " more seconds!", NamedTextColor.RED)
                )
                .addOnlinePlayersArgument("target")
                .addStringArgument("message", StringType.PHRASE)
                .addExecution(Player.class, context -> {
                    Player target = context.getArgument("target");
                    String message = context.getArgument("message");
                    target.sendMessage(context.getSender().getName() + " -> " + message + ".");
                })
                .register(this, "prefix");

        StellarConfig.INSTANCE.setPrefix("myserver");

        new StellarCommand("admin")
                /* command logic */
                .register(); // or can be specified here

        new StellarCommand("test")
                .addMessageCooldown(
                        5,
                        TimeUnit.SECONDS,
                        (context, remaining) -> "You cannot use this command right now! Please wait " + remaining + " more seconds."
                )
                .register();

        CommandUtil.unregisterCommand("enchant");
        StellarConfig.INSTANCE
            .setPlugin(this)
            .setPrefix("example")
            .setMiniMessage(this.miniMessage);

        new StellarCommand("enchant")
                .addArgument(new RegistryArgument("enchant", Registry.ENCHANTMENT))
                .addIntegerArgument("level", 0, 255)
                .addExecution(Player.class, context -> {
                    Enchantment enchantment = context.getArgument("enchant");
                    int level = context.getArgument("level");
                    context.getSender().getInventory().getItemInMainHand().addUnsafeEnchantment(enchantment, level);
                })
                .register(this, "stellar");

        new StellarCommand("fly")
                .addExecution(Player.class, context -> {
                    context.getSender().setFlying(!context.getSender().isFlying());
                    context.getSender().sendMessage(ChatColor.GREEN + "You are now " + (context.getSender().isFlying() ? "flying." : "not flying."));
                })
                .register();
    }
}
