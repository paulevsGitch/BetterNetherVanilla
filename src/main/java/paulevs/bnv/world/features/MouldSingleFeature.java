package paulevs.bnv.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import paulevs.bnv.blocks.BNVBlockProperties;
import paulevs.bnv.blocks.BNVBlockProperties.TripplePlant;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public class MouldSingleFeature extends DefaultFeature {
	private final BlockState[] states = new BlockState[3];
	private Block block;
	
	public MouldSingleFeature(Block block) {
		this.block = block;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos center = context.origin();
		Random random = context.random();
		
		if (random.nextInt(4) > 0 || !level.getBlockState(center.below()).is(BlockTags.NYLIUM)) {
			return false;
		}
		
		if (states[0] == null) {
			states[0] = block.defaultBlockState().setValue(BNVBlockProperties.TRIPPLE_PLANT, TripplePlant.SMALL);
			states[1] = block.defaultBlockState().setValue(BNVBlockProperties.TRIPPLE_PLANT, TripplePlant.BOTTOM);
			states[2] = block.defaultBlockState().setValue(BNVBlockProperties.TRIPPLE_PLANT, TripplePlant.TOP);
		}
		
		byte l = (byte) MHelper.randRange(1, 3, random);
		if (l == 1) {
			BlocksHelper.setWithoutUpdate(level, center, states[0]);
			return true;
		}
		
		MutableBlockPos pos = center.mutable();
		for (byte i = 0; i < l; i++) {
			BlocksHelper.setWithoutUpdate(level, pos, i == 0 ? states[1] : states[2]);
			pos.setY(pos.getY() + 1);
			if (!level.getBlockState(pos).isAir()) {
				if (i == 0) {
					pos.setY(pos.getY() - 1);
					BlocksHelper.setWithoutUpdate(level, pos, states[0]);
				}
				return true;
			}
		}
		
		return true;
	}
}
