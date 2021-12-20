package paulevs.bnv.mixin.common;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.biome.NetherBiomes;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.NetherPlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetherBiomes.class)
public class NetherBiomesMixin {
	@Inject(method = "crimsonForest", at = @At("HEAD"), cancellable = true)
	private static void bnv_crimsonForest(CallbackInfoReturnable<Biome> info) {
		MobSpawnSettings mobSpawnSettings = new MobSpawnSettings
			.Builder()
			.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 1, 2, 4))
			.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.HOGLIN, 9, 3, 4))
			.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 5, 3, 4))
			.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2))
			.build();
		
		BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings
			.Builder()
			.addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE)
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA);
		
		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		
		builder
			.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN)
			.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE)
			.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA)
			.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE)
			.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA)
			.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED)
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.WEEPING_VINES)
			//.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TreePlacements.CRIMSON_FUNGI)
			//.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, BNVFeatures.CRIMSON_HUGE_FUNGUS.getPlacedFeature())
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.CRIMSON_FOREST_VEGETATION);
		
		BiomeDefaultFeatures.addNetherDefaultOres(builder);
		
		info.setReturnValue(new Biome.BiomeBuilder()
			.precipitation(Biome.Precipitation.NONE)
			.biomeCategory(Biome.BiomeCategory.NETHER)
			.temperature(2.0f)
			.downfall(0.0f)
			.specialEffects(new BiomeSpecialEffects
				.Builder()
				.waterColor(4159204)
				.waterFogColor(329011)
				.fogColor(0x330303).skyColor(calculateSkyColor(2.0f))
				.ambientParticle(new AmbientParticleSettings(ParticleTypes.CRIMSON_SPORE, 0.025f))
				.ambientLoopSound(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
				.ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD, 6000, 8, 2.0))
				.ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS, 0.0111))
				.backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST))
				.build()
			).mobSpawnSettings(mobSpawnSettings)
			.generationSettings(builder.build())
			.build()
		);
	}
	
	@Inject(method = "warpedForest", at = @At("HEAD"), cancellable = true)
	private static void warpedForest(CallbackInfoReturnable<Biome> info) {
		MobSpawnSettings mobSpawnSettings = new MobSpawnSettings
			.Builder()
			.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 1, 4, 4))
			.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 60, 1, 2))
			.addMobCharge(EntityType.ENDERMAN, 1.0, 0.12)
			.build();
		
		BiomeGenerationSettings.Builder builder = new BiomeGenerationSettings
			.Builder()
			.addCarver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE)
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, MiscOverworldPlacements.SPRING_LAVA);
		
		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		
		builder
			.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN)
			.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE)
			.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_SOUL_FIRE)
			.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA)
			.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE)
			.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA)
			.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED)
			//.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, TreePlacements.WARPED_FUNGI)
			//.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, BNVFeatures.WARPED_HUGE_FUNGUS.getPlacedFeature())
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.WARPED_FOREST_VEGETATION)
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.NETHER_SPROUTS)
			.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.TWISTING_VINES);
		
		BiomeDefaultFeatures.addNetherDefaultOres(builder);
		
		info.setReturnValue(new Biome.BiomeBuilder()
			.precipitation(Biome.Precipitation.NONE)
			.biomeCategory(Biome.BiomeCategory.NETHER)
			.temperature(2.0f)
			.downfall(0.0f)
			.specialEffects(new BiomeSpecialEffects.Builder()
					.waterColor(4159204)
					.waterFogColor(329011)
					.fogColor(1705242)
					.skyColor(calculateSkyColor(2.0f))
					.ambientParticle(new AmbientParticleSettings(ParticleTypes.WARPED_SPORE, 0.01428f))
					.ambientLoopSound(SoundEvents.AMBIENT_WARPED_FOREST_LOOP)
					.ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 2.0))
					.ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS, 0.0111))
					.backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_WARPED_FOREST))
					.build()
			).mobSpawnSettings(mobSpawnSettings)
			.generationSettings(builder.build()).build()
		);
	}
	
	private static int calculateSkyColor(float f) {
		float g = f;
		g /= 3.0f;
		g = Mth.clamp(g, -1.0f, 1.0f);
		return Mth.hsvToRgb(0.62222224f - g * 0.05f, 0.5f + g * 0.1f, 1.0f);
	}
}
