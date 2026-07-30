package expresscarts;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ExpressMinecartEntity extends AbstractMinecartEntity {

    public ExpressMinecartEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public ExpressMinecartEntity(EntityType<?> type, World world, double x, double y, double z) {
        super(type, world, x, y, z);
    }

    @Override
    protected Item getItem() {
        return ModItems.EXPRESS_MINECART;
    }

    // --- 新增：动态加速逻辑 ---
    @Override
    public double getMaxSpeed() {
        return ExpressCartsConfig.maxCartSpeed;
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient() && this.hasPassengers()) {
            Entity passenger = this.getFirstPassenger();
            if (passenger instanceof PlayerEntity player) {
                if (player.forwardSpeed > 0) {
                    Vec3d vel = this.getVelocity();
                    this.setVelocity(
                        vel.x * ExpressCartsConfig.accelerationMultiplier,
                        vel.y,
                        vel.z * ExpressCartsConfig.accelerationMultiplier
                    );
                }
            }
        }
    }
}
