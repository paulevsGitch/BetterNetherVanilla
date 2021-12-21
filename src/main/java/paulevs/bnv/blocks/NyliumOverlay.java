package paulevs.bnv.blocks;

import com.google.common.collect.Maps;
import com.mojang.math.Transformation;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag.Named;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NyliumBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;
import paulevs.bnv.BNV;
import ru.bclib.api.TagAPI;
import ru.bclib.client.models.BasePatterns;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.ModelsHelper.MultiPartBuilder;
import ru.bclib.client.models.PatternsHelper;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.BlockModelProvider;
import ru.bclib.interfaces.RenderLayerProvider;
import ru.bclib.interfaces.TagProvider;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NyliumOverlay extends NyliumBlock implements BlockModelProvider, RenderLayerProvider, TagProvider {
	public static final BooleanProperty[] DIRECTION_BOOLEAN = BNVBlockProperties.DIRECTION_BOOLEAN;
	private static final Map<Block, Block> MAPPED_BLOCKS = Maps.newHashMap();
	private static final BlockPos[] OFFSETS = new BlockPos[] {
		BlockPos.ZERO.north(),
		BlockPos.ZERO.east(),
		BlockPos.ZERO.south(),
		BlockPos.ZERO.west()
	};
	private static int totalPriority = 0;
	private final int priority;
	private final Block source;
	
	public NyliumOverlay(Block source) {
		super(FabricBlockSettings.copyOf(source).dropsLike(source));
		BlockState state = getStateDefinition().any();
		for (BooleanProperty property: DIRECTION_BOOLEAN) {
			state = state.setValue(property, false);
		}
		registerDefaultState(state);
		priority = totalPriority++;
		MAPPED_BLOCKS.put(source, this);
		this.source = source;
	}
	
	@Override
	public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
		return new ItemStack(source);
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(DIRECTION_BOOLEAN);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction direction, BlockState sideState, LevelAccessor level, BlockPos blockPos, BlockPos blockPos2) {
		return getState(level, blockPos, state);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return getState(context.getLevel(), context.getClickedPos(), null);
	}
	
	public BlockState getState(LevelAccessor level, BlockPos blockPos, BlockState state) {
		if (state == null) {
			state = defaultBlockState();
		}
		MutableBlockPos pos = new MutableBlockPos();
		for (byte i = 0; i < DIRECTION_BOOLEAN.length; i++) {
			pos.set(blockPos).move(OFFSETS[i]);
			boolean stateValue = canOverlay(level, pos, level.getBlockState(pos)) && isAirlike(level.getBlockState(pos.move(Direction.UP)));
			state = state.setValue(DIRECTION_BOOLEAN[i], stateValue);
		}
		return state;
	}
	
	private boolean canOverlay(LevelAccessor level, BlockPos blockPos, BlockState state) {
		if (state.is(this) || !state.getMaterial().isSolidBlocking()) {
			return false;
		}
		if (state.getBlock() instanceof NyliumOverlay) {
			NyliumOverlay overlay = NyliumOverlay.class.cast(state.getBlock());
			return priority < overlay.priority;
		}
		return state.isFaceSturdy(level, blockPos, Direction.UP);
	}
	
	private boolean isAirlike(BlockState state) {
		return state.isAir() || state.getMaterial().isReplaceable() || !state.getMaterial().blocksMotion();
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	@Override
	public void addTags(List<Named<Block>> blockTags, List<Named<Item>> itemTags) {
		TagAPI.addTags(
			this,
			BlockTags.NYLIUM,
			BlockTags.MUSHROOM_GROW_BLOCK,
			BlockTags.MINEABLE_WITH_PICKAXE,
			BlockTags.ENDERMAN_HOLDABLE
		);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		MultiPartBuilder model = MultiPartBuilder.create(stateDefinition);
		
		String sourceName = stateId.getPath().replace("_overlay", "");
		Map<String, String> replacements = Maps.newHashMap();
		replacements.put("%modid%", "minecraft");
		replacements.put("%texture%_bottom", "netherrack");
		replacements.put("%texture%_side", sourceName + "_side");
		replacements.put("%texture%_top", sourceName);
		
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_BOTTOM_TOP, replacements);
		UnbakedModel unbakedModel = ModelsHelper.fromPattern(pattern);
		ResourceLocation modelPath = BNV.makeID(stateId.getPath());
		modelCache.put(modelPath, unbakedModel);
		
		unbakedModel = ModelsHelper.createRandomTopModel(modelPath);
		modelPath = BNV.makeID(stateId.getPath() + "_rotated");
		modelCache.put(modelPath, unbakedModel);
		
		model.part(modelPath).add();
		replacements.clear();
		
		replacements.put("%texture%", "bnv:block/" + stateId.getPath());
		for (byte i = 0; i < DIRECTION_BOOLEAN.length; i++) {
			final BooleanProperty property = DIRECTION_BOOLEAN[i];
			final BlockPos offset = OFFSETS[i];
			
			byte x1 = (byte) (offset.getX() < 0 ? 8 : 0);
			byte x2 = (byte) (offset.getX() > 0 ? 8 : 16);
			byte z1 = (byte) (offset.getZ() < 0 ? 8 : 0);
			byte z2 = (byte) (offset.getZ() > 0 ? 8 : 16);
			replacements.put("[ 0, 16, 0 ]", String.format("[ %d, 16.01, %d ]", x1, z1));
			replacements.put("[ 16, 16, 16 ]", String.format("[ %d, 16.01%d, %d ]", x2, i & 1, z2));
			
			x1 = (byte) (offset.getX() < 0 ? 0 : offset.getX() > 0 ? 12 : 4);
			x2 = (byte) (offset.getX() < 0 ? 4 : offset.getX() > 0 ? 16 : 12);
			z1 = (byte) (offset.getZ() < 0 ? 0 : offset.getZ() > 0 ? 12 : 4);
			z2 = (byte) (offset.getZ() < 0 ? 4 : offset.getZ() > 0 ? 16 : 12);
			replacements.put("[ 0, 0, 16, 16 ]", String.format("[ %d, %d, %d, %d ]", x1, z1, x2, z2));
			
			modelPath = BNV.makeID(stateId.getPath() + "_plane_" + property.getName());
			pattern = PatternsHelper.createJson(BNVPatterns.UPPER_PLANE, replacements);
			unbakedModel = ModelsHelper.fromPattern(pattern);
			modelCache.put(modelPath, unbakedModel);
			
			Vector3f localOffset = new Vector3f(offset.getX(), 0, offset.getZ());
			Transformation transformation = new Transformation(localOffset, null, null, null);
			model.part(modelPath).setTransformation(transformation).setCondition(state -> state.getValue(property)).add();
		}
		
		return model.build();
	}
	
	@Nullable
	public static Block getMappedBlock(Block source) {
		return MAPPED_BLOCKS.get(source);
	}
}
