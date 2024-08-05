package io.github.LazyGamer1111.railsapi.mixins;

import io.github.LazyGamer1111.railsapi.AbstractBoosterRail;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin extends Entity {
    private double maxSpeed = 8.0;

    public AbstractMinecartEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    private boolean checkForNewPoweredRailTypes(BlockState state, Block block) {
        return state.isOf(block) || state.getBlock() instanceof AbstractBoosterRail;
    }

    @Redirect(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;add(DDD)Lnet/minecraft/util/math/Vec3d;", ordinal = 5))
    private Vec3d increaseAccelForNewRails(Vec3d vec, double x, double y, double z) {
        Vec3d newvec = vec.add(x, y, z);
        BlockState blockState = this.getWorld().getBlockState(this.getBlockPos());
        Block block = blockState.getBlock();
        if(block instanceof AbstractBoosterRail) {
            newvec.multiply(((AbstractBoosterRail) block).addedSpeed);
        }
        return newvec;
    }

    @Redirect(method = "moveOnRail", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(DD)D"))
    private double increaseSpeedCap(double a, double b) {
        return Math.min(8.0, b);
    }

    @Redirect(method = "moveOnRail", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/vehicle/AbstractMinecartEntity;getMaxSpeed()D"))
    public double increaseMaxSpeedOnNewRails(AbstractMinecartEntity instance) {
        double speed = maxSpeed;
        BlockState blockState = this.getWorld().getBlockState(this.getBlockPos());
        if (blockState.isOf(Blocks.POWERED_RAIL)) {
            speed = 8.0;
        } else if (blockState.getBlock() instanceof AbstractBoosterRail) {
            speed = ((AbstractBoosterRail) blockState.getBlock()).maxSpeed;
        }
        maxSpeed = speed;
        return speed / (this.isTouchingWater() ? 40.0 : 20.0);
    }
}


