package me.nihilanth.nocollision.mixin;

import me.nihilanth.nocollision.config.CollisionConfig;
import me.nihilanth.nocollision.config.CollisionRule;
import me.nihilanth.nocollision.config.MobConfig;
import me.nihilanth.nocollision.logic.CollisionRules;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityPushMixin {

    @Inject(
            method = "pushAwayFrom",
            at = @At("HEAD"),
            cancellable = true
    )
    private void nocollision$push(Entity other, CallbackInfo ci) {
        Entity self = (Entity) (Object) this;

        if (!(self instanceof LivingEntity) || !(other instanceof LivingEntity)) return;

        if (CollisionRules.shouldCancelCollision(self, other)) {
            ci.cancel();
        }
    }
}
