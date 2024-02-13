package mc.duzo.persona.data;

import mc.duzo.persona.common.persona.Persona;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * Player specific nbt data which will be stored in {@link ServerData}
 *
 * @author duzo
 */
public class PlayerData {
    private Persona persona;
    private boolean hasTarget;
    private int target;

    public Optional<Persona> getPersona() {
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
    public static Optional<LivingEntity> getTarget(ServerPlayerEntity player) {
        PlayerData data = ServerData.getPlayerState(player);

        if (!data.hasTarget) return Optional.empty();

        Entity target = player.getServerWorld().getEntityById(data.target);

        if (!(target instanceof LivingEntity))
            return Optional.empty();

        return Optional.of((LivingEntity) target);
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();

        if (this.getPersona().isPresent())
            nbt.put("persona", this.persona.toNbt());

        return nbt;
    }

    public static PlayerData createFromNbt(NbtCompound nbt) {
        PlayerData data = new PlayerData();

        if (nbt.contains("persona"))
            data.persona = new Persona(nbt.getCompound("persona"));

        return data;
    }
}
