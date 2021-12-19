package paulevs.bnv.world.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import paulevs.bnv.blocks.NyliumOverlay;
import ru.bclib.world.features.DefaultFeature;

public class OverlayFixerFeature extends DefaultFeature {
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext) {
		WorldGenLevel level = featurePlaceContext.level();
		BlockPos blockPos = featurePlaceContext.origin();
		ChunkAccess chunk = level.getChunk(blockPos);
		final int minY = level.getMinBuildHeight();
		final int maxY = featurePlaceContext.chunkGenerator().getGenDepth();
		MutableBlockPos pos = new MutableBlockPos();
		MutableBlockPos posWorld = new MutableBlockPos();
		int minX = chunk.getPos().getMinBlockX();
		int minZ = chunk.getPos().getMinBlockZ();
		for (int y = minY; y < maxY; y++) {
			pos.setY(y);
			for (int x = 0; x < 16; x++) {
				pos.setX(x);
				for (int z = 0; z < 16; z++) {
					pos.setZ(z);
					BlockState state = chunk.getBlockState(pos);
					if (state.getBlock() instanceof NyliumOverlay) {
						NyliumOverlay overlay = NyliumOverlay.class.cast(state.getBlock());
						posWorld.set(minX, 0, minZ).move(pos);
						state = overlay.getState(level, posWorld, state);
						chunk.setBlockState(pos, state, false);
					}
				}
			}
		}
		return true;
	}
}
