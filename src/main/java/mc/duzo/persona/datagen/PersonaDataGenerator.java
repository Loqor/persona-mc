package mc.duzo.persona.datagen;

import mc.duzo.persona.common.PersonaSounds;
import mc.duzo.persona.common.persona.Persona;
import mc.duzo.persona.common.skill.Skill;
import mc.duzo.persona.common.skill.SkillRegistry;
import mc.duzo.persona.datagen.provider.PersonaSoundProvider;
import mc.duzo.persona.datagen.provider.lang.LanguageProvider;
import mc.duzo.persona.datagen.provider.lang.LanguageType;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.apache.commons.codec.language.bm.Lang;

public class PersonaDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator gen) {
		FabricDataGenerator.Pack pack = gen.createPack();
		genSounds(pack);
		genLang(pack);
	}

	private void genSounds(FabricDataGenerator.Pack pack) {
		pack.addProvider((((output, registriesFuture) -> {
			PersonaSoundProvider provider = new PersonaSoundProvider(output);

			// Music
			provider.addSound("velvet_room", PersonaSounds.VELVET_MUSIC);

			// Skills
			provider.addSound("attack", PersonaSounds.ATTACK);
			provider.addSound("dia", PersonaSounds.DIA);

			// PERSONA!!
			provider.addSound("persona_shout", PersonaSounds.PERSONA_SHOUT);
			provider.addSound("arsene", PersonaSounds.ARSENE_SHOUT);

			// Other
			provider.addSound("welcome_velvet", PersonaSounds.WELCOME_VELVET);

			return provider;
		})));
	}

	private void genLang(FabricDataGenerator.Pack pack) {
		genEnglish(pack);
	}

	private void genEnglish(FabricDataGenerator.Pack pack) {
		pack.addProvider((((output, registriesFuture) -> {
			LanguageProvider provider = new LanguageProvider(output, LanguageType.EN_US);

			for (Skill skill : SkillRegistry.REGISTRY) {
				Identifier id = skill.id();

				provider.addTranslation("skill." + id.getNamespace() + "." + id.getPath(), skill.id().getPath());
			}

			return provider;
		})));
	}
}
