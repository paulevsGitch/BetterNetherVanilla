package paulevs.bnv.registries;

import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import paulevs.bnv.BNV;
import paulevs.bnv.world.features.MouldClusterFeature;
import paulevs.bnv.world.features.MouldSingleFeature;
import paulevs.bnv.world.features.OverlayFixerFeature;
import paulevs.bnv.world.features.TreeFungusFeature;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.world.features.BCLFeature;

public class BNVFeatures {
	public static final BCLFeature OVERLAY_FIXER = BCLFeature.makeChunkFeature(
		BNV.makeID("overlay_fixer"),
		Decoration.TOP_LAYER_MODIFICATION,
		new OverlayFixerFeature()
	);
	
	public static final BCLFeature CRIMSON_HUGE_FUNGUS = BCLFeature.makeVegetationFeature(
		BNV.makeID("crimson_huge_fungus"), new TreeFungusFeature(
			Blocks.CRIMSON_STEM,
			Blocks.CRIMSON_HYPHAE,
			Blocks.NETHER_WART_BLOCK,
			Blocks.SHROOMLIGHT,
			BNVBlocks.CRIMSON_WEEPING_MOSS,
			0.75F, 1.7F
		), 2, true
	);
	
	public static final BCLFeature WARPED_HUGE_FUNGUS = BCLFeature.makeVegetationFeature(
		BNV.makeID("warped_huge_fungus"), new TreeFungusFeature(
			Blocks.WARPED_STEM,
			Blocks.WARPED_HYPHAE,
			Blocks.WARPED_WART_BLOCK,
			Blocks.SHROOMLIGHT,
			BNVBlocks.WARPED_WEEPING_MOSS,
			4F, 2.5F
		), 2, true
	);
	
	public static final BCLFeature CRIMSON_MOULD_CLUSTER = BCLFeature.makeVegetationFeature(
		BNV.makeID("crimson_mould_cluster"), new MouldClusterFeature(BNVBlocks.CRIMSON_MOULD), 1, true
	);
	
	public static final BCLFeature WARPED_MOULD_CLUSTER = BCLFeature.makeVegetationFeature(
		BNV.makeID("warped_mould_cluster"), new MouldClusterFeature(BNVBlocks.WARPED_MOULD), 1, true
	);
	
	public static final BCLFeature CRIMSON_MOULD_SINGLE = BCLFeature.makeVegetationFeature(
		BNV.makeID("crimson_mould_single"), new MouldSingleFeature(BNVBlocks.CRIMSON_MOULD), 1, true
	);
	
	public static final BCLFeature WARPED_MOULD_SINGLE = BCLFeature.makeVegetationFeature(
		BNV.makeID("warped_mould_single"), new MouldSingleFeature(BNVBlocks.WARPED_MOULD), 1, true
	);
	
	public static void init() {
		BiomeAPI.registerNetherBiomeModification((biomeID, biome) -> {
			BiomeAPI.addBiomeFeature(biome, OVERLAY_FIXER);
			if (biomeID.equals(Biomes.CRIMSON_FOREST.location())) {
				BiomeAPI.addBiomeFeature(biome, CRIMSON_HUGE_FUNGUS);
				BiomeAPI.addBiomeFeature(biome, CRIMSON_MOULD_CLUSTER);
				BiomeAPI.addBiomeFeature(biome, WARPED_MOULD_SINGLE);
			}
			else if (biomeID.equals(Biomes.WARPED_FOREST.location())) {
				BiomeAPI.addBiomeFeature(biome, WARPED_HUGE_FUNGUS);
				BiomeAPI.addBiomeFeature(biome, WARPED_MOULD_CLUSTER);
				BiomeAPI.addBiomeFeature(biome, CRIMSON_MOULD_SINGLE);
			}
		});
	}
}
