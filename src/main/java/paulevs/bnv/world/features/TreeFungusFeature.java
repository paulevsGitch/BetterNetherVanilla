package paulevs.bnv.world.features;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;
import paulevs.bnv.blocks.BNVBlockProperties;
import paulevs.bnv.blocks.BNVBlockProperties.TripplePlant;
import ru.bclib.noise.OpenSimplexNoise;
import ru.bclib.util.BlocksHelper;
import ru.bclib.util.MHelper;
import ru.bclib.world.features.DefaultFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class TreeFungusFeature extends DefaultFeature {
	private static final OpenSimplexNoise POINTS_NOISE = new OpenSimplexNoise(0);
	private static final BlockState AIR = Blocks.AIR.defaultBlockState();
	private static final Function<BlockState, Boolean> REPLACE = state -> state.isAir() ||
		state.getMaterial().isReplaceable() ||
		state.getMaterial().equals(Material.PLANT) ||
		state.getMaterial().equals(Material.MOSS);
	
	private final BlockState[] mossStates = new BlockState[3];
	private final Block light;
	private final Block moss;
	private final Block stem;
	private final Block bark;
	private final Block cap;
	
	private final float radius;
	private final float height;
	
	public TreeFungusFeature(Block stem, Block bark, Block cap, Block light, Block moss, float radius, float height) {
		this.light = light;
		this.moss = moss;
		this.stem = stem;
		this.bark = bark;
		this.cap = cap;
		
		this.radius = radius;
		this.height = height;
	}
	
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos center = context.origin();
		Random random = context.random();
		
		if (!canGrow(level.getBlockState(center.below()))) {
			return false;
		}
		
		final BlockState light = this.light.defaultBlockState();
		final BlockState stem = this.stem.defaultBlockState();
		final BlockState bark = this.bark.defaultBlockState();
		final BlockState cap = this.cap.defaultBlockState();
		
		if (mossStates[0] == null) {
			mossStates[0] = this.moss.defaultBlockState().setValue(BNVBlockProperties.TRIPPLE_PLANT, TripplePlant.SMALL);
			mossStates[1] = this.moss.defaultBlockState().setValue(BNVBlockProperties.TRIPPLE_PLANT, TripplePlant.TOP);
			mossStates[2] = this.moss.defaultBlockState().setValue(BNVBlockProperties.TRIPPLE_PLANT, TripplePlant.BOTTOM);
		}
		
		MutableBlockPos pos = center.mutable();
		byte h = (byte) MHelper.randRange(7, 16, random);
		if (!growStem(stem, level, pos, h)) {
			return true;
		}
		BlockPos top = pos.immutable();
		growRoots(stem, bark, level, center, pos, h * 0.75F, random);
		growCap(cap, light, level, top, pos, h * radius, h * height, 0.7F, random);
		growBranches(stem, bark, level, top, pos, h * 0.4F, random);
		growLights(light, level, top.below(), pos, MHelper.max(Mth.sqrt(h * radius) * 0.3F, 1.5F), random);
		
		return true;
	}
	
	private void growRoots(BlockState stem, BlockState bark, WorldGenLevel level, BlockPos center, MutableBlockPos pos, float maxLength, Random random) {
		byte[] indexes = new byte[] {2, 2, 3, 4};
		shuffle(indexes, random);
		for (byte i = 0; i < 4; i++) {
			Direction dir = BlocksHelper.HORIZONTAL[i];
			Direction dir2 = BlocksHelper.HORIZONTAL[(i + 1) & 3];
			byte length = (byte) ((indexes[i] + MHelper.randRange(-0.2F, 0.2F, random)) / 4F * maxLength);
			pos.set(center).move(dir).setY(center.getY() - 1);
			verticalLine(stem, bark, level, pos, length, (byte) 1);
			
			if (random.nextInt(4) > 0) {
				length = (byte) MHelper.min(length - 1, MHelper.randRange(3, 4, random));
				pos.set(center).move(dir, 2).move(dir2).setY(center.getY() - 2);
				verticalLine(stem, bark, level, pos, length, (byte) 1);
			}
			
			if (random.nextInt(4) > 0) {
				length = (byte) MHelper.min(length - 1, MHelper.randRange(3, 4, random));
				pos.set(center).move(dir, 2).move(dir2, -1).setY(center.getY() - 2);
				verticalLine(stem, bark, level, pos, length, (byte) 1);
			}
		}
	}
	
	private void verticalLine(BlockState state, BlockState end, WorldGenLevel level, MutableBlockPos pos, byte length, byte dir) {
		byte last = (byte) (length - 1);
		for (byte j = 0; j < length; j++) {
			if (canReplace(level.getBlockState(pos))) {
				BlocksHelper.setWithoutUpdate(level, pos, j == last ? end : state);
			}
			pos.setY(pos.getY() + dir);
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
	
	private void growCap(BlockState cap, BlockState light, WorldGenLevel level, BlockPos center, MutableBlockPos pos, float radius, float height, float curvature, Random random) {
		float sqrt = Mth.sqrt(radius);
		byte minXZ = (byte) Mth.floor(-sqrt);
		byte maxXZ = (byte) Mth.floor(sqrt + 2);
		
		sqrt = Mth.sqrt(height);
		byte minY = (byte) Mth.floor(-sqrt);
		byte maxY = (byte) Mth.floor(sqrt + 2);
		
		float aspect = radius / height;
		float angle = random.nextFloat() * (float) Math.PI * 2;
		
		for (byte x = minXZ; x < maxXZ; x++) {
			pos.setX(x + center.getX());
			for (byte z = minXZ; z < maxXZ; z++) {
				pos.setZ(z + center.getZ());
				float px = x * x;
				float pz = z * z;
				float distance = MHelper.length(x, z) * curvature;
				float noise = (float) Math.sin(Math.atan2(x, z) + angle) * distance * 0.5F + distance;
				for (byte y = minY; y < maxY; y++) {
					float py = y * aspect + noise;
					if (py >= 0) {
						py *= py;
						if (px + pz + py <= radius) {
							pos.setY(y + center.getY());
							if (canReplace(level.getBlockState(pos))) {
								if (POINTS_NOISE.eval(pos.getX() * 0.5, pos.getY() * 0.5, pos.getZ() * 0.5) > 0.5F && random.nextInt(4) == 0) {
									BlocksHelper.setWithoutUpdate(level, pos, light);
								}
								else {
									BlocksHelper.setWithoutUpdate(level, pos, cap);
									placeMoss(level, pos, random);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void growBranches(BlockState stem, BlockState bark, WorldGenLevel level, BlockPos center, MutableBlockPos pos, float maxLength, Random random) {
		byte[] indexes = new byte[] {2, 2, 3, 4};
		shuffle(indexes, random);
		for (byte i = 0; i < 4; i++) {
			Direction dir = BlocksHelper.HORIZONTAL[i];
			Direction dir2 = BlocksHelper.HORIZONTAL[(i + 1) & 3];
			byte length = (byte) ((indexes[i] + MHelper.randRange(-0.2F, 0.2F, random)) / 4F * maxLength);
			pos.set(center).move(dir).setY(center.getY() - 1);
			verticalLine(stem, bark, level, pos, length, (byte) -1);
			
			if (random.nextInt(4) > 0) {
				length = (byte) MHelper.min(length - 1, MHelper.randRange(3, 4, random));
				pos.set(center).move(dir, 2).move(dir2).setY(center.getY() - 2);
				verticalLine(stem, bark, level, pos, length, (byte) -1);
			}
			
			if (random.nextInt(4) > 0) {
				length = (byte) MHelper.min(length - 1, MHelper.randRange(3, 4, random));
				pos.set(center).move(dir, 2).move(dir2, -1).setY(center.getY() - 2);
				verticalLine(stem, bark, level, pos, length, (byte) -1);
			}
		}
	}
	
	private void growLights(BlockState light, WorldGenLevel level, BlockPos center, MutableBlockPos pos, float radius, Random random) {
		byte minXZ = (byte) Mth.floor(-radius);
		byte maxXZ = (byte) Mth.floor(radius + 2);
		radius *= radius;
		
		for (int x = minXZ; x < maxXZ; x++) {
			float x2 = x * x;
			for (int z = minXZ; z < maxXZ; z++) {
				float z2 = z * z;
				if (x2 + z2 < radius) {
					pos.set(center.getX() + x, center.getY(), center.getZ() + z);
					verticalLine(light, light, level, pos, (byte) MHelper.randRange(1, 3, random), (byte) -1);
					if (level.getBlockState(pos.above()).equals(light)) {
						BlockState state = level.getBlockState(pos);
						while (state.is(mossStates[0].getBlock())) {
							BlocksHelper.setWithoutUpdate(level, pos, AIR);
							state = level.getBlockState(pos.setY(pos.getY() - 1));
						}
					}
				}
			}
		}
	}
	
	private void placeMoss(WorldGenLevel level, MutableBlockPos pos, Random random) {
		byte h = (byte) MHelper.randRange(1, 2, random);
		for (byte i = 0; i < h; i++) {
			pos.setY(pos.getY() - 1);
			if (!canReplace(level.getBlockState(pos))) {
				return;
			}
			BlocksHelper.setWithoutUpdate(level, pos, h == 1 ? mossStates[0] : mossStates[i + 1]);
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
