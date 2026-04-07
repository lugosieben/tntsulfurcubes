package net.lugo.tntsulfurcubes.mixin;

import net.lugo.tntsulfurcubes.Ignitable;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SulfurCubeArchetype;
import net.minecraft.world.entity.monster.cubemob.SulfurCube;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(SulfurCube.class)
public class SulfurCubeMixin implements Ignitable {

	@Unique
	private boolean tntsulfurcubes$ignited = false;

	@Override
	public boolean tntsulfurcubes$isIgnited() {
		return tntsulfurcubes$ignited;
	}

	@Override
	public void tntsulfurcubes$setIgnited(boolean value) {
		this.tntsulfurcubes$ignited = value;
	}

	@Unique
	private static final EntityDataAccessor<Integer> tntsulfurcubes$FUSE = SynchedEntityData.defineId(SulfurCube.class, EntityDataSerializers.INT);

	@Override
	public int tntsulfurcubes$getFuse() {
		return ((SulfurCube) (Object) this).getEntityData().get(tntsulfurcubes$FUSE);
	}

	@Override
	public void tntsulfurcubes$setFuse(int value) {
		((SulfurCube) (Object) this).getEntityData().set(tntsulfurcubes$FUSE, value);
	}

	@Inject(at = @At("TAIL"), method = "defineSynchedData")
	private void tntsulfurcubes$defineSynchedData(SynchedEntityData.Builder entityData, CallbackInfo ci) {
		entityData.define(tntsulfurcubes$FUSE, 0);
	}

	@Inject(at = @At("HEAD"), method = "isSwallowableItem", cancellable = true)
	private static void isSwallowableItem(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
		if (itemStack.is(Items.TNT)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(at = @At("HEAD"), method = "matchingArchetypes", cancellable = true)
	private void matchingArchetypes(ItemStack stack, CallbackInfoReturnable<List<SulfurCubeArchetype>> cir) {
		if (stack.is(Items.TNT)) {
			stack = new ItemStack(Items.GRASS_BLOCK, 1);
			ItemStack finalStack = stack;
			cir.setReturnValue(
                    ((SulfurCube) (Object) this).level().registryAccess().lookupOrThrow(Registries.SULFUR_CUBE_ARCHETYPE).stream().filter((arch) -> finalStack.is(arch.items())).collect(Collectors.toCollection(ArrayList::new))
			);
		}
	}

	@Inject(at = @At("HEAD"), method = "mobInteract", cancellable = true)
	private void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		SulfurCube sulfurCube = (SulfurCube) (Object) this;
		if (tntsulfurcubes$getFuse() > 0) cir.setReturnValue(InteractionResult.PASS);
		if (sulfurCube.getItemBySlot(EquipmentSlot.BODY).is(Items.TNT)) {
			ItemStack held = player.getItemInHand(hand);

			if (!tntsulfurcubes$isIgnited()) {
				if (held.is(Items.FLINT_AND_STEEL)) {
					sulfurCube.level().playSound(player, sulfurCube.getX(), sulfurCube.getY(), sulfurCube.getZ(), SoundEvents.FLINTANDSTEEL_USE, sulfurCube.getSoundSource(), 1.0F, sulfurCube.getRandom().nextFloat() * 0.4F + 0.8F);
					if (!sulfurCube.level().isClientSide()) {
						tntsulfurcubes$setIgnited(true);
						held.hurtAndBreak(1, player, hand.asEquipmentSlot());
					}

					cir.setReturnValue(InteractionResult.SUCCESS);
				}

				if (held.is(Items.FIRE_CHARGE)) {
					sulfurCube.level().playSound(player, sulfurCube.getX(), sulfurCube.getY(), sulfurCube.getZ(), SoundEvents.FIRECHARGE_USE, sulfurCube.getSoundSource(), 1.0F, (sulfurCube.getRandom().nextFloat() - sulfurCube.getRandom().nextFloat()) * 0.2F + 1.0F);
					if (!sulfurCube.level().isClientSide()) {
						tntsulfurcubes$setIgnited(true);
						if (!player.getAbilities().instabuild) {
							held.shrink(1);
						}
					}

					cir.setReturnValue(InteractionResult.SUCCESS);
				}
			}
		}
	}
}