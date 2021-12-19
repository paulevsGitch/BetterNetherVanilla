package paulevs.bnv.mixin.client;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionSpecialEffects.NetherEffects.class)
public class NetherEffectsMixin {
	@Inject(method = "isFoggyAt", at = @At("HEAD"), cancellable = true)
	private void bnv_isFoggyAt(int i, int j, CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(false);
	}
}
