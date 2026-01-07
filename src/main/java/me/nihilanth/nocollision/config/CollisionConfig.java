package me.nihilanth.nocollision.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class CollisionConfig {

    private static final Gson GSON =
            new GsonBuilder().setPrettyPrinting().create();

    private static final File FILE =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("nocollision-rules.json")
                    .toFile();

    public static boolean playersCanCollide = true;
    public static CollisionRule playersNoHostileCollision = CollisionRule.OFF;
    public static CollisionRule playersNoPassiveCollision = CollisionRule.OFF;

    public static void load() {
        if (!FILE.exists()) {
            save();
            return;
        }

        try (FileReader reader = new FileReader(FILE)) {
            Data data = GSON.fromJson(reader, Data.class);

            playersCanCollide = data.playersCanCollide;
            playersNoHostileCollision = data.playersNoHostileCollision;
            playersNoPassiveCollision = data.playersNoPassiveCollision;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(FILE)) {
            Data data = new Data();

            data.playersCanCollide = playersCanCollide;
            data.playersNoHostileCollision = playersNoHostileCollision;
            data.playersNoPassiveCollision = playersNoPassiveCollision;

            GSON.toJson(data, writer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Data {
        boolean playersCanCollide;
        CollisionRule playersNoHostileCollision;
        CollisionRule playersNoPassiveCollision;
    }
}
