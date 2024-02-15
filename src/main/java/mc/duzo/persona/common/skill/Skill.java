package mc.duzo.persona.common.skill;

import mc.duzo.persona.common.PersonaSounds;
import mc.duzo.persona.common.persona.Persona;
import mc.duzo.persona.data.PlayerData;
import mc.duzo.persona.data.ServerData;
import mc.duzo.persona.network.PersonaMessages;
import mc.duzo.persona.util.Identifiable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public abstract class Skill implements Identifiable {
    private final Identifier id;

    protected Skill(Identifier id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "id=" + id +
                '}';
    }

    public void run(ServerPlayerEntity source, Persona persona, LivingEntity target) {
        PlayerData data = ServerData.getPlayerState(source);

        if (this.usesHealth()) {
            source.damage(source.getDamageSources().generic(), source.getMaxHealth() * (this.cost() / 100f));
            return;
        }

        data.removeSP(this.cost());

        ServerData.getServerState(source.getServer()).markDirty();
        PersonaMessages.syncData(source, source);
    }

    public Text name() {
        return Text.translatable("skill." + id.getNamespace() + "." + id.getPath());
    }
    public Text description() {
        return Text.translatable("skill." + id.getNamespace() + "." + id.getPath());
    }
    public abstract boolean usesHealth();
    public abstract int cost();
    public abstract double cooldown();
    public SoundEvent useSound() {
        return PersonaSounds.ATTACK;
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

    public static Skill create(Identifier id, RunSkill onRun, boolean usesHealth, int cost, double cooldown, @Nullable SoundEvent sound) {
        return new Skill(id) {
            @Override
            public void run(ServerPlayerEntity source, Persona persona, LivingEntity target) {
                super.run(source, persona, target);

                onRun.run(source, persona, target);
            }

            @Override
            public boolean usesHealth() {
                return usesHealth;
            }

            @Override
            public int cost() {
                return cost;
            }

            @Override
            public double cooldown() {
                return cooldown;
            }

            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public SoundEvent useSound() {
                if (sound == null) return super.useSound();
                return sound;
            }
        };
    }
    public static Skill create(Identifier id, RunSkill onRun, boolean usesHealth, int cost, double cooldown) {
        return create(id, onRun, usesHealth, cost, cooldown, null);
    }
    public interface RunSkill {
        void run(ServerPlayerEntity source, Persona persona, LivingEntity target);
    }
}
