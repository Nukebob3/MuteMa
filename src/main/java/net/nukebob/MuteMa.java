package net.nukebob;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.nukebob.effects.ModEffects;

public class MuteMa implements ModInitializer {
	public static final String MOD_ID = "mutema";

	public static boolean muted = false;

	@Override
	public void onInitialize() {
		ModEffects.registerEffects();
		ClientTickEvents.END_CLIENT_TICK.register(this::manageEffect);
	}

	private void manageEffect(MinecraftClient client) {
		if (muted) {
			if (MinecraftClient.getInstance().player != null) MinecraftClient.getInstance().player.addStatusEffect(new StatusEffectInstance(ModEffects.MUTED_EFFECT, -1, 0, true, true));
		}
	}
}