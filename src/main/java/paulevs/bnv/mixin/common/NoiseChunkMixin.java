package paulevs.bnv.mixin.common;

import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseChunk.NoiseFiller;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSampler;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnv.interfaces.TargetChecker;

@Mixin(NoiseChunk.class)
public class NoiseChunkMixin implements TargetChecker {
	private boolean bnv_isNetherGenerator;
	
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void bnv_onNoiseChunkInit(int i, int j, int k, NoiseSampler noiseSampler, int l, int m, NoiseFiller noiseFiller, NoiseGeneratorSettings noiseGeneratorSettings, Aquifer.FluidPicker fluidPicker, Blender blender, CallbackInfo info) {
		bnv_isNetherGenerator = noiseGeneratorSettings.stable(NoiseGeneratorSettings.NETHER);
	}
	
	@Override
	public boolean isTarget() {
		return bnv_isNetherGenerator;
	}
}
