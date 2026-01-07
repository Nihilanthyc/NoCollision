package me.nihilanth.nocollision;

import me.nihilanth.nocollision.config.CollisionConfig;
import me.nihilanth.nocollision.team.NoCollisionTeams;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import me.nihilanth.nocollision.command.NoCollisionCommand;
import me.nihilanth.nocollision.config.MobConfig;

public class Nocollision implements ModInitializer {

    public static final String MOD_ID = "nocollision";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("NoCollision mod Enabled");
        MobConfig.load();
        CollisionConfig.load();
        NoCollisionCommand.register();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            NoCollisionTeams.update(server);
        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(
                (player, origin, destination) -> {
                    NoCollisionTeams.update(player.getEntityWorld().getServer());
                }
        );
    }
}
