package mc.duzo.persona.data;

import mc.duzo.persona.common.Persona;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
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
    private UUID target;

    public Optional<Persona> getPersona() {
        return Optional.ofNullable(this.persona);
    }

    /**
     * Sets this players target
     * Calls markdirty if on server
     * @param target the new target
     */
    public void setTarget(@Nullable LivingEntity target) {
        if (target == null) {
            this.target = null;
            return;
        }

        this.target = target.getUuid();

        if (!target.getWorld().isClient()) {
            ServerData.getServerState(target.getServer()).markDirty();
        }
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
