package paulevs.bnv.registries;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import paulevs.bnv.BNV;
import paulevs.bnv.blocks.NyliumOverlay;
import paulevs.bnv.blocks.WeepingMoss;
import paulevs.bnv.mixin.common.WorldCarverAccessor;
import ru.bclib.api.BonemealAPI;
import ru.bclib.config.PathConfig;
import ru.bclib.registry.BlockRegistry;
import ru.bclib.util.CollectionsUtil;

import java.util.Set;

public class BNVBlocks {
	public static final BlockRegistry REGISTRY = new BlockRegistry(BNV.CREATIVE_TAB, new PathConfig(BNV.MOD_ID, "blocks"));
	
	public static final Block CRIMSON_NYLIUM_OVERLAY = registerBO("crimson_nylium_overlay", new NyliumOverlay(Blocks.CRIMSON_NYLIUM));
	public static final Block WARPED_NYLIUM_OVERLAY = registerBO("warped_nylium_overlay", new NyliumOverlay(Blocks.WARPED_NYLIUM));
	public static final Block CRIMSON_WEEPING_MOSS = register("crimson_weeping_moss", new WeepingMoss());
	public static final Block WARPED_WEEPING_MOSS = register("warped_weeping_moss", new WeepingMoss());
	
	public static void init() {
		BonemealAPI.addSpreadableBlock(CRIMSON_NYLIUM_OVERLAY, Blocks.NETHERRACK);
		BonemealAPI.addSpreadableBlock(WARPED_NYLIUM_OVERLAY, Blocks.NETHERRACK);
		
		WorldCarverAccessor accessor = WorldCarverAccessor.class.cast(WorldCarver.NETHER_CAVE);
		Set<Block> blocks = CollectionsUtil.getMutable(accessor.bnv_getReplaceableBlocks());
		blocks.add(CRIMSON_NYLIUM_OVERLAY);
		blocks.add(WARPED_NYLIUM_OVERLAY);
		accessor.bnv_setReplaceableBlocks(blocks);
	}
	
	private static Block register(String name, Block block) {
		return REGISTRY.register(BNV.makeID(name), block);
	}
	
	private static Block registerBO(String name, Block block) {
		return REGISTRY.registerBlockOnly(BNV.makeID(name), block);
	}
}
