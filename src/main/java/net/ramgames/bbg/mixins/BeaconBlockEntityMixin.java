package net.ramgames.bbg.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.ramgames.bbg.BeaconBlockEntityDuck;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Debug(export = true)
@Mixin(BeaconBlockEntity.class)
public class BeaconBlockEntityMixin implements BeaconBlockEntityDuck {

    @Unique
    public int blockedY = 0;


    @SuppressWarnings("LocalMayBeArgsOnly")
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V"))
    private static void preventClearIfTintedGlass(List<BeaconBlockEntity.BeamSegment> instance, Operation<Void> original, @Local(ordinal = 1) BlockPos blockPos, @Local World world, @Local BeaconBlockEntity.BeamSegment beamSegment, @Local BeaconBlockEntity beacon) {
        ((BeaconBlockEntityMixin)(Object)beacon).blockedY = blockPos.getY();
        if(!world.getBlockState(blockPos).getBlock().equals(Blocks.TINTED_GLASS)) original.call(instance);
        for(int i = blockPos.getY() ; i <= world.getHeight(); i++) {
            BlockPos pos = blockPos.withY(i);
            if(world.getBlockState(pos).getBlock().equals(Blocks.TINTED_GLASS)) continue;
            if(world.getBlockState(pos).getOpacity() >= 15) {
                original.call(instance);
                return;
            }
        }
    }

    @Override
    @Unique
    public int beamBeGone$getBlockedY() {
        return blockedY;
    }
}