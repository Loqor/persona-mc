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

    private static KeyBinding nextSkillKey;
    private static boolean wasNextSkillHeld;

    private static KeyBinding previousSkillKey;
    private static boolean wasPreviousSkillHeld;

    private static KeyBinding useSkillKey;
    private static boolean wasUseSkillHeld;

    public static void initialise() {
        targetingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + PersonaMod.MOD_ID + ".target",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "category." + PersonaMod.MOD_ID
        ));

        nextSkillKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + PersonaMod.MOD_ID + ".next_skill",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category." + PersonaMod.MOD_ID
        ));

        previousSkillKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + PersonaMod.MOD_ID + ".previous_skill",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "category." + PersonaMod.MOD_ID
        ));

        useSkillKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + PersonaMod.MOD_ID + ".use_skill",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "category." + PersonaMod.MOD_ID
        ));


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity player = client.player;
            if (player == null) return;

            tickTargetingKey(player);
            tickNextSkillKey(player);
            tickPreviousSkillKey(player);
            tickUseSkillKey(player);
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

    private static void tickNextSkillKey(ClientPlayerEntity player) {
        if (!nextSkillKey.wasPressed() && wasNextSkillHeld) {
            wasNextSkillHeld = false;
            return;
        }

        if (wasNextSkillHeld || !nextSkillKey.isPressed()) return;

        wasNextSkillHeld = true; // Does not appear to work

        PersonaClientMessages.sendChangeSkillRequest(true);
    }
    private static void tickPreviousSkillKey(ClientPlayerEntity player) {
        if (!previousSkillKey.wasPressed() && wasPreviousSkillHeld) {
            wasPreviousSkillHeld = false;
            return;
        }

        if (wasPreviousSkillHeld || !previousSkillKey.isPressed()) return;

        wasPreviousSkillHeld = true; // Does not appear to work

        PersonaClientMessages.sendChangeSkillRequest(false);
    }

    private static void tickUseSkillKey(ClientPlayerEntity player) {
        if (!useSkillKey.wasPressed() && wasUseSkillHeld) {
            wasUseSkillHeld = false;
            return;
        }

        if (wasUseSkillHeld || !useSkillKey.isPressed()) return;

        wasUseSkillHeld = true; // Does not appear to work

        PersonaClientMessages.sendUseSkillRequest();
    }
}
