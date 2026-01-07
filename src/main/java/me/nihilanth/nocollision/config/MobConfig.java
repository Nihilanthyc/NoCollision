package me.nihilanth.nocollision.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class MobConfig {

    public static final Set<EntityType<?>> NO_COLLISION_MOBS = new HashSet<>();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH =
            FabricLoader.getInstance().getConfigDir().resolve("nocollision-mobs.json");

    public static void load() {
        NO_COLLISION_MOBS.clear();

        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }

        try {
            JsonObject json = GSON.fromJson(
                    Files.readString(CONFIG_PATH),
                    JsonObject.class
            );

            json.getAsJsonArray("mobs").forEach(element -> {
                Identifier id = Identifier.of(element.getAsString());
                EntityType<?> type = Registries.ENTITY_TYPE.get(id);
                if (type != null) {
                    NO_COLLISION_MOBS.add(type);
                }
            });

        } catch (Exception e) {
            System.err.println("[NoCollision] Error on loading config file:");
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            JsonObject json = new JsonObject();
            var array = new com.google.gson.JsonArray();

            NO_COLLISION_MOBS.forEach(type -> {
                Identifier id = Registries.ENTITY_TYPE.getId(type);
                if (id != null) {
                    array.add(id.toString());
                }
            });

            json.add("mobs", array);

            Files.writeString(CONFIG_PATH, GSON.toJson(json));

        } catch (IOException e) {
            System.err.println("[NoCollision] Error on saving config file:");
            e.printStackTrace();
        }
    }

    public static boolean isNoCollision(EntityType<?> type) {
        return NO_COLLISION_MOBS.contains(type);
    }

}
