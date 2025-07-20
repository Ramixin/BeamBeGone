package net.ramixin.bbg.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ramixin.bbg.BeaconBlockEntityDuck;
import net.ramixin.bbg.BeamBeGone;
import net.ramixin.bbg.BeamSegmentDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin implements BeaconBlockEntityDuck {

    @Shadow private List<BeaconBlockEntity.BeamSegment> field_19178;

    @Unique
    private boolean invisiblePresent = false;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Lists;newArrayList()Ljava/util/ArrayList;", remap = false))
    private static void resetInvisiblePresent(World world, BlockPos pos, BlockState state, BeaconBlockEntity blockEntity, CallbackInfo ci) {
        BeaconBlockEntityDuck.get(blockEntity).beamBeGone$setInvisiblePresent(false);
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
    private static boolean preventBuildCancelIfTinted(BlockState instance, Block block, Operation<Boolean> original, @Local LocalRef<BeaconBlockEntity.BeamSegment> segmentRef, @Local(argsOnly = true) BeaconBlockEntity beaconBlockEntity) {
        if (!instance.isIn(BeamBeGone.MAKES_BEAM_INVISIBLE)) return original.call(instance, block);
        BeaconBlockEntity.BeamSegment segment = segmentRef.get();
        BeaconBlockEntity.BeamSegment newSegment = new BeaconBlockEntity.BeamSegment(segment.getColor());
        boolean invisible = BeamSegmentDuck.get(segment).beamBeGone$isInvisible();
        if(BeaconBlockEntityDuck.get(beaconBlockEntity).beamBeGone$isInvisiblePresent())
            BeamSegmentDuck.get(segment).beamBeGone$decrementHeight();
        if(!invisible)
            BeaconBlockEntityDuck.get(beaconBlockEntity).beamBeGone$setInvisiblePresent(true);
        else {
            BeamSegmentDuck.get(segment).beamBeGone$incrementHeight();
            BeamSegmentDuck.get(newSegment).beamBeGone$decrementHeight();
        }
        BeamSegmentDuck.get(newSegment).beamBeGone$setInvisible(!invisible);

        segmentRef.set(newSegment);
        ((BeaconBlockEntityDuck)beaconBlockEntity).beamBeGone$getBeamBuffer().add(newSegment);
        return true;
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private static boolean propagateInvisibleField(List<Object> instance, Object e, Operation<Boolean> original, @Local(argsOnly = true) BeaconBlockEntity beaconBlockEntity) {
        if(instance.isEmpty()) return original.call(instance, e);
        if(!(e instanceof BeaconBlockEntity.BeamSegment beamSegment)) return original.call(instance, e);
        if(!(instance.getLast() instanceof BeaconBlockEntity.BeamSegment top)) return original.call(instance, e);
        BeamSegmentDuck.get(beamSegment).beamBeGone$setInvisible(BeamSegmentDuck.get(top).beamBeGone$isInvisible());

        if(BeaconBlockEntityDuck.get(beaconBlockEntity).beamBeGone$isInvisiblePresent()) {
            BeamSegmentDuck.get(top).beamBeGone$decrementHeight();
            BeamSegmentDuck.get(beamSegment).beamBeGone$incrementHeight();
        }

        return original.call(instance, e);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBottomY()I"))
    private static void turnFirstSegmentInvisibleIfDirectGlass(World world, BlockPos pos, BlockState state, BeaconBlockEntity blockEntity, CallbackInfo ci) {
        if(!world.getBlockState(pos.up()).isIn(BeamBeGone.MAKES_BEAM_INVISIBLE)) return;
        List<BeaconBlockEntity.BeamSegment> beamBuffer = BeaconBlockEntityDuck.get(blockEntity).beamBeGone$getBeamBuffer();
        if(beamBuffer.isEmpty()) return;
        BeamSegmentDuck.get(beamBuffer.getFirst()).beamBeGone$setInvisible(true);
    }

    @Override
    public List<BeaconBlockEntity.BeamSegment> beamBeGone$getBeamBuffer() {
        return field_19178;
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