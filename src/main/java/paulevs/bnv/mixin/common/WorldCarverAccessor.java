package paulevs.bnv.mixin.common;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(WorldCarver.class)
public interface WorldCarverAccessor {
	@Accessor("replaceableBlocks")
	Set<Block> bnv_getReplaceableBlocks();
	
	@Accessor("replaceableBlocks")
	void bnv_setReplaceableBlocks(Set<Block> blocks);
}
