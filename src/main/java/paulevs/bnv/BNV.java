package paulevs.bnv;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import paulevs.bnv.registries.BNVBiomes;
import paulevs.bnv.registries.BNVBlocks;
import paulevs.bnv.registries.BNVFeatures;
import ru.bclib.BCLib;
import ru.bclib.registry.BaseRegistry;
import ru.bclib.util.TranslationHelper;

public class BNV implements ModInitializer {
	public static final String MOD_ID = "bnv";
	
	public static final CreativeModeTab CREATIVE_TAB = FabricItemGroupBuilder
		.create(makeID("bnv"))
		.icon(() -> new ItemStack(Blocks.CRIMSON_NYLIUM))
		.appendItems(stacks -> {
			stacks.addAll(BaseRegistry.getModBlockItems(MOD_ID).stream().map(ItemStack::new).toList());
			stacks.addAll(BaseRegistry.getModItems(MOD_ID).stream().map(ItemStack::new).toList());
		}).build();
	
	@Override
	public void onInitialize() {
		BNVBlocks.init();
		BNVBiomes.init();
		BNVFeatures.init();
		if (BCLib.isDevEnvironment() && BCLib.isClient()) {
			TranslationHelper.printMissingEnNames(MOD_ID);
			TranslationHelper.printMissingNames(MOD_ID, "ru_ru");
		}
	}

	public static ResourceLocation makeID(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
