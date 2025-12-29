package net.ramixin.bbg;

import net.minecraft.world.level.block.entity.BeaconBlockEntity;

import java.util.List;

public interface BeaconBlockEntityDuck {

    List<BeaconBlockEntity.Section> beamBeGone$getCheckingBeamSections();

    boolean beamBeGone$isInvisiblePresent();

    void beamBeGone$setInvisiblePresent(boolean present);

    static BeaconBlockEntityDuck get(BeaconBlockEntity beacon) {
        return (BeaconBlockEntityDuck) beacon;
    }
}
