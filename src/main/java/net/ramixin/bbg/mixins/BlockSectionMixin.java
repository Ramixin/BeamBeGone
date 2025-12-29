package net.ramixin.bbg.mixins;

import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.ramixin.bbg.SectionDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BeaconBeamOwner.Section.class)
public abstract class BlockSectionMixin implements SectionDuck {

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
