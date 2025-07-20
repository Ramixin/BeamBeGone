package net.ramixin.bbg.mixins;

import net.minecraft.block.entity.BeaconBlockEntity;
import net.ramixin.bbg.BeamSegmentDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BeaconBlockEntity.BeamSegment.class)
public abstract class BeamSegmentMixin implements BeamSegmentDuck {

    @Shadow private int height;

    @Shadow
    public abstract void increaseHeight();

    @Unique
    private boolean invisible = false;


    @Override
    public boolean beamBeGone$isInvisible() {
        return invisible;
    }

    @Override
    public void beamBeGone$setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    @Override
    public void beamBeGone$decrementHeight() {
        --height;
    }

    @Override
    public void beamBeGone$incrementHeight() {
        increaseHeight();
    }
}
