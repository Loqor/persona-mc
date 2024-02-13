package mc.duzo.persona.data;

import mc.duzo.persona.common.Persona;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

/**
 * Player specific nbt data which will be stored in {@link ServerData}
 *
 * @author duzo
 */
public class PlayerData {
    private Persona persona;

    public Optional<Persona> getPersona() {
        return Optional.ofNullable(this.persona);
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
