package net.lugo.tntsulfurcubes.mixin;

import net.lugo.tntsulfurcubes.Ignitable;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AbstractCubeMob.class)
public class AbstractCubeMobMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void tntennis$tick(CallbackInfo ci) {
        if (!((Object) this instanceof SulfurCube)) {
            return;
        }
        SulfurCube sulfurCube = (SulfurCube) (Object) this;
        Ignitable ignitable = (Ignitable) this;
        if (!ignitable.tntsulfurcubes$isIgnited()) {
            return;
        }

        Level level = sulfurCube.level();
        if (level.isClientSide()) {
            return;
        }

        int fuse = ignitable.tntsulfurcubes$getFuse();
        if (fuse >= 80) {
            level.explode(sulfurCube, sulfurCube.getX(), sulfurCube.getY(), sulfurCube.getZ(), 4f, Level.ExplosionInteraction.TNT);
            sulfurCube.discard();
            return;
        }

        ignitable.tntsulfurcubes$setFuse(fuse + 1);
    }
}
