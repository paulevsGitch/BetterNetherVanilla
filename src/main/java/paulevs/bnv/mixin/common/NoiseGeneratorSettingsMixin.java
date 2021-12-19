package paulevs.bnv.mixin.common;

import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(NoiseGeneratorSettings.class)
public class NoiseGeneratorSettingsMixin {
	@ModifyArg(
		method = "nether",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/levelgen/NoiseSettings;create(IILnet/minecraft/world/level/levelgen/NoiseSamplingSettings;Lnet/minecraft/world/level/levelgen/NoiseSlider;Lnet/minecraft/world/level/levelgen/NoiseSlider;IIZZZLnet/minecraft/world/level/biome/TerrainShaper;)Lnet/minecraft/world/level/levelgen/NoiseSettings;"
		),
		index = 1
	)
	private static int bnv_changeNetherHeight(int height) {
		return 256;
	}
}
