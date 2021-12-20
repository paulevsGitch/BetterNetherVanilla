package paulevs.bnv.registries;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.SurfaceRules.ConditionSource;
import net.minecraft.world.level.levelgen.SurfaceRules.RuleSource;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.api.surface.SurfaceRuleBuilder;

public class BNVBiomes {
	private static final ConditionSource FLOOR = SurfaceRules.verticalGradient("bedrock_floor", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5));
	private static final ConditionSource CEIL = SurfaceRules.not(SurfaceRules.verticalGradient("bedrock_roof", VerticalAnchor.belowTop(5), VerticalAnchor.top()));
	private static final RuleSource BEDROCK = SurfaceRules.state(Blocks.BEDROCK.defaultBlockState());
	private static final BlockState NETHERRACK = Blocks.NETHERRACK.defaultBlockState();
	
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
			.filler(NETHERRACK)
			.biome(biome)
			.build();
		BiomeAPI.addSurfaceRule(biome.location(), surface);
	}
}
