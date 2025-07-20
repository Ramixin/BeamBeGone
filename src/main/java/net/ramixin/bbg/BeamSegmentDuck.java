package net.ramixin.bbg;

import net.minecraft.block.entity.BeaconBlockEntity;

public interface BeamSegmentDuck {

    boolean beamBeGone$isInvisible();

    void beamBeGone$setInvisible(boolean invisible);

    static BeamSegmentDuck get(BeaconBlockEntity.BeamSegment segment) {
        return (BeamSegmentDuck) segment;
    }

    void beamBeGone$decrementHeight();

    void beamBeGone$incrementHeight();
}
