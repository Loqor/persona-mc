package mc.duzo.persona.datagen.provider.sound;

import net.minecraft.sound.SoundEvent;

@FunctionalInterface
public interface CustomSoundBuilder {
    void add(String soundName, SoundEvent[] soundEvents);

    default void add(String soundName, SoundEvent soundEvent) {
        add(soundName, new SoundEvent[]{soundEvent});
    }
}
