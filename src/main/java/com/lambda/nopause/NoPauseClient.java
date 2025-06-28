package com.lambda.nopause;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class NoPauseClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register the keybinding check and animation logic
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            NoPause.checkKeybinding();

            // Handle HUD animation state
            if (NoPause.isHudAnimationActive()) {
                long timeSinceToggle = System.currentTimeMillis() - NoPause.getLastToggleTime();
                if (timeSinceToggle > NoPause.FADE_DURATION) {
                    NoPause.setHudAnimationActive(false);
                }
            }
        });

        // Register the HUD renderer
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            NoPauseHud.render(drawContext);
        });
    }
}