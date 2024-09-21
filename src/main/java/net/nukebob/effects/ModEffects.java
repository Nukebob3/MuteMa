package net.nukebob.effects;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.nukebob.MuteMa;

@Environment(EnvType.CLIENT)
public class ModEffects {
    public static RegistryEntry<StatusEffect> MUTED_EFFECT;

    public static RegistryEntry<StatusEffect> registerStatusEffect(String name) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(MuteMa.MOD_ID, name), new MutedEffect());
    }

    public static void registerEffects() {
        MUTED_EFFECT = registerStatusEffect("muted");
    }
}
