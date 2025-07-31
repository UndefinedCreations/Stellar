package com.undefined.stellar;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class JavaTesting extends JavaPlugin {

    @Override
    public void onEnable() {
        StellarConfig.setPlugin(this);

        new StellarCommand("test")
                .then("test", argument -> {
                    argument.addExecution(Player.class, context -> {
                        context.getSender().sendMessage("test");
                    });
                })
                .register(this);
    }

}
