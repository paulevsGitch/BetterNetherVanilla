package paulevs.bnv.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FungusBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnv.registries.NetherFeatures;

import java.util.Random;

@Mixin(FungusBlock.class)
public class FungusBlockMixin {
	@Inject(method = "performBonemeal", at = @At("HEAD"), cancellable = true)
	private void bnv_performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state, CallbackInfo info) {
		FungusBlock fungus = FungusBlock.class.cast(this);
		if (fungus == Blocks.CRIMSON_FUNGUS) {
			NetherFeatures.CRIMSON_HUGE_FUNGUS.getPlacedFeature().place(level, level.getChunkSource().getGenerator(), random, pos);
			info.cancel();
		}
		else if (fungus == Blocks.WARPED_FUNGUS) {
			NetherFeatures.WARPED_HUGE_FUNGUS.getPlacedFeature().place(level, level.getChunkSource().getGenerator(), random, pos);
			info.cancel();
		}
	}
	
	@Inject(method = "isValidBonemealTarget", at = @At("HEAD"), cancellable = true)
	private void bnv_isValidBonemealTarget(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, boolean bl, CallbackInfoReturnable<Boolean> info) {
		FungusBlock fungus = FungusBlock.class.cast(this);
		if (fungus == Blocks.CRIMSON_FUNGUS || fungus == Blocks.WARPED_FUNGUS) {
			info.setReturnValue(blockGetter.getBlockState(blockPos.below()).is(BlockTags.NYLIUM));
		}
	}
}
