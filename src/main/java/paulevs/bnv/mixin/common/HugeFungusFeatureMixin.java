package paulevs.bnv.mixin.common;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import net.minecraft.world.level.levelgen.feature.HugeFungusFeature;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HugeFungusFeature.class)
public abstract class HugeFungusFeatureMixin extends Feature<HugeFungusConfiguration> {
	public HugeFungusFeatureMixin(Codec<HugeFungusConfiguration> codec) {
		super(codec);
	}
	
	/*@Inject(method = "place", at = @At("HEAD"), cancellable = true)
	private void bnv_onPlace(FeaturePlaceContext<HugeFungusConfiguration> featurePlaceContext, CallbackInfoReturnable<Boolean> info) {
		HugeFungusConfiguration hugeFungusConfiguration = featurePlaceContext.config();
		
		Feature<NoneFeatureConfiguration> feature = null;
		if (bnv_isCrimson(hugeFungusConfiguration)) {
			feature = (Feature<NoneFeatureConfiguration>) NetherFeatures.CRIMSON_HUGE_FUNGUS.getFeature();
		}
		else if (bnv_isWarped(hugeFungusConfiguration)) {
			feature = (Feature<NoneFeatureConfiguration>) NetherFeatures.WARPED_HUGE_FUNGUS.getFeature();
		}
		
		if (feature == null) {
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
		info.setReturnValue(feature.place(context));
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
	}*/
}
