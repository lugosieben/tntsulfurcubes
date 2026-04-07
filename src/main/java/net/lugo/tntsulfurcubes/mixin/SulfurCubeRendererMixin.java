package net.lugo.tntsulfurcubes.mixin;

import net.lugo.tntsulfurcubes.Fuse;
import net.lugo.tntsulfurcubes.Ignitable;
import net.minecraft.client.renderer.entity.SulfurCubeRenderer;
import net.minecraft.client.renderer.entity.state.SulfurCubeRenderState;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SulfurCubeRenderer.class)
public class SulfurCubeRendererMixin {
    @Inject(at = @At("TAIL"), method = "extractRenderState(Lnet/minecraft/world/entity/monster/cubemob/SulfurCube;Lnet/minecraft/client/renderer/entity/state/SulfurCubeRenderState;F)V")
    protected void tntsulfurcubes$extractRenderState(SulfurCube entity, SulfurCubeRenderState state, float partialTicks, CallbackInfo ci) {
        if ((Object) this instanceof SulfurCubeRenderer) {
            Fuse zombieRenderState = (Fuse) state;
            zombieRenderState.tntsulfurcubes$setFuse(((Ignitable) entity).tntsulfurcubes$getFuse());
        }
    }
}

