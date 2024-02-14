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
                GLFW.GLFW_KEY_N,
                "category." + PersonaMod.MOD_ID
        ));

        previousSkillKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + PersonaMod.MOD_ID + ".previous_skill",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
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
        if (!targetingKey.wasPressed()) {
            if (wasTargetingHeld)
                wasTargetingHeld = false;
            return;
        }

        if (wasTargetingHeld) return;

        wasTargetingHeld = true;

        PersonaClientMessages.sendTargetChangeRequest();
    }

    private static void tickNextSkillKey(ClientPlayerEntity player) {
        if (!nextSkillKey.wasPressed()) {
            if (wasNextSkillHeld)
                wasNextSkillHeld = false;
            return;
        }

        if (wasNextSkillHeld) return;

        wasNextSkillHeld = true;

        PersonaClientMessages.sendChangeSkillRequest(true);
    }
    private static void tickPreviousSkillKey(ClientPlayerEntity player) {
        if (!previousSkillKey.wasPressed()) {
            if (wasPreviousSkillHeld)
                wasPreviousSkillHeld = false;
            return;
        }

        if (wasPreviousSkillHeld) return;

        wasPreviousSkillHeld = true;

        PersonaClientMessages.sendChangeSkillRequest(false);
    }

    private static void tickUseSkillKey(ClientPlayerEntity player) {
        if (!useSkillKey.wasPressed()) {
            if (wasUseSkillHeld)
                wasUseSkillHeld = false;
            return;
        }

        if (wasUseSkillHeld) return;

        wasUseSkillHeld = true;

        PersonaClientMessages.sendUseSkillRequest();
    }
}
