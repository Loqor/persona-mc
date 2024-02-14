package mc.duzo.persona.datagen;

import mc.duzo.persona.common.PersonaSounds;
import mc.duzo.persona.datagen.provider.PersonaSoundProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class PersonaDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator gen) {
		FabricDataGenerator.Pack pack = gen.createPack();
		genSounds(pack);
	}

	private void genSounds(FabricDataGenerator.Pack pack) {
		pack.addProvider((((output, registriesFuture) -> {
			PersonaSoundProvider provider = new PersonaSoundProvider(output);

			// Music
			provider.addSound("velvet_room", PersonaSounds.VELVET_MUSIC);

			return provider;
		})));
	}
}
