package net.ramixin.bbg.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.state.BeaconRenderState;
import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.ramixin.bbg.SectionDuck;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BeaconRenderer.class)
public class BeaconBlockEntityRendererMixin {

    @WrapOperation(method = "submit(Lnet/minecraft/client/renderer/blockentity/state/BeaconRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BeaconRenderer;submitBeaconBeam(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;FFIII)V"))
    private void preventRenderIfNoAlpha(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, float f, float g, int i, int j, int k, Operation<Void> original, @Local BeaconRenderState.Section section) {
        if(!SectionDuck.get(section).beamBeGone$isInvisible()) original.call(poseStack, submitNodeCollector, f, g, i, j, k);
    }

    @ModifyReturnValue(method = "method_74339", at = @At(value = "RETURN"))
    private static BeaconRenderState.Section transferSectionVisibilityOnExtract(BeaconRenderState.Section original, @Local(argsOnly = true) BeaconBeamOwner.Section blockSection) {
        SectionDuck blockDuck = SectionDuck.get(blockSection);
        SectionDuck returnDuck = SectionDuck.get(original);
        returnDuck.beamBeGone$setInvisible(blockDuck.beamBeGone$isInvisible());
        return original;
    }

}
