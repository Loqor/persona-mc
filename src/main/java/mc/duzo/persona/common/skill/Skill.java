package mc.duzo.persona.common.skill;

import mc.duzo.persona.common.persona.Persona;
import mc.duzo.persona.util.Identifiable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class Skill implements Identifiable {
    private final Identifier id;

    protected Skill(Identifier id) {
        this.id = id;
    }

    public abstract void run(Persona persona, LivingEntity target);

    public Text description() {
        return Text.translatable("skill." + id.getNamespace() + "." + id.getPath());
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();

        nbt.putString("id", this.id.toString());

        return nbt;
    }

    public Skill loadNbt(NbtCompound nbt) {
        if (!(this.id.equals(new Identifier(nbt.getString("id"))))) {
            throw new RuntimeException("Attempted to load skill with mismatched id");
        }

        return this;
    }
    public static Skill fromNbt(NbtCompound nbt) {
        return SkillRegistry.get(new Identifier(nbt.getString("id")));
    }

    public interface RunSkill {
        void run(Persona persona, LivingEntity target);
    }

    public static class Builder {
        private final Identifier id;
        private final RunSkill onRun;

        public Builder(Identifier id, RunSkill onRun) {
            this.id = id;
            this.onRun = onRun;
        }

        public Skill build() {
            return new Skill(id) {
                @Override
                public Identifier id() {
                    return id;
                }

                @Override
                public void run(Persona persona, LivingEntity target) {
                    onRun.run(persona, target);
                }
            };
        }
    }
}
