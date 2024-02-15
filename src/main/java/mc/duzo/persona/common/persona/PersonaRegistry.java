package mc.duzo.persona.common.persona;

import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.common.PersonaSounds;
import mc.duzo.persona.common.skill.Skill;
import mc.duzo.persona.common.skill.SkillRegistry;
import mc.duzo.persona.common.skill.SkillSet;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class PersonaRegistry {
    public static final SimpleRegistry<Persona> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<Persona>ofRegistry(new Identifier(PersonaMod.MOD_ID, "persona"))).buildAndRegister();

    public static Persona register(Persona persona) {
        return Registry.register(REGISTRY, persona.id(), persona);
    }

    public static Persona get(Identifier id) {
        return REGISTRY.get(id);
    }

    public static Persona DEV;

    public static void init() {
        DEV = register(Persona.create(
                new Identifier(PersonaMod.MOD_ID, "dev"),
                new SkillSet(
                        SkillRegistry.DIA,
                        SkillRegistry.CLEAVE,
                        SkillRegistry.ZIO,
                        SkillRegistry.TRAFURI,
                        SkillRegistry.VELVET
                ),
                new Identifier(PersonaMod.MOD_ID, "textures/skins/arsene.png"), // TODO - ask for permission (https://www.planetminecraft.com/member/magpied/) / ask for them to make more / ask for one of my texture friends to make some
                PersonaSounds.ARSENE_SHOUT
        ));
    }
}
