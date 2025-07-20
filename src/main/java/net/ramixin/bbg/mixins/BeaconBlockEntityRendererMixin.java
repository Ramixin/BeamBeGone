package net.ramixin.bbg.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BeaconBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.ramixin.bbg.BeamSegmentDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BeaconBlockEntityRenderer.class)
public class BeaconBlockEntityRendererMixin {

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BeaconBlockEntityRenderer;renderBeam(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;FFJIII)V"))
    private void preventRenderIfNoAlpha(MatrixStack matrices, VertexConsumerProvider vertexConsumers, float tickProgress, float scale, long worldTime, int yOffset, int maxY, int color, Operation<Void> original, @Local BeaconBlockEntity.BeamSegment segment) {
        if(!BeamSegmentDuck.get(segment).beamBeGone$isInvisible()) original.call(matrices, vertexConsumers, tickProgress, scale, worldTime, yOffset, maxY, color);
    }


}
