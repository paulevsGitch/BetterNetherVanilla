package paulevs.bnv.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import paulevs.bnv.blocks.BNVBlockProperties;
import paulevs.bnv.blocks.BNVBlockProperties.TripplePlant;
import paulevs.bnv.blocks.MouldBlock;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.Random;

public class MouldClusterFeature extends DefaultFeature {
	private final BlockState[] states = new BlockState[3];
	private Block block;
	
	public MouldClusterFeature(Block block) {
		this.block = block;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos center = context.origin();
		Random random = context.random();
		
		if (states[0] == null) {
			states[0] = block.defaultBlockState().setValue(BNVBlockProperties.TRIPPLE_PLANT, TripplePlant.SMALL);
			states[1] = block.defaultBlockState().setValue(BNVBlockProperties.TRIPPLE_PLANT, TripplePlant.BOTTOM);
			states[2] = block.defaultBlockState().setValue(BNVBlockProperties.TRIPPLE_PLANT, TripplePlant.TOP);
		}
		
		MutableBlockPos pos = new MutableBlockPos();
		byte radius = (byte) MHelper.randRange(4, 7, random);
		byte count = (byte) (radius * MHelper.randRange(0.8F, 1.5F, random));
		for (byte i = 0; i < count; i++) {
			int dx = Mth.clamp(Mth.floor((float) random.nextGaussian() * 2 * radius + 0.5F), -radius, radius);
			int dz = Mth.clamp(Mth.floor((float) random.nextGaussian() * 2 * radius + 0.5F), -radius, radius);
			pos.set(center.getX() + dx, center.getY() + 5, center.getZ() + dz);
			for (byte j = 0; j < 10; j++) {
				if (level.getBlockState(pos).is(BlockTags.NYLIUM)) {
					pos.setY(pos.getY() + 1);
					if (level.getBlockState(pos).isAir()) {
						for (Direction dir: BlocksHelper.HORIZONTAL) {
							if (level.getBlockState(pos.relative(dir)).getBlock() instanceof MouldBlock) {
								break;
							}
						}
						place(level, pos, random);
						break;
					}
				}
				pos.setY(pos.getY() - 1);
			}
		}
		
		return true;
	}
	
	private void place(WorldGenLevel level, MutableBlockPos pos, Random random) {
		byte l = (byte) MHelper.randRange(1, 3, random);
		if (l == 1) {
			BlocksHelper.setWithoutUpdate(level, pos, states[0]);
			return;
		}
		for (byte i = 0; i < l; i++) {
			BlocksHelper.setWithoutUpdate(level, pos, i == 0 ? states[1] : states[2]);
			pos.setY(pos.getY() + 1);
			if (!level.getBlockState(pos).isAir()) {
				if (i == 0) {
					pos.setY(pos.getY() - 1);
					BlocksHelper.setWithoutUpdate(level, pos, states[0]);
				}
				return;
			}
		}
	}
}
