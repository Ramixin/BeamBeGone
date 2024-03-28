package net.ramgames.btt.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.ramgames.btt.BeaconBlockEntityAccess;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Debug(export = true)
@Mixin(BeaconBlockEntityRenderer.class)
public class BeaconBlockEntityRenderMixin {

    @ModifyConstant(method = "render(Lnet/minecraft/block/entity/BeaconBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", constant = @Constant(intValue = 1024))
    private int removeDefaultMaxHeightBeamIfTintedGlassBlocked(int constant, @Local(argsOnly = true) BeaconBlockEntity beacon, @Local BeaconBlockEntity.BeamSegment segment) {
        if(beacon.getWorld() == null) return constant;
        if(beacon.getWorld().getBlockState(beacon.getPos().withY(((BeaconBlockEntityAccess)beacon).beamThroughTint$getBlockedY())).getBlock().equals(Blocks.TINTED_GLASS)) return segment.getHeight();
        return constant;
    }

}
