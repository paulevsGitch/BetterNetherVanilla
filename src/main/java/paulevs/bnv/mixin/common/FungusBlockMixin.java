package paulevs.bnv.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FungusBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnv.registries.BNVBlocks;
import paulevs.bnv.registries.BNVFeatures;

import java.util.Optional;
import java.util.Random;

@Mixin(FungusBlock.class)
public class FungusBlockMixin {
	@Inject(method = "performBonemeal", at = @At("HEAD"), cancellable = true)
	private void bnv_performBonemeal(ServerLevel level, Random random, BlockPos pos, BlockState state, CallbackInfo info) {
		FungusBlock fungus = FungusBlock.class.cast(this);
		if (fungus == Blocks.CRIMSON_FUNGUS) {
			FeaturePlaceContext context = bnv_getContext(level, random, pos);
			BNVFeatures.CRIMSON_HUGE_FUNGUS.getFeature().place(context);
			info.cancel();
		}
		else if (fungus == Blocks.WARPED_FUNGUS) {
			FeaturePlaceContext context = bnv_getContext(level, random, pos);
			BNVFeatures.WARPED_HUGE_FUNGUS.getFeature().place(context);
			info.cancel();
		}
	}
	
	@Inject(method = "isValidBonemealTarget", at = @At("HEAD"), cancellable = true)
	private void bnv_isValidBonemealTarget(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, boolean bl, CallbackInfoReturnable<Boolean> info) {
		FungusBlock fungus = FungusBlock.class.cast(this);
		BlockState below = blockGetter.getBlockState(blockPos.below());
		if (fungus == Blocks.CRIMSON_FUNGUS && (below.is(Blocks.CRIMSON_NYLIUM) || below.is(BNVBlocks.CRIMSON_NYLIUM_OVERLAY))) {
			info.setReturnValue(true);
		}
		else if (fungus == Blocks.WARPED_FUNGUS && (below.is(Blocks.WARPED_NYLIUM) || below.is(BNVBlocks.WARPED_NYLIUM_OVERLAY))) {
			info.setReturnValue(true);
		}
	}
	
	private FeaturePlaceContext bnv_getContext(ServerLevel level, Random random, BlockPos pos) {
		return new FeaturePlaceContext(
			Optional.empty(),
			level,
			level.getChunkSource().getGenerator(),
			random,
			pos,
			NoneFeatureConfiguration.INSTANCE
		);
	}
}
