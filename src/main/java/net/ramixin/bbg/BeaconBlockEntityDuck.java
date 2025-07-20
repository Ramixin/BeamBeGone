package net.ramixin.bbg;

import net.minecraft.block.entity.BeaconBlockEntity;

import java.util.List;

public interface BeaconBlockEntityDuck {

    List<BeaconBlockEntity.BeamSegment> beamBeGone$getBeamBuffer();

    boolean beamBeGone$isInvisiblePresent();

    void beamBeGone$setInvisiblePresent(boolean present);

    static BeaconBlockEntityDuck get(BeaconBlockEntity beacon) {
        return (BeaconBlockEntityDuck) beacon;
    }
}
