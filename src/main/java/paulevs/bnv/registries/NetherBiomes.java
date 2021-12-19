package paulevs.bnv.registries;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.api.surface.SurfaceRuleBuilder;

public class NetherBiomes {
	public static void init() {
		surface(Biomes.WARPED_FOREST, NetherBlocks.WARPED_NYLIUM_OVERLAY);
		surface(Biomes.CRIMSON_FOREST, NetherBlocks.CRIMSON_NYLIUM_OVERLAY);
	}
	
	private static void surface(ResourceKey<Biome> biome, Block block) {
		RuleSource surface = SurfaceRuleBuilder.start().surface(block.defaultBlockState()).biome(biome).build();
		BiomeAPI.addSurfaceRule(biome.location(), surface);
	}
}
