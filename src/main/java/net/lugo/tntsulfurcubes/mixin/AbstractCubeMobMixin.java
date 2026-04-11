package net.lugo.tntsulfurcubes.mixin;

import net.lugo.tntsulfurcubes.Ignitable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AbstractCubeMob.class)
public class AbstractCubeMobMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void tntennis$tick(CallbackInfo ci) {
        if (!((Object) this instanceof SulfurCube sulfurCube)) {
            return;
        }
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
            if (((ServerLevel)sulfurCube.level()).getGameRules().get(GameRules.TNT_EXPLODES)) {
                level.explode(sulfurCube, sulfurCube.getX(), sulfurCube.getY(), sulfurCube.getZ(), 4f, Level.ExplosionInteraction.TNT);
            }
            sulfurCube.discard();
            return;
        }

        ignitable.tntsulfurcubes$setFuse(fuse + 1);
    }
}
