package net.ramgames.bbg.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.ramgames.bbg.BeaconBlockEntityDuck;
import net.ramgames.bbg.BeamBeGone;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(BeaconBlockEntityRenderer.class)
public class BeaconBlockEntityRenderMixin {

    @ModifyConstant(method = "render", constant = @Constant(intValue = 2048))
    private int removeDefaultMaxHeightBeamIfTintedGlassBlocked(int constant, @Local(argsOnly = true) BlockEntity emitter, @Local BeaconBlockEntity.BeamSegment segment) {
        if(emitter.getWorld() == null) return constant;
        World world = emitter.getWorld();
        BlockPos pos = emitter.getPos();
        if(!(emitter instanceof BeaconBlockEntityDuck duck)) return constant;
        if(world.getBlockState(pos.withY(duck.beamBeGone$getBlockedY())).isIn(BeamBeGone.MAKES_BEAM_INVISIBLE)) return segment.getHeight();
        return constant;
    }

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void cancelIfTintedGlassDirectlyAboveBeacon(BlockEntity entity, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, Vec3d cameraPos, CallbackInfo ci) {
        if(entity.getWorld() == null) return;
        if(entity.getWorld().getBlockState(entity.getPos().up()).getBlock().equals(Blocks.TINTED_GLASS)) ci.cancel();
    }

}