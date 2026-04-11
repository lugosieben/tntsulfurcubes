package net.lugo.tntsulfurcubes;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public class TNTSulfurCubes implements ModInitializer {
	public static final String MOD_ID = "tntsulfurcubes";

	public static final GameRule<Boolean> EXPLODE_ON_IMPACT = GameRuleBuilder
			.forBoolean(false)
			.category(GameRuleCategory.MOBS)
			.buildAndRegister(Identifier.fromNamespaceAndPath(TNTSulfurCubes.MOD_ID, "explode_on_impact"));

	public static final GameRule<Double> IMPACT_EXPLOSION_MIN_SPEED = GameRuleBuilder
			.forDouble(0.5d)
			.minValue(0d)
			.category(GameRuleCategory.MOBS)
			.buildAndRegister(Identifier.fromNamespaceAndPath(TNTSulfurCubes.MOD_ID, "impact_explosion_min_speed"));

	public static final GameRule<Double> IMPACT_EXPLOSION_POWER = GameRuleBuilder
			.forDouble(4d)
			.minValue(0d)
			.category(GameRuleCategory.MOBS)
			.buildAndRegister(Identifier.fromNamespaceAndPath(TNTSulfurCubes.MOD_ID, "impact_explosion_power"));

	@Override
	public void onInitialize() {}
}