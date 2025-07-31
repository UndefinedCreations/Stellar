package com.undefined.stellar;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class JavaTesting extends JavaPlugin {

    @Override
    public void onEnable() {
        StellarConfig.setPlugin(this);

        new StellarCommand("server")
                .addPhraseArgument("args")
                .addWordArgument(0, argument -> {
                    argument.addExecution(Player.class, context -> {
                        context.getArgument(0);
                        // ...
                    });
                })
                .addWordSuggestions(1, "first", "second");
    }

}
