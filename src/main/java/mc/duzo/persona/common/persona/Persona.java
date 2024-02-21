package mc.duzo.persona.common.persona;

import mc.duzo.persona.common.PersonaSounds;
import mc.duzo.persona.common.skill.SkillSet;
import mc.duzo.persona.util.Identifiable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class Persona implements Identifiable {
    // todo needs docs
    // todo needs cleaning up, this should be abstract really.
    private final Identifier id;
    private final Identifier texture;
    private final SkillSet skills;
    private final SoundEvent summonSound;
    private int level;

    protected Persona(Identifier id, SkillSet skills, Identifier texture, SoundEvent sound) {
        this.id = id;
        this.skills = skills;
        this.texture = texture;
        this.summonSound = sound;
    }
    protected Persona(Identifier id, SkillSet skills, Identifier texture) {
        this(id, skills, texture, PersonaSounds.PERSONA_SHOUT);
    }
    public Persona(NbtCompound nbt) {
        this.id = new Identifier(nbt.getString("id"));
        this.skills = SkillSet.fromNbt(nbt.getCompound("SkillSet"));
        this.texture = new Identifier(nbt.getString("Texture"));
        this.summonSound = SoundEvent.of(new Identifier(nbt.getString("Sound")));

        this.loadNbt(nbt);
    }

    public static Persona create(Identifier id, SkillSet skills, Identifier texture, SoundEvent sound) {
        return new Persona(id, skills, texture, sound);
    }
    public static Persona create(Identifier id, SkillSet skills, Identifier texture) {
        return new Persona(id, skills, texture);
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", skills=" + skills +
                ", level=" + level +
                '}';
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    public SkillSet getSkillSet() {
        return this.skills;
    }

    public int getLevel() {
        return this.level;
    }

    public void levelUp() {
        this.level++;
    }

    public SoundEvent getSummonSound() {
        return this.summonSound;
    }

    public Identifier texture() {
        return this.texture;
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();

        nbt.putString("id", this.id.toString());
        nbt.put("SkillSet", this.skills.toNbt());
        nbt.putInt("Level", this.level);
        nbt.putString("Texture", this.texture.toString());
        nbt.putString("Sound", this.summonSound.getId().toString());

        return nbt;
    }

    public Persona loadNbt(NbtCompound nbt) {
        if (!(this.id.equals(new Identifier(nbt.getString("id"))))) {
            throw new RuntimeException("Attempted to load persona with mismatched id");
        }

        this.level = nbt.getInt("Level");

        return this;
    }
}
