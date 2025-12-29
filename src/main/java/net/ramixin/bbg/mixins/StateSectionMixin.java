package net.ramixin.bbg.mixins;

import net.minecraft.client.renderer.blockentity.state.BeaconRenderState;
import net.ramixin.bbg.SectionDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BeaconRenderState.Section.class)
public class StateSectionMixin implements SectionDuck {

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
        throw new IllegalCallerException("Cannot decrement height of a section in render state");
    }

    @Override
    public void beamBeGone$incrementHeight() {
        throw new IllegalCallerException("Cannot increment height of a section in render state");
    }
}
