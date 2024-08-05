package com.github.LazyGamer1111.railsapi;

import net.minecraft.block.PoweredRailBlock;

public abstract class AbstractBoosterRail extends PoweredRailBlock {
    public final double maxSpeed;
    public final double addedSpeed;

    public AbstractBoosterRail(Settings settings, double maxSpeed, double addedSpeed){
        super(settings);

        this.maxSpeed = maxSpeed;
        this.addedSpeed = addedSpeed;
    }
}
