package me.nihilanth.nocollision.logic;

import me.nihilanth.nocollision.config.CollisionConfig;
import me.nihilanth.nocollision.config.CollisionRule;
import me.nihilanth.nocollision.config.MobConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;

public class CollisionRules {

    public static boolean shouldCancelCollision(Entity a, Entity b) {

        if (a instanceof LivingEntity la && b instanceof LivingEntity lb) {

            if (MobConfig.isNoCollision(la.getType())
                    && MobConfig.isNoCollision(lb.getType())) {
                return true;
            }
        }

        if (a instanceof PlayerEntity && b instanceof PlayerEntity) {
            return !CollisionConfig.playersCanCollide;
        }

        if (a instanceof PlayerEntity player && b instanceof LivingEntity mob) {
            return playerVsMob(player, mob);
        }

        if (b instanceof PlayerEntity player && a instanceof LivingEntity mob) {
            return playerVsMob(player, mob);
        }

        return false;
    }

    private static boolean playerVsMob(PlayerEntity player, LivingEntity mob) {

        boolean isHostile = mob instanceof HostileEntity;
        boolean isPassive = mob instanceof PassiveEntity;

        if (isHostile) {
            return checkRule(
                    CollisionConfig.playersNoHostileCollision,
                    mob
            );
        }

        if (isPassive) {
            return checkRule(
                    CollisionConfig.playersNoPassiveCollision,
                    mob
            );
        }

        return false;
    }

    private static boolean checkRule(
            CollisionRule rule,
            LivingEntity mob
    ) {
        return switch (rule) {
            case ALL -> true;
            case LISTONLY -> MobConfig.isNoCollision(mob.getType());
            case OFF -> false;
        };
    }
}
