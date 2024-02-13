package mc.duzo.persona.common.skill;

import mc.duzo.persona.PersonaMod;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public class SkillRegistry {
    public static final SimpleRegistry<Skill> REGISTRY = FabricRegistryBuilder.createSimple(RegistryKey.<Skill>ofRegistry(new Identifier(PersonaMod.MOD_ID, "skill"))).buildAndRegister();

    public static Skill register(Skill skill) {
        return Registry.register(REGISTRY, skill.id(), skill);
    }

    public static Skill get(Identifier id) {
        return REGISTRY.get(id);
    }

    public static Skill DIA = register(Skill.create(new Identifier(PersonaMod.MOD_ID, "dia"), (source, persona, target) -> target.setHealth(target.getHealth() + 4), false, 3));
    public static Skill CLEAVE = register(Skill.create(new Identifier(PersonaMod.MOD_ID, "cleave"), (source, persona, target) -> target.damage(target.getDamageSources().generic(), 4), true, 5));

    public static void init() {

    }
}
