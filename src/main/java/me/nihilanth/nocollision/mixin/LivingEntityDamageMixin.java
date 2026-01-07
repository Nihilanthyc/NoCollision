package me.nihilanth.nocollision.mixin;

import me.nihilanth.nocollision.config.MobConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityDamageMixin {

    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void nocollision$cancelCramming(
            ServerWorld world,
            DamageSource source,
            float amount,
            CallbackInfoReturnable<Boolean> cir
    ) {
        LivingEntity self = (LivingEntity) (Object) this;

        if (!MobConfig.isNoCollision(self.getType())) return;

        if (source.isOf(DamageTypes.CRAMMING)) {
            cir.setReturnValue(false);
        }
    }
}

