package mc.duzo.persona.client.util;

import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.client.network.PersonaClientMessages;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    private static KeyBinding targetingKey;
    private static boolean wasTargetingHeld;

    public static void initialise() {
        targetingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + PersonaMod.MOD_ID + ".ability",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category." + PersonaMod.MOD_ID
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity player = client.player;
            if (player == null) return;

            tickTargetingKey(player);
        });
    }

    private static void tickTargetingKey(ClientPlayerEntity player) {
        if (!targetingKey.wasPressed() && wasTargetingHeld) {
            wasTargetingHeld = false;
            return;
        }

        if (wasTargetingHeld || !targetingKey.isPressed()) return;

        wasTargetingHeld = true; // Does not appear to work

        PersonaClientMessages.sendTargetChangeRequest();
    }
}
