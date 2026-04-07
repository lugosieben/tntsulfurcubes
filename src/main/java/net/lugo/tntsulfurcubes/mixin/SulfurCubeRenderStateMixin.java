package net.lugo.tntsulfurcubes.mixin;

import net.lugo.tntsulfurcubes.Fuse;
import net.minecraft.client.renderer.entity.state.SulfurCubeRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SulfurCubeRenderState.class)
public class SulfurCubeRenderStateMixin implements Fuse {

    @Unique
    private int tntsulfurcubes$FUSE = 0;

    @Override
    public float tntsulfurcubes$getFuse() {
        return tntsulfurcubes$FUSE;
    }

    @Override
    public void tntsulfurcubes$setFuse(int fuse) {
        tntsulfurcubes$FUSE = fuse;
    }
}
