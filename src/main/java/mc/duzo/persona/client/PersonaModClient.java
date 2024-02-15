package mc.duzo.persona.client;

import mc.duzo.persona.client.hud.SPHudOverlay;
import mc.duzo.persona.client.network.PersonaClientMessages;
import mc.duzo.persona.client.sound.PlayerFollowingLoopingSound;
import mc.duzo.persona.client.sound.SoundsManager;
import mc.duzo.persona.client.util.Keybinds;
import mc.duzo.persona.common.PersonaSounds;
import mc.duzo.persona.util.VelvetUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;

public class PersonaModClient implements ClientModInitializer {
    public static final SoundsManager sounds = SoundsManager.create();

    @Override
    public void onInitializeClient() {
        PersonaClientMessages.initialise();
        Keybinds.initialise();

        HudRenderCallback.EVENT.register(new SPHudOverlay());

        ClientTickEvents.END_CLIENT_TICK.register(this::tick);

        ClientEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!(entity instanceof ClientPlayerEntity player)) return;

            PersonaClientMessages.askForPlayerData(player.getUuid());
        });
    }

    private void tick(MinecraftClient client) {
        if (client.player == null) return;
    }
}
