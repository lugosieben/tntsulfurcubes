package net.lugo.tntsulfurcubes.mixin;

import net.lugo.tntsulfurcubes.Fuse;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.SulfurCubeRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(at = @At("HEAD"), method = "getWhiteOverlayProgress", cancellable = true)
    protected void gwop(LivingEntityRenderState state, CallbackInfoReturnable<Float> cir) {
        if ((Object) this instanceof SulfurCubeRenderer) {
            float swell = ((Fuse) state).tntsulfurcubes$getFuse();
            if (swell <= 0.0F) {
                cir.setReturnValue(0.0F);
            } else {
                float f = swell / 80f;
                cir.setReturnValue((int) (f * 20.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F));
            }
        }
    }
}
