package mc.duzo.persona.common.skill;

import mc.duzo.persona.common.persona.Persona;
import mc.duzo.persona.data.PlayerData;
import mc.duzo.persona.data.ServerData;
import mc.duzo.persona.util.Identifiable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class Skill implements Identifiable {
    private final Identifier id;

    protected Skill(Identifier id) {
        this.id = id;
    }

    public void run(ServerPlayerEntity source, Persona persona, LivingEntity target) {
        PlayerData data = ServerData.getPlayerState(source);

        if (this.usesHealth()) {
            source.damage(source.getDamageSources().generic(), source.getMaxHealth() * (this.cost() / 100f));
            return;
        }

        data.removeSP(this.cost());
        ServerData.getServerState(source.getServer()).markDirty();
        source.sendMessage(Text.literal(data.spiritPoints() + " SP remaining"), false);
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

    public static Skill create(Identifier id, RunSkill onRun, boolean usesHealth, int cost, double cooldown) {
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
        };
    }
    public interface RunSkill {
        void run(ServerPlayerEntity source, Persona persona, LivingEntity target);
    }
}
