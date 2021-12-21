package paulevs.bnv.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import paulevs.bnv.blocks.BNVBlockProperties.TripplePlant;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.client.models.BasePatterns;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.PatternsHelper;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.RenderLayerProvider;

import java.util.Map;
import java.util.Optional;

public class MouldBlock extends BaseBlockNotFull implements RenderLayerProvider {
	public static final EnumProperty<TripplePlant> SHAPE = BNVBlockProperties.TRIPPLE_PLANT;
	private static final Map<TripplePlant, VoxelShape> SHAPE_MAP = Maps.newEnumMap(TripplePlant.class);
	
	public MouldBlock() {
		super(FabricBlockSettings.copyOf(Blocks.MOSS_BLOCK).noOcclusion());
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return SHAPE_MAP.get(state.getValue(SHAPE));
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(SHAPE);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockState down = level.getBlockState(pos.below());
		return down.is(this) || down.is(BlockTags.NYLIUM);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		if (!canSurvive(state, level, pos)) {
			return Blocks.AIR.defaultBlockState();
		}
		if (level.getBlockState(pos.below()).is(this)) {
			return state.setValue(SHAPE, TripplePlant.TOP);
		}
		if (level.getBlockState(pos.above()).is(this)) {
			return state.setValue(SHAPE, TripplePlant.BOTTOM);
		}
		return state.setValue(SHAPE, TripplePlant.SMALL);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		BlockState down = level.getBlockState(pos.below());
		if (down.is(this) || down.is(BlockTags.NYLIUM)) {
			return defaultBlockState().setValue(SHAPE, down.is(BlockTags.NYLIUM) ? TripplePlant.SMALL : TripplePlant.TOP);
		}
		return null;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		Map<String, String> replacements = Maps.newHashMap();
		if (blockState.getValue(SHAPE) == TripplePlant.BOTTOM) {
			replacements.put("%cover%", stateId.getNamespace() + ":block/" + stateId.getPath() + "_cover");
			replacements.put("%stem%", "minecraft:block/" + stateId.getPath().replace("_mould", "_stem"));
			Optional<String> pattern = PatternsHelper.createJson(BNVPatterns.MOULD_BOTTOM, replacements);
			return ModelsHelper.fromPattern(pattern);
		}
		else if (blockState.getValue(SHAPE) == TripplePlant.SMALL) {
			replacements.put("%side%", stateId.getNamespace() + ":block/" + stateId.getPath() + "_small_side");
			replacements.put("%top%", stateId.getNamespace() + ":block/" + stateId.getPath() + "_small_top");
			Optional<String> pattern = PatternsHelper.createJson(BNVPatterns.MOULD_SMALL, replacements);
			return ModelsHelper.fromPattern(pattern);
		}
		replacements.put("%modid%", stateId.getNamespace());
		replacements.put("%texture%", stateId.getPath());
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_BASE, replacements);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		Map<String, String> replacements = Maps.newHashMap();
		replacements.put("%side%", itemID.getNamespace() + ":block/" + itemID.getPath() + "_small_side");
		replacements.put("%top%", itemID.getNamespace() + ":block/" + itemID.getPath() + "_small_top");
		Optional<String> pattern = PatternsHelper.createJson(BNVPatterns.MOULD_SMALL, replacements);
		return ModelsHelper.fromPattern(pattern);
	}
	
	static {
		SHAPE_MAP.put(TripplePlant.BOTTOM, Shapes.or(box(4, 0, 4, 12, 16, 12), box(0, 8, 0, 16, 16, 16)));
		SHAPE_MAP.put(TripplePlant.TOP, Shapes.block());
		SHAPE_MAP.put(TripplePlant.SMALL, box(3, 0, 3, 13, 12, 13));
	}
}
