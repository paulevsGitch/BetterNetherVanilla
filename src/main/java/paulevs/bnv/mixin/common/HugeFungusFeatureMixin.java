package paulevs.bnv.mixin.common;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import net.minecraft.world.level.levelgen.feature.HugeFungusFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnv.registries.NetherFeatures;

@Mixin(HugeFungusFeature.class)
public abstract class HugeFungusFeatureMixin extends Feature<HugeFungusConfiguration> {
	private Feature<NoneFeatureConfiguration> bnv_replacementFeature = null;
	private boolean bnv_featureReplaced = false;
	
	public HugeFungusFeatureMixin(Codec<HugeFungusConfiguration> codec) {
		super(codec);
	}
	
	@Inject(method = "place", at = @At("HEAD"), cancellable = true)
	private void bnv_onPlace(FeaturePlaceContext<HugeFungusConfiguration> featurePlaceContext, CallbackInfoReturnable<Boolean> info) {
		HugeFungusConfiguration hugeFungusConfiguration = featurePlaceContext.config();
		
		if (!bnv_featureReplaced) {
			bnv_featureReplaced = true;
			if (bnv_isCrimson(hugeFungusConfiguration)) {
				bnv_replacementFeature = (Feature<NoneFeatureConfiguration>) NetherFeatures.CRIMSON_HUGE_FUNGUS.getFeature();
			}
			else if (bnv_isWarped(hugeFungusConfiguration)) {
				bnv_replacementFeature = (Feature<NoneFeatureConfiguration>) NetherFeatures.WARPED_HUGE_FUNGUS.getFeature();
			}
		}
		
		if (bnv_replacementFeature == null) {
			return;
		}
		
		FeaturePlaceContext<NoneFeatureConfiguration> context = new FeaturePlaceContext(
			featurePlaceContext.topFeature(),
			featurePlaceContext.level(),
			featurePlaceContext.chunkGenerator(),
			featurePlaceContext.random(),
			featurePlaceContext.origin(),
			featurePlaceContext.config()
		);
		info.setReturnValue(bnv_replacementFeature.place(context));
	}
	
	private boolean bnv_isCrimson(HugeFungusConfiguration configuration) {
		return configuration.validBaseState.is(Blocks.CRIMSON_NYLIUM) &&
			configuration.stemState.is(Blocks.CRIMSON_STEM) &&
			configuration.hatState.is(Blocks.NETHER_WART_BLOCK);
	}
	
	private boolean bnv_isWarped(HugeFungusConfiguration configuration) {
		return configuration.validBaseState.is(Blocks.WARPED_NYLIUM) &&
			configuration.stemState.is(Blocks.WARPED_STEM) &&
			configuration.hatState.is(Blocks.WARPED_WART_BLOCK);
	}
}
