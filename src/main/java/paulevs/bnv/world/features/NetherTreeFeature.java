package paulevs.bnv.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public class NetherTreeFeature extends DefaultFeature {
	private final Block stem;
	
	public NetherTreeFeature(Block stem) {
		this.stem = stem;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		Random random = context.random();
		BlockPos pos = context.origin();
		
		if (!canGrow(level.getBlockState(pos.below()))) {
			return false;
		}
		
		int h = MHelper.randRange(5, 10, random);
		
		BlockState stem = this.stem.defaultBlockState();
		
		MutableBlockPos mpos = pos.mutable();
		for (int i = 0; i < h; i++) {
			if (!canReplace(level.getBlockState(mpos))) {
				return true;
			}
			BlocksHelper.setWithoutUpdate(level, mpos, stem);
			mpos.setY(mpos.getY() + 1);
		}
		
		return true;
	}
	
	private boolean canGrow(BlockState state) {
		return state.is(BlockTags.NYLIUM);
	}
	
	private boolean canReplace(BlockState state) {
		return state.isAir() || state.getMaterial().isReplaceable() || state.getMaterial().equals(Material.PLANT);
	}
}
