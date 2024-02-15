package mc.duzo.persona.common;

import mc.duzo.persona.PersonaMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class PersonaSounds {
    public static final SoundEvent VELVET_MUSIC = register("velvet_room");
    public static final SoundEvent WELCOME_VELVET = register("welcome_velvet");
    public static final SoundEvent ATTACK = register("attack");
    public static final SoundEvent PERSONA_SHOUT = register("persona_shout");
    public static final SoundEvent ARSENE_SHOUT = register("arsene");

    public static void init() {

    }

    private static SoundEvent register(String name) {
        return register(new Identifier(PersonaMod.MOD_ID, name));
    }
    private static SoundEvent register(Identifier id) {
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}
