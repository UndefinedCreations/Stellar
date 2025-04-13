package com.undefined.stellar;

import com.undefined.stellar.argument.world.ParticleArgument;
import com.undefined.stellar.data.argument.ParticleData;
import org.bukkit.plugin.java.JavaPlugin;

public class JavaMain extends JavaPlugin {

    @Override
    public void onEnable() {
        new StellarCommand("spawn-particle")
                .addArgument(new ParticleArgument("particle"))
                .addExecution(context -> {
                    ParticleData<Object> data = (ParticleData<Object>) context.get("particle");
                    context.getSender().sendMessage(data.getParticle().name());
    //                sender.spawnParticle(
    //                    data.particle,
    //                    sender.eyeLocation,
    //                    10,
    //                    data.options
    //                )
                })
                .register(this);
    }

}
