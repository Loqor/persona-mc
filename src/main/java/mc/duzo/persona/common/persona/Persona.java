package mc.duzo.persona.common.persona;

import mc.duzo.persona.common.skill.SkillSet;
import mc.duzo.persona.util.Identifiable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class Persona implements Identifiable {
    private final Identifier id;
    private final SkillSet skills;

    public Persona(Identifier id, SkillSet skills) {
        this.id = id;
        this.skills = skills;
    }
    public Persona(NbtCompound nbt) {
        this.id = new Identifier(nbt.getString("id"));
        this.skills = SkillSet.fromNbt(nbt.getCompound("SkillSet"));

        this.loadNbt(nbt);
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    public SkillSet skills() {
        return this.skills;
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();

        nbt.putString("id", this.id.toString());
        nbt.put("SkillSet", this.skills.toNbt());

        return nbt;
    }

    public Persona loadNbt(NbtCompound nbt) {
        if (!(this.id.equals(new Identifier(nbt.getString("id"))))) {
            throw new RuntimeException("Attempted to load persona with mismatched id");
        }

        return this;
    }
}
