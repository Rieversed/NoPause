package com.lambda.nopause;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NoPauseHud {
    private static final Identifier ICON_ENABLED = Identifier.of("nopause", "textures/enabled.png");
    private static final Identifier ICON_DISABLED = Identifier.of("nopause", "textures/disabled.png");

    public static void render(DrawContext context) {
        // The animation's lifecycle is now controlled by NoPauseClient and the NoPause state
        if (!NoPause.shouldShowHudIndicator() || !NoPause.isHudAnimationActive()) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) {
            return;
        }

        long timeSinceToggle = System.currentTimeMillis() - NoPause.getLastToggleTime();

        // This check is a safeguard, but the primary control is isHudAnimationActive
        if (timeSinceToggle > NoPause.FADE_DURATION) {
            return;
        }

        float alpha = 1.0f - (timeSinceToggle / (float) NoPause.FADE_DURATION);

        int colorAlpha = (int) (alpha * 255);
        int textColor = (colorAlpha << 24) | 0xFFFFFF;

        int x = 10; // X position of the HUD element
        int y = 10; // Y position of the HUD element

        Identifier icon = NoPause.isEnabled() ? ICON_ENABLED : ICON_DISABLED;
        String statusText = NoPause.isEnabled() ? "Enabled" : "Disabled";

        // --- Rendering ---
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Set color with alpha for the icon and text
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);
        context.drawTexture(icon, x, y, 0, 0, 16, 16, 16, 16);
        context.drawText(client.textRenderer, Text.literal(statusText), x + 20, y + 4, textColor, true);

        // Reset shader color and disable blend
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
    }
}