package com.lambda.nopause;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoPause implements ModInitializer {
	public static final String MOD_ID = "nopause";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	private static boolean enabled = true;
	private static KeyBinding toggleKey;
	private static long lastToggleTime = 0;
	public static final long FADE_DURATION = 2000; // 2 seconds
	private static boolean hudAnimationActive = false;
	private static boolean showChatMessages = true;
	private static boolean showHudIndicator = true;

	public static boolean isEnabled() {
		return enabled;
	}

	public static long getLastToggleTime() {
		return lastToggleTime;
	}

	public static boolean shouldShowChatMessages() {
		return showChatMessages;
	}

	public static void setShowChatMessages(boolean show) {
		showChatMessages = show;
	}

	public static boolean shouldShowHudIndicator() {
		return showHudIndicator;
	}

	public static void setShowHudIndicator(boolean show) {
		showHudIndicator = show;
	}

	public static boolean isHudAnimationActive() {
		return hudAnimationActive;
	}

	public static void setHudAnimationActive(boolean active) {
		hudAnimationActive = active;
	}

	public static void setEnabled(boolean value, boolean showMessage) {
		if (enabled == value) return;
		enabled = value;
		lastToggleTime = System.currentTimeMillis();
		hudAnimationActive = true;

		if (showMessage && showChatMessages) {
			MinecraftClient client = MinecraftClient.getInstance();
			if (client != null && client.player != null) {
				client.player.sendMessage(
					Text.literal("§7[§bNoPause§7] §f" + (enabled ? "Enabled" : "Disabled")),
					false
				);
			}
		}
	}

	@Override
	public void onInitialize() {
		// Register keybinding
		toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.nopause.toggle",
			InputUtil.Type.KEYSYM,
			InputUtil.GLFW_KEY_N, // N key
			"category.nopause.general"
		));

		// Register client-side command
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(
				ClientCommandManager.literal("nopause")
					.then(ClientCommandManager.literal("enable")
						.executes(context -> {
							setEnabled(true, true);
							return 1;
						})
					)
					.then(ClientCommandManager.literal("disable")
						.executes(context -> {
							setEnabled(false, true);
							return 1;
						})
					)
					.then(ClientCommandManager.literal("notifications")
						.then(ClientCommandManager.literal("chat")
							.then(ClientCommandManager.literal("on")
								.executes(context -> {
									setShowChatMessages(true);
									context.getSource().sendFeedback(Text.literal("§7[§bNoPause§7] §fChat notifications enabled."));
									return 1;
								})
							)
							.then(ClientCommandManager.literal("off")
								.executes(context -> {
									setShowChatMessages(false);
									context.getSource().sendFeedback(Text.literal("§7[§bNoPause§7] §fChat notifications disabled."));
									return 1;
								})
							)
						)
						.then(ClientCommandManager.literal("visual")
							.then(ClientCommandManager.literal("on")
								.executes(context -> {
									setShowHudIndicator(true);
									context.getSource().sendFeedback(Text.literal("§7[§bNoPause§7] §fHUD visual enabled."));
									return 1;
								})
							)
							.then(ClientCommandManager.literal("off")
								.executes(context -> {
									setShowHudIndicator(false);
									context.getSource().sendFeedback(Text.literal("§7[§bNoPause§7] §fHUD visual disabled."));
									return 1;
								})
							)
						)
					)
			);
		});

		LOGGER.info("NoPause mod initialized!");
	}

	// Call this method every tick to check for keybinding
	public static void checkKeybinding() {
		while (toggleKey.wasPressed()) {
			setEnabled(!enabled, true);
		}
	}
}