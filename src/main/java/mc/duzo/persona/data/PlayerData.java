package mc.duzo.persona.data;

import mc.duzo.persona.common.persona.Persona;
import mc.duzo.persona.network.PersonaMessages;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Player specific nbt data which will be stored in {@link ServerData}
 * Remember to sync and markDirty!!
 *
 * @author duzo
 */
public class PlayerData {
    public static final int MAX_SP = 100;

    private Persona persona;
    private boolean hasTarget;
    private int target;
    private int spiritPoints;
    private boolean personaRevealed;

    public Optional<Persona> findPersona() {
        return Optional.ofNullable(this.persona);
    }

    /**
     * Sets the players persona
     * Will not call markdirty
     */
    public void setPersona(Persona persona) {
        this.persona = persona;
    }
    public void setPersona(Persona persona, MinecraftServer server) {
        this.setPersona(persona);

        ServerData.getServerState(server).markDirty();
    }

    /**
     * Sets this players target
     * Calls markdirty if on server
     * @param target the new target
     */
    public void setTarget(@Nullable LivingEntity target) {
        if (target == null) {
            this.target = 0;
            this.hasTarget = false;
            return;
        }

        this.target = target.getId();
        this.hasTarget = true;

        if (!target.getWorld().isClient()) {
            ServerData.getServerState(target.getServer()).markDirty();
        }
    }
    public Optional<LivingEntity> findTarget(ServerWorld world) {
        if (!this.hasTarget) return Optional.empty();

        Entity target = world.getEntityById(this.target);

        if (!(target instanceof LivingEntity))
            return Optional.empty();

        return Optional.of((LivingEntity) target);
    }

    public int spiritPoints() {
        return this.spiritPoints;
    }
    public void removeSP(int amount) {
        this.spiritPoints = Math.max(0, this.spiritPoints - amount);
    }
    public void addSP(int amount) {
        this.spiritPoints = Math.min(MAX_SP, this.spiritPoints + amount);
    }
    public boolean hasEnoughSP(int amount) {
        return this.spiritPoints >= amount;
    }

    public boolean isPersonaRevealed() {
        return this.personaRevealed;
    }
    public void revealPersona() {
        if (this.findPersona().isEmpty()) {
            if (this.isPersonaRevealed()) this.hidePersona();
            return;
        }

        this.personaRevealed = true;
    }
    public void hidePersona() {
        this.personaRevealed = false;
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();

        if (this.findPersona().isPresent())
            nbt.put("persona", this.persona.toNbt());

        nbt.putInt("SP", this.spiritPoints());
        nbt.putBoolean("PersonaRevealed", this.isPersonaRevealed());

        return nbt;
    }

    public static PlayerData createFromNbt(NbtCompound nbt) {
        PlayerData data = new PlayerData();

        if (nbt.contains("persona"))
            data.persona = new Persona(nbt.getCompound("persona"));

        if (nbt.contains("SP"))
            data.spiritPoints = nbt.getInt("SP");

        if (nbt.contains("PersonaRevealed"))
            data.personaRevealed = nbt.getBoolean("PersonaRevealed");

        return data;
    }
}
