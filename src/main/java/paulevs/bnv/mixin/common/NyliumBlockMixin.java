package paulevs.bnv.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NyliumBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnv.blocks.NyliumOverlay;
import ru.bclib.util.BlocksHelper;

import java.util.Random;

@Mixin(NyliumBlock.class)
public class NyliumBlockMixin extends Block {
	public NyliumBlockMixin(Properties properties) {
		super(properties);
	}
	
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Block replacement = NyliumOverlay.getMappedBlock(this);
		if (replacement != null) {
			return replacement.getStateForPlacement(context);
		}
		return super.getStateForPlacement(context);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction direction, BlockState sideState, LevelAccessor level, BlockPos blockPos, BlockPos blockPos2) {
		Block replacement = NyliumOverlay.getMappedBlock(this);
		if (replacement != null) {
			return replacement.updateShape(replacement.defaultBlockState(), direction, sideState, level, blockPos, blockPos2);
		}
		return super.updateShape(state, direction, sideState, level, blockPos, blockPos2);
	}
	
	@Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
	private void bnv_randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random, CallbackInfo info) {
		Block replacement = NyliumOverlay.getMappedBlock(this);
		if (replacement != null) {
			BlocksHelper.setWithoutUpdate(serverLevel, blockPos, replacement.defaultBlockState());
			info.cancel();
		}
	}
}
