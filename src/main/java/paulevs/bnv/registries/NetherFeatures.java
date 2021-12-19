package paulevs.bnv.registries;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.biome.Biome.BiomeCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import paulevs.bnv.BNV;
import paulevs.bnv.world.features.NetherTreeFeature;
import paulevs.bnv.world.features.OverlayFixerFeature;
import ru.bclib.world.features.BCLFeature;

public class NetherFeatures {
	public static final BCLFeature OVERLAY_FIXER = BCLFeature.makeChunkFeature(
		BNV.makeID("overlay_fixer"),
		Decoration.VEGETAL_DECORATION,
		new OverlayFixerFeature()
	);
	
	public static final BCLFeature CRIMSON_HUGE_FUNGUS = BCLFeature.makeCountFeature(
		BNV.makeID("crimson_huge_fungus"),
		Decoration.VEGETAL_DECORATION,
		new NetherTreeFeature(Blocks.CRIMSON_STEM),
		2
	);
	
	public static final BCLFeature WARPED_HUGE_FUNGUS = BCLFeature.makeCountFeature(
		BNV.makeID("warped_huge_fungus"),
		Decoration.VEGETAL_DECORATION,
		new NetherTreeFeature(Blocks.WARPED_STEM),
		2
	);
	
	public static void init() {
		/*BiomeAPI.registerNetherBiomeModification((biomeID, biome) -> {
			System.out.println(biomeID);
			BCLBiome biome2 = BiomeAPI.getBiome(biomeID);
			BiomeAPI.addBiomeFeatures(biome2.getActualBiome(), OVERLAY_FIXER);
		});*/
		
		BiomeModifications.addFeature(
			biomeSelectionContext -> biomeSelectionContext
				.getBiome()
				.getBiomeCategory().equals(BiomeCategory.NETHER),
			OVERLAY_FIXER.getDecoration(),
			BuiltinRegistries.PLACED_FEATURE.getResourceKey(OVERLAY_FIXER.getPlacedFeature()).get()
		);
	}
}
