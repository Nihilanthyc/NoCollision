package me.nihilanth.nocollision.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import me.nihilanth.nocollision.config.CollisionConfig;
import me.nihilanth.nocollision.config.CollisionRule;
import me.nihilanth.nocollision.config.MobConfig;
import me.nihilanth.nocollision.team.NoCollisionTeams;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.util.Identifier;

public class NoCollisionCommand {

    public static void register() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {

                    dispatcher.register(CommandManager.literal("nocollision")
                            .requires(source -> source.hasPermissionLevel(2))

                            .then(CommandManager.literal("add")
                                    .then(CommandManager.argument("mob", IdentifierArgumentType.identifier())
                                            .suggests((ctx, builder) ->
                                                    CommandSource.suggestIdentifiers(
                                                            Registries.ENTITY_TYPE.getIds(),
                                                            builder
                                                    )
                                            )
                                            .executes(ctx -> {
                                                return addMob(
                                                        ctx.getSource(),
                                                        IdentifierArgumentType.getIdentifier(ctx, "mob")
                                                );
                                            })
                                    )
                            )

                            .then(CommandManager.literal("playersCanCollide")
                                    .then(CommandManager.argument("value", BoolArgumentType.bool())
                                            .executes(ctx -> {

                                                CollisionConfig.playersCanCollide =
                                                        BoolArgumentType.getBool(ctx, "value");
                                                CollisionConfig.save();

                                                // 🔥 APLICA IMEDIATAMENTE
                                                NoCollisionTeams.update(ctx.getSource().getServer());

                                                ctx.getSource().sendFeedback(
                                                        () -> Text.literal(
                                                                "§aPlayers can collide: " +
                                                                        CollisionConfig.playersCanCollide),
                                                        true
                                                );
                                                return 1;
                                            })
                                    )
                            )

                            .then(CommandManager.literal("playersNoHostileCollision")
                                    .then(CommandManager.argument("rule", StringArgumentType.word())
                                            .suggests((c, b) -> CommandSource.suggestMatching(
                                                    new String[]{"ALL", "LISTONLY", "OFF"}, b))
                                            .executes(ctx -> {
                                                setRule(
                                                        ctx.getSource(),
                                                        true,
                                                        StringArgumentType.getString(ctx, "rule")
                                                );
                                                return 1;
                                            })
                                    )
                            )

                            .then(CommandManager.literal("playersNoPassiveCollision")
                                    .then(CommandManager.argument("rule", StringArgumentType.word())
                                            .suggests((c, b) -> CommandSource.suggestMatching(
                                                    new String[]{"ALL", "LISTONLY", "OFF"}, b))
                                            .executes(ctx -> {
                                                setRule(
                                                        ctx.getSource(),
                                                        false,
                                                        StringArgumentType.getString(ctx, "rule")
                                                );
                                                return 1;
                                            })
                                    )
                            )

                            .then(CommandManager.literal("remove")
                                    .then(CommandManager.argument("mob", IdentifierArgumentType.identifier())
                                            .suggests((ctx, builder) -> {
                                                return CommandSource.suggestMatching(
                                                        MobConfig.NO_COLLISION_MOBS.stream()
                                                                .map(type -> Registries.ENTITY_TYPE.getId(type))
                                                                .filter(id -> id != null)
                                                                .map(Identifier::toString),
                                                        builder
                                                );
                                            })
                                            .executes(ctx -> {
                                                return removeMob(
                                                        ctx.getSource(),
                                                        IdentifierArgumentType.getIdentifier(ctx, "mob")
                                                );
                                            })
                                    )
                            )


                            .then(CommandManager.literal("list")
                                    .executes(ctx -> {
                                        return listMobs(ctx.getSource());
                                    })
                            )
                    );
                }
        );
    }

    private static int addMob(ServerCommandSource source, Identifier id) {
        EntityType<?> type = Registries.ENTITY_TYPE.get(id);

        if (type == null) {
            source.sendError(Text.literal("Invalid entity: " + id));
            return 0;
        }

        if (MobConfig.NO_COLLISION_MOBS.contains(type)) {
            source.sendError(Text.literal("Entity already in the list."));
            return 0;
        }

        MobConfig.NO_COLLISION_MOBS.add(type);
        MobConfig.save();

        source.sendFeedback(
                () -> Text.literal("§aAdded: " + id),
                true
        );
        return 1;
    }


    private static int removeMob(ServerCommandSource source, Identifier id) {
        EntityType<?> type = Registries.ENTITY_TYPE.get(id);

        if (type == null || !MobConfig.NO_COLLISION_MOBS.contains(type)) {
            source.sendError(Text.literal("Entity is not in the list."));
            return 0;
        }

        MobConfig.NO_COLLISION_MOBS.remove(type);
        MobConfig.save();

        source.sendFeedback(
                () -> Text.literal("§cRemoved: " + id),
                true
        );
        return 1;
    }


    private static int listMobs(ServerCommandSource source) {
        if (MobConfig.NO_COLLISION_MOBS.isEmpty()) {
            source.sendFeedback(
                    () -> Text.literal("§eList is empty."),
                    false
            );
            return 1;
        }

        source.sendFeedback(
                () -> Text.literal("§6Entities without collision/cramming:"),
                false
        );

        MobConfig.NO_COLLISION_MOBS.forEach(type -> {
            Identifier id = Registries.ENTITY_TYPE.getId(type);
            source.sendFeedback(
                    () -> Text.literal(" - " + id),
                    false
            );
        });

        return 1;
    }

    private static void setRule(
            ServerCommandSource src,
            boolean hostile,
            String value
    ) {
        CollisionRule rule;
        try {
            rule = CollisionRule.valueOf(value.toUpperCase());
        } catch (Exception e) {
            src.sendError(Text.literal("Invalid value: " + value));
            return;
        }

        if (hostile) {
            CollisionConfig.playersNoHostileCollision = rule;
        } else {
            CollisionConfig.playersNoPassiveCollision = rule;
        }

        CollisionConfig.save();

        src.sendFeedback(
                () -> Text.literal("§aUpdated rule: " + rule),
                true
        );
    }
}
