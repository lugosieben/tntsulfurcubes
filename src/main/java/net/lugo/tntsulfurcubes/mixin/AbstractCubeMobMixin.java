package net.lugo.tntsulfurcubes.mixin;

import net.lugo.tntsulfurcubes.Ignitable;
import net.lugo.tntsulfurcubes.TNTSulfurCubes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AbstractCubeMob.class)
public class AbstractCubeMobMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void tntennis$tick(CallbackInfo ci) {
        //noinspection ConstantValue
        if (!((Object) this instanceof SulfurCube sulfurCube)) {
            return;
        }

        Level level = sulfurCube.level();
        if (level.isClientSide()) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) level;
        GameRules gameRules = serverLevel.getGameRules();

        if (gameRules.get(TNTSulfurCubes.EXPLODE_ON_IMPACT)
                && sulfurCube.getItemBySlot(EquipmentSlot.BODY).is(Items.TNT)
                && (sulfurCube.horizontalCollision || sulfurCube.verticalCollision || !level.noEntityCollision(sulfurCube, sulfurCube.getBoundingBox()))) {
            double speed = sulfurCube.getDeltaMovement().length();
            double minSpeed = Math.max(0d, gameRules.get(TNTSulfurCubes.IMPACT_EXPLOSION_MIN_SPEED));
            if (speed < minSpeed) {
                return;
            }

            if (gameRules.get(GameRules.TNT_EXPLODES)) {
                double impactPower = Math.max(0d, gameRules.get(TNTSulfurCubes.IMPACT_EXPLOSION_POWER)) * speed;
                level.explode(sulfurCube, sulfurCube.getX(), sulfurCube.getY(), sulfurCube.getZ(), (float) impactPower, Level.ExplosionInteraction.TNT);
            }
            sulfurCube.discard();
            return;
        }

        Ignitable ignitable = (Ignitable) this;
        if (!ignitable.tntsulfurcubes$isIgnited()) {
            return;
        }

        int fuse = ignitable.tntsulfurcubes$getFuse();
        if (fuse >= 80) {
            if (gameRules.get(GameRules.TNT_EXPLODES)) {
                level.explode(sulfurCube, sulfurCube.getX(), sulfurCube.getY(), sulfurCube.getZ(), 4f, Level.ExplosionInteraction.TNT);
            }
            sulfurCube.discard();
            return;
        }

        ignitable.tntsulfurcubes$setFuse(fuse + 1);
    }
}
