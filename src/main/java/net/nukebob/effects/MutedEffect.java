package net.nukebob.effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class MutedEffect extends StatusEffect {
    public MutedEffect() {
        super(StatusEffectCategory.NEUTRAL, 0);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return false;
    }
}
