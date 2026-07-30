package expresscarts.mixin;

import expresscarts.ExpressCartsConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartMixin {

    @Inject(method = "getMaxSpeed", at = @At("HEAD"), cancellable = true)
    private void modifyMaxSpeed(CallbackInfoReturnable<Double> cir) {
        cir.setReturnValue(ExpressCartsConfig.maxCartSpeed);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void applyAcceleration(CallbackInfo ci) {
        AbstractMinecartEntity cart = (AbstractMinecartEntity) (Object) this;
        if (!cart.getWorld().isClient() && cart.hasPassengers()) {
            Entity passenger = cart.getFirstPassenger();
            if (passenger instanceof PlayerEntity player) {
                if (player.forwardSpeed > 0) {
                    Vec3d vel = cart.getVelocity();
                    cart.setVelocity(
                        vel.x * ExpressCartsConfig.accelerationMultiplier,
                        vel.y,
                        vel.z * ExpressCartsConfig.accelerationMultiplier
                    );
                }
            }
        }
    }
}
