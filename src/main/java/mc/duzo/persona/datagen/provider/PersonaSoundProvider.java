package mc.duzo.persona.datagen.provider;

import mc.duzo.persona.datagen.provider.sound.CustomSoundBuilder;
import mc.duzo.persona.datagen.provider.sound.CustomSoundProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.sound.SoundEvent;

import java.util.HashMap;

public class PersonaSoundProvider extends CustomSoundProvider {
    private final FabricDataOutput dataGenerator;

    private final HashMap<String, SoundEvent[]> soundEventList = new HashMap<>();

    public PersonaSoundProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
        dataGenerator = dataOutput;
    }

    @Override
    public void generateSoundsData(CustomSoundBuilder builder) {
        soundEventList.forEach(builder::add);
    }

    public void addSound(String soundName, SoundEvent sound) {
        soundEventList.put(soundName, new SoundEvent[]{sound});
    }

    public void addSound(String soundName, SoundEvent[] soundEvents) {
        soundEventList.put(soundName, soundEvents);
    }
}
