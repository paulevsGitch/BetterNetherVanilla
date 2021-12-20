package paulevs.bnv.blocks;

import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.mininglevel.v1.FabricMineableTags;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag.Named;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import paulevs.bnv.blocks.NetherBlockProperties.TripplePlant;
import ru.bclib.api.TagAPI;
import ru.bclib.blocks.BaseBlockNotFull;
import ru.bclib.client.models.BasePatterns;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.PatternsHelper;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.RenderLayerProvider;
import ru.bclib.interfaces.TagProvider;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class WeepingMoss extends BaseBlockNotFull implements RenderLayerProvider, TagProvider {
	public static final EnumProperty<TripplePlant> SHAPE = NetherBlockProperties.TRIPPLE_PLANT;
	
	public WeepingMoss() {
		super(FabricBlockSettings.of(Material.PLANT).sound(SoundType.MOSS).noOcclusion().noCollission().instabreak());
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(SHAPE);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockPos above = pos.above();
		BlockState up = level.getBlockState(above);
		if (up.is(this) && !level.getBlockState(above.above()).is(this)) {
			return true;
		}
		return up.getMaterial().isSolidBlocking() && up.isFaceSturdy(level, above, Direction.UP);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
		if (!canSurvive(state, level, pos)) {
			return Blocks.AIR.defaultBlockState();
		}
		if (level.getBlockState(pos.above()).is(this)) {
			return state.setValue(SHAPE, TripplePlant.BOTTOM);
		}
		if (level.getBlockState(pos.below()).is(this)) {
			return state.setValue(SHAPE, TripplePlant.TOP);
		}
		return state.setValue(SHAPE, TripplePlant.SMALL);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos pos = context.getClickedPos();
		Level level = context.getLevel();
		BlockPos above = pos.above();
		BlockState up = level.getBlockState(above);
		if (up.is(this) && !level.getBlockState(above.above()).is(this)) {
			return defaultBlockState().setValue(SHAPE, TripplePlant.BOTTOM);
		}
		else if (up.getMaterial().isSolidBlocking() && up.isFaceSturdy(level, above, Direction.UP)) {
			return defaultBlockState().setValue(SHAPE, TripplePlant.SMALL);
		}
		return null;
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	@Override
	public void addTags(List<Named<Block>> blockTags, List<Named<Item>> itemTags) {
		blockTags.add(FabricMineableTags.SHEARS_MINEABLE);
		blockTags.add(TagAPI.MINEABLE_HOE);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation itemID) {
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.ITEM_GENERATED, itemID);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public UnbakedModel getModelVariant(ResourceLocation stateId, BlockState blockState, Map<ResourceLocation, UnbakedModel> modelCache) {
		Map<String, String> textures = Maps.newHashMap();
		String suffix = blockState.getValue(SHAPE).getSerializedName();
		textures.put("%texture%", stateId.getNamespace() + ":/block/" + stateId.getPath() + "_" + suffix);
		Optional<String> pattern = PatternsHelper.createJson(NetherPatterns.CUBE_WALLS, textures);
		return ModelsHelper.fromPattern(pattern);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.getOptionalParameter(LootContextParams.TOOL);
		if (tool != null && (tool.is(FabricToolTags.SHEARS) || tool.is(FabricToolTags.HOES))) {
			return Collections.singletonList(new ItemStack(this));
		}
		return Collections.EMPTY_LIST;
	}
}
