package paulevs.bnv.registries;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.NetherPlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules.ConditionSource;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import paulevs.bnv.BNV;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.api.surface.SurfaceRuleBuilder;
import ru.bclib.world.biomes.BCLBiome;

public class BNVBiomes {
	private static final ConditionSource FLOOR = SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5));
	private static final ConditionSource CEIL = SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top()));
	private static final RuleSource GRAVEL_SHORE = SurfaceRules.ifTrue(
		SurfaceRules.stoneDepthCheck(4, false, false, CaveSurface.FLOOR),
		SurfaceRules.ifTrue(
			SurfaceRules.verticalGradient("bnv:gravel_shore", VerticalAnchor.aboveBottom(32), VerticalAnchor.aboveBottom(37)),
			SurfaceRules.state(Blocks.GRAVEL.defaultBlockState())
		)
	);
	private static final RuleSource BEDROCK = SurfaceRules.state(Blocks.BEDROCK.defaultBlockState());
	private static final BlockState NETHERRACK = Blocks.NETHERRACK.defaultBlockState();
	
	public static final BCLBiome CRIMSON_FOREST_GLADE = BiomeAPI.registerSubBiome(
		BiomeAPI.CRIMSON_FOREST_BIOME,
		BCLBiomeBuilder
			.start(BNV.makeID("crimson_forest_glade"))
			.skyColor(7254527)
			.waterColor(4159204)
			.waterFogColor(329011)
			.fogColor(0x330303)
			.particles(ParticleTypes.CRIMSON_SPORE, 0.025F)
			.loop(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
			.mood(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD, 6000, 8, 2.0F)
			.additions(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS)
			.music(SoundEvents.MUSIC_BIOME_CRIMSON_FOREST)
			.surface(surface(BNVBlocks.CRIMSON_NYLIUM_OVERLAY))
			.carver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE)
			.feature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE)
			.feature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA)
			.feature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE)
			.feature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA)
			.feature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED)
			.feature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.WEEPING_VINES)
			.feature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.CRIMSON_FOREST_VEGETATION)
			.feature(BNVFeatures.CRIMSON_MOULD_CLUSTER)
			.feature(BNVFeatures.WARPED_MOULD_SINGLE)
			.spawn(EntityType.ZOMBIFIED_PIGLIN, 1, 2, 4)
			.spawn(EntityType.HOGLIN, 9, 3, 4)
			.spawn(EntityType.PIGLIN, 5, 3, 4)
			.spawn(EntityType.STRIDER, 60, 1, 2)
			.genChance(0.3F)
			.build()
	);
	
	public static final BCLBiome WARPED_FOREST_GLADE = BiomeAPI.registerSubBiome(
		BiomeAPI.WARPED_FOREST_BIOME,
		BCLBiomeBuilder
			.start(BNV.makeID("warped_forest_glade"))
			.skyColor(7254527)
			.waterColor(4159204)
			.waterFogColor(329011)
			.fogColor(1705242)
			.particles(ParticleTypes.WARPED_SPORE, 0.01428F)
			.loop(SoundEvents.AMBIENT_WARPED_FOREST_LOOP)
			.mood(SoundEvents.AMBIENT_WARPED_FOREST_MOOD, 6000, 8, 2.0F)
			.additions(SoundEvents.AMBIENT_WARPED_FOREST_ADDITIONS)
			.music(SoundEvents.MUSIC_BIOME_WARPED_FOREST)
			.surface(surface(BNVBlocks.WARPED_NYLIUM_OVERLAY))
			.carver(GenerationStep.Carving.AIR, Carvers.NETHER_CAVE)
			.feature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE)
			.feature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_SOUL_FIRE)
			.feature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA)
			.feature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE)
			.feature(GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA)
			.feature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.WARPED_FOREST_VEGETATION)
			.feature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.NETHER_SPROUTS)
			.feature(GenerationStep.Decoration.VEGETAL_DECORATION, NetherPlacements.TWISTING_VINES)
			.feature(BNVFeatures.WARPED_MOULD_CLUSTER)
			.feature(BNVFeatures.CRIMSON_MOULD_SINGLE)
			.spawn(EntityType.ENDERMAN, 1, 4, 4)
			.spawn(EntityType.STRIDER, 60, 1, 2)
			.genChance(0.3F)
			.build()
	);
	
	public static void init() {
		surface(Biomes.WARPED_FOREST, BNVBlocks.WARPED_NYLIUM_OVERLAY);
		surface(Biomes.CRIMSON_FOREST, BNVBlocks.CRIMSON_NYLIUM_OVERLAY);
	}
	
	private static void surface(ResourceKey<Biome> biome, Block block) {
		RuleSource surface = SurfaceRuleBuilder
			.start()
			.surface(block.defaultBlockState())
			.rule(-1, SurfaceRules.ifTrue(FLOOR, BEDROCK))
			.rule(-1, SurfaceRules.ifTrue(CEIL, BEDROCK))
			.rule(-1, GRAVEL_SHORE)
			.filler(NETHERRACK)
			.biome(biome)
			.build();
		BiomeAPI.addSurfaceRule(biome.location(), surface);
	}
	
	private static RuleSource surface(Block block) {
		return SurfaceRuleBuilder
			.start()
			.surface(block.defaultBlockState())
			.rule(-1, SurfaceRules.ifTrue(FLOOR, BEDROCK))
			.rule(-1, SurfaceRules.ifTrue(CEIL, BEDROCK))
			.rule(-1, GRAVEL_SHORE)
			.filler(NETHERRACK)
			.build();
	}
}
