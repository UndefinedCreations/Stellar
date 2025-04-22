package com.undefined.stellar;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class TestMain extends JavaPlugin {
    @Override
    public void onEnable() {
        new StellarCommand("enum")
            .addEnumArgument("type", EntityType.class)
            .addExecution(context -> {
                EntityType type = context.getArgument("type");
                context.getSender().sendMessage(type.name());
            });

        List<String> list = new ArrayList<>();
        list.add("minecraft:a");
        list.add("minecraft:b");
        list.add("minecraft:c");
        new StellarCommand("list")
            .addListArgument("name", list, (sender, string) -> string.replaceFirst("[a-zA-Z]:", ""))
            .addExecution(Player.class, context -> {
                String name = context.getArgument("name");
                context.getSender().sendMessage(name);
            });
//        CommandUtilKt.unregisterCommand("test");
//        new StellarCommand("test")
//                .register(this);
    }
}
