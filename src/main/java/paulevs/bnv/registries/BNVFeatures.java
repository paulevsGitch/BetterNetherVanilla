package paulevs.bnv.registries;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import paulevs.bnv.BNV;
import paulevs.bnv.world.features.NetherTreeFeature;
import paulevs.bnv.world.features.OverlayFixerFeature;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.features.BCLFeature;

public class BNVFeatures {
	public static final BCLFeature OVERLAY_FIXER = BCLFeature.makeChunkFeature(
		BNV.makeID("overlay_fixer"),
		Decoration.VEGETAL_DECORATION,
		new OverlayFixerFeature()
	);
	
	public static final BCLFeature CRIMSON_HUGE_FUNGUS = BCLFeature.makeVegetationFeature(
		BNV.makeID("crimson_huge_fungus"),
		new NetherTreeFeature(Blocks.CRIMSON_STEM, Blocks.CRIMSON_HYPHAE, Blocks.NETHER_WART_BLOCK),
		2, true
	);
	
	public static final BCLFeature WARPED_HUGE_FUNGUS = BCLFeature.makeVegetationFeature(
		BNV.makeID("warped_huge_fungus"),
		new NetherTreeFeature(Blocks.WARPED_STEM, Blocks.WARPED_HYPHAE, Blocks.WARPED_WART_BLOCK),
		2, true
	);
	
	public static void init() {
		BiomeAPI.registerNetherBiomeModification((biomeID, biome) -> {
			BCLBiome biome2 = BiomeAPI.getBiome(biomeID);
			BiomeAPI.addBiomeFeature(biome2.getActualBiome(), OVERLAY_FIXER);
		});
		
		/*BiomeModifications.addFeature(
			biomeSelectionContext -> biomeSelectionContext
				.getBiome()
				.getBiomeCategory().equals(BiomeCategory.NETHER),
			OVERLAY_FIXER.getDecoration(),
			BuiltinRegistries.PLACED_FEATURE.getResourceKey(OVERLAY_FIXER.getPlacedFeature()).get()
		);*/
	}
}