package net.nukebob.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nukebob.MuteMa;
import net.nukebob.effects.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    @Unique
    private TextIconButtonWidget textIconButtonWidget = createButton(20, (buttonWidget) -> {
        playSound();
        muteButtonPress();
    }, volume == 0.0);

    @Unique
    private static float volume = MinecraftClient.getInstance().options.getSoundVolume(SoundCategory.MASTER);

    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void addMuteButton(CallbackInfo ci) {
        refresh(this.textIconButtonWidget);
        this.addDrawableChild(this.textIconButtonWidget);
    }

    @Inject(at = @At("RETURN"), method = "initTabNavigation")
    private void refreshMuteButtonPosition(CallbackInfo ci) {
        refresh(this.textIconButtonWidget);
    }

    @Unique
    private void refresh(ButtonWidget buttonWidget) {
        for (ButtonWidget button : this.children().stream().filter(e -> e instanceof ButtonWidget).map(e -> (ButtonWidget) e).toList()) {
            if (button.getMessage().equals(Text.translatable("options.sounds"))) {
                int buttonX = button.getX();
                int buttonY = button.getY();
                int buttonWidth = button.getWidth();

                buttonWidget.setPosition(buttonX + buttonWidth + 5, buttonY);

                break;
            }
        }
    }

    @Unique
    private void playSound() {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f), 0);
    }

    @Unique
    private void muteButtonPress() {
        MinecraftClient client = MinecraftClient.getInstance();
        switchImage();
        if (volume == 0.0f) {
            client.getSoundManager().updateSoundVolume(SoundCategory.MASTER, client.options.getSoundVolume(SoundCategory.MASTER));
            volume = client.options.getSoundVolume(SoundCategory.MASTER);
            if (volume == 0.0f) {
                MinecraftClient.getInstance().getSoundManager().updateSoundVolume(SoundCategory.MASTER, 1.0f);
                volume = 1.0f;
            }
            if (client.player != null) {
                client.player.removeStatusEffect(ModEffects.MUTED_EFFECT);
            }

            MuteMa.muted = false;
        } else {
            client.getSoundManager().updateSoundVolume(SoundCategory.MASTER, 0.0f);
            volume = 0.0f;

            if (client.player != null) {
                client.player.addStatusEffect(new StatusEffectInstance(ModEffects.MUTED_EFFECT, -1, 0, true, true));
            }

            MuteMa.muted = true;
        }
    }

    @Unique
    private void switchImage() {
        this.remove(this.textIconButtonWidget);
        this.textIconButtonWidget = createButton(20, (buttonWidget) -> {
            playSound();
            muteButtonPress();
        }, volume != 0.0);
        refresh(this.textIconButtonWidget);
        this.addDrawableChild(textIconButtonWidget);
    }

    @Unique
    private static TextIconButtonWidget createButton(int width, ButtonWidget.PressAction onPress, boolean muted) {
        return TextIconButtonWidget.builder(Text.empty(), onPress, true).width(width).texture(muted ? Identifier.of(MuteMa.MOD_ID, "icon/muted") : Identifier.of(MuteMa.MOD_ID, "icon/unmuted"), 16, 16).build();
    }
}