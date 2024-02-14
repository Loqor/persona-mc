package mc.duzo.persona.mixin.client;

import mc.duzo.persona.util.VelvetUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicTracker.class)
public class MusicMixin {
    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    public void persona_removeMusic(MusicSound type, CallbackInfo ci) {
        if (MinecraftClient.getInstance().player != null && VelvetUtil.isInVelvetRoom(MinecraftClient.getInstance().player)) {
            ci.cancel();
        }
    }
}
