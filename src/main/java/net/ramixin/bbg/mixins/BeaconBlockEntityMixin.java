package net.ramixin.bbg.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.ramixin.bbg.BeaconBlockEntityDuck;
import net.ramixin.bbg.BeamBeGone;
import net.ramixin.bbg.SectionDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin implements BeaconBlockEntityDuck {

    @Shadow private List<BeaconBeamOwner.Section> checkingBeamSections;

    @Unique
    private boolean invisiblePresent = false;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayList()Ljava/util/ArrayList;", remap = false))
    private static void resetInvisiblePresent(Level level, BlockPos blockPos, BlockState blockState, BeaconBlockEntity blockEntity, CallbackInfo ci) {
        BeaconBlockEntityDuck.get(blockEntity).beamBeGone$setInvisiblePresent(false);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private static boolean preventBuildCancelIfTinted(BlockState instance, Block block, Operation<Boolean> original, @Local LocalRef<BeaconBlockEntity.Section> sectionRef, @Local(argsOnly = true) BeaconBlockEntity beaconBlockEntity) {
        if (!instance.is(BeamBeGone.MAKES_BEAM_INVISIBLE)) return original.call(instance, block);
        BeaconBlockEntity.Section segment = sectionRef.get();
        BeaconBlockEntity.Section newSegment = new BeaconBlockEntity.Section(segment.getColor());
        boolean invisible = SectionDuck.get(segment).beamBeGone$isInvisible();
        if(BeaconBlockEntityDuck.get(beaconBlockEntity).beamBeGone$isInvisiblePresent())
            SectionDuck.get(segment).beamBeGone$decrementHeight();
        if(!invisible)
            BeaconBlockEntityDuck.get(beaconBlockEntity).beamBeGone$setInvisiblePresent(true);
        else {
            SectionDuck.get(segment).beamBeGone$incrementHeight();
            SectionDuck.get(newSegment).beamBeGone$decrementHeight();
        }
        SectionDuck.get(newSegment).beamBeGone$setInvisible(!invisible);

        sectionRef.set(newSegment);
        ((BeaconBlockEntityDuck)beaconBlockEntity).beamBeGone$getCheckingBeamSections().add(newSegment);
        return true;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private static boolean propagateInvisibleField(List<Object> instance, Object e, Operation<Boolean> original, @Local(argsOnly = true) BeaconBlockEntity beaconBlockEntity) {
        if(instance.isEmpty()) return original.call(instance, e);
        if(!(e instanceof BeaconBeamOwner.Section beamSegment)) return original.call(instance, e);
        if(!(instance.getLast() instanceof BeaconBlockEntity.Section top)) return original.call(instance, e);
        SectionDuck.get(beamSegment).beamBeGone$setInvisible(SectionDuck.get(top).beamBeGone$isInvisible());

        if(BeaconBlockEntityDuck.get(beaconBlockEntity).beamBeGone$isInvisiblePresent()) {
            SectionDuck.get(top).beamBeGone$decrementHeight();
            SectionDuck.get(beamSegment).beamBeGone$incrementHeight();
        }

        return original.call(instance, e);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getMinY()I"))
    private static void turnFirstSegmentInvisibleIfDirectGlass(Level level, BlockPos pos, BlockState blockState, BeaconBlockEntity blockEntity, CallbackInfo ci) {
        if(!level.getBlockState(pos.above()).is(BeamBeGone.MAKES_BEAM_INVISIBLE)) return;
        List<BeaconBeamOwner.Section> beamBuffer = BeaconBlockEntityDuck.get(blockEntity).beamBeGone$getCheckingBeamSections();
        if(beamBuffer.isEmpty()) return;
        SectionDuck.get(beamBuffer.getFirst()).beamBeGone$setInvisible(true);
    }

    @Override
    public List<BeaconBlockEntity.Section> beamBeGone$getCheckingBeamSections() {
        return checkingBeamSections;
    }

    @Override
    public boolean beamBeGone$isInvisiblePresent() {
        return invisiblePresent;
    }

    @Override
    public void beamBeGone$setInvisiblePresent(boolean present) {
        this.invisiblePresent = present;
    }
}