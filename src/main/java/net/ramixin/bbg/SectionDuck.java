package net.ramixin.bbg;

import net.minecraft.client.renderer.blockentity.state.BeaconRenderState;
import net.minecraft.world.level.block.entity.BeaconBeamOwner;

public interface SectionDuck {

    boolean beamBeGone$isInvisible();

    void beamBeGone$setInvisible(boolean invisible);

    static SectionDuck get(BeaconBeamOwner.Section section) {
        return (SectionDuck) section;
    }

    static SectionDuck get(BeaconRenderState.Section section) {
        return SectionDuck.class.cast(section);
    }

    void beamBeGone$decrementHeight();

    void beamBeGone$incrementHeight();
}
