package com.lambda.nopause.mixin;

import com.lambda.nopause.NoPause;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "onWindowFocusChanged", at = @At("HEAD"), cancellable = true)
    private void onWindowFocusChanged(boolean focused, CallbackInfo ci) {
        // Only cancel when the mod is enabled and losing focus
        if (NoPause.isEnabled() && !focused) {
            ci.cancel();
        }
    }
} 