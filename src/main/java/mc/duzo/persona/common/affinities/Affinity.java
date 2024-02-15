package mc.duzo.persona.common.affinities;

import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.util.Identifiable;
import net.minecraft.util.Identifier;

public enum Affinity implements Identifiable {
    PHYS("phys"),
    FIRE("fire"),
    ICE("ice"),
    ELEC("elec"),
    FORCE("force"),
    LIGHT("light"),
    DARK("dark"),
    ALMIGHTY("almighty");

    private final Identifier id;

    Affinity(Identifier id) {
        this.id = id;
    }
    Affinity(String name) {
        this(new Identifier(PersonaMod.MOD_ID, name));
    }

    @Override
    public Identifier id() {
        return this.id;
    }
}
