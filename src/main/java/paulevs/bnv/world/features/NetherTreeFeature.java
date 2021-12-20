package paulevs.bnv.world.features;

import com.mojang.math.Vector3f;
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
import net.minecraft.world.level.material.Material;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class NetherTreeFeature extends DefaultFeature {
	private static final Function<BlockState, Boolean> REPLACE = state -> state.isAir() ||
		state.getMaterial().isReplaceable() ||
		state.getMaterial().equals(Material.PLANT);
	
	private final Block stem;
	private final Block bark;
	private final Block cap;
	
	public NetherTreeFeature(Block stem, Block bark, Block cap) {
		this.stem = stem;
		this.bark = bark;
		this.cap = cap;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		Random random = context.random();
		BlockPos center = context.origin();
		
		System.out.println("place " + center + " " + level.getBlockState(center.below()));
		
		if (!canGrow(level.getBlockState(center.below()))) {
			return false;
		}
		
		final BlockState stem = this.stem.defaultBlockState();
		final BlockState bark = this.bark.defaultBlockState();
		final BlockState cap = this.cap.defaultBlockState();
		
		MutableBlockPos pos = center.mutable();
		byte h = (byte) MHelper.randRange(8, 16, random);
		if (!growStem(stem, level, pos, h)) {
			return true;
		}
		//BlockPos top = pos.immutable();
		growRoots(stem, bark, level, center, pos, h, random);
		//growCap(cap, level, top, pos, h);
		
		return true;
	}
	
	private void growRoots(BlockState stem, BlockState bark, WorldGenLevel level, BlockPos center, MutableBlockPos pos, byte h, Random random) {
		byte maxLength = (byte) (h >> 1);
		byte[] indexes = new byte[] {2, 2, 3, 4};
		shuffle(indexes, random);
		for (byte i = 0; i < 4; i++) {
			Direction dir = BlocksHelper.HORIZONTAL[i];
			Direction dir2 = BlocksHelper.HORIZONTAL[(i + 1) & 3];
			byte length = (byte) ((indexes[i] + MHelper.randRange(-0.2F, 0.2F, random)) / 4F * maxLength);
			pos.set(center).move(dir).setY(center.getY() - 1);
			lineUp(stem, bark, level, pos, length);
			
			if (random.nextInt(4) > 0) {
				length = (byte) MHelper.min(length - 1, MHelper.randRange(3, 4, random));
				pos.set(center).move(dir, 2).move(dir2).setY(center.getY() - 2);
				lineUp(stem, bark, level, pos, length);
			}
			
			if (random.nextInt(4) > 0) {
				length = (byte) MHelper.min(length - 1, MHelper.randRange(3, 4, random));
				pos.set(center).move(dir, 2).move(dir2, -1).setY(center.getY() - 2);
				lineUp(stem, bark, level, pos, length);
			}
		}
	}
	
	private void lineUp(BlockState state, BlockState end, WorldGenLevel level, MutableBlockPos pos, byte length) {
		byte last = (byte) (length - 1);
		for (byte j = 0; j < length; j++) {
			if (canReplace(level.getBlockState(pos))) {
				BlocksHelper.setWithoutUpdate(level, pos, j == last ? end : state);
			}
			pos.setY(pos.getY() + 1);
		}
	}
	
	private boolean growStem(BlockState stem, WorldGenLevel level, MutableBlockPos pos, byte h) {
		for (int i = 0; i < h; i++) {
			if (!canReplace(level.getBlockState(pos))) {
				return false;
			}
			BlocksHelper.setWithoutUpdate(level, pos, stem);
			pos.setY(pos.getY() + 1);
		}
		return true;
	}
	
	private void growCap(BlockState cap, WorldGenLevel level, BlockPos center, MutableBlockPos pos, byte h) {
		float radiusH = h * 0.5F;
		float radiusH2 = radiusH * 2;
		float radiusV = radiusH * 0.5F;
		float r2 = radiusH * radiusH;
		float r3 = radiusH2 * radiusH2;
		
		byte minXZ = (byte) Mth.floor(-radiusH);
		byte maxXZ = (byte) Mth.floor(radiusH + 2);
		byte maxY = (byte) Mth.floor(radiusV + 2);
		
		/*for (byte x = minXZ; x < maxXZ; x++) {
			pos.setX(x + center.getX());
			for (byte z = minXZ; z < maxXZ; z++) {
				// noise here //
				float x2 = x * x;
				float z2 = z * z;
				/for (byte y = -2; y < maxY; y++) {
					float y3 = y + radiusH + 1;
					float y2 = y - 2;
					y2 = y2 * y2 * 4;
					y3 = y3 * y3;
					pos.setY(y + center.getY());
					if (x2 + y2 + z2 <= r2 && x2 + y3 + z2 >= r2) {
						pos.setZ(z + center.getZ());
						if (canReplace(level.getBlockState(pos))) {
							BlocksHelper.setWithoutUpdate(level, pos, cap);
						}
					}
				}
			}
		}*/
		
		for (byte x = minXZ; x < maxXZ; x++) {
			for (byte z = minXZ; z < maxXZ; z++) {
				for (byte y = -2; y < maxY; y++) {
				
				}
			}
		}
	}
	
	private boolean canGrow(BlockState state) {
		return state.is(BlockTags.NYLIUM);
	}
	
	private boolean canReplace(BlockState state) {
		return state.isAir() || state.getMaterial().isReplaceable() || state.getMaterial().equals(Material.PLANT);
	}
	
	private List<Vector3f> smoothSpline(List<Vector3f> spline, int side) {
		final int size = spline.size();
		List<Vector3f> result = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			int count = 0;
			float x = 0;
			float z = 0;
			for (int j = -side; j <= side; j++) {
				int index = i + j;
				if (index >= 0 && index < size) {
					Vector3f pos = spline.get(index);
					x += pos.x();
					z += pos.z();
					count++;
				}
			}
			Vector3f pos = spline.get(i);
			result.add(new Vector3f(x / count, pos.y(), z / count));
		}
		return result;
	}
	
	private static void shuffle(byte[] array, Random random) {
		for (int i = 0; i < array.length; i++) {
			int i2 = random.nextInt(array.length);
			byte element = array[i];
			array[i] = array[i2];
			array[i2] = element;
		}
	}
}
