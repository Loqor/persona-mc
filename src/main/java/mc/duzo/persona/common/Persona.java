package mc.duzo.persona.common;

import mc.duzo.persona.util.Identifiable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class Persona implements Identifiable {
    private final Identifier id;

    protected Persona(Identifier id) {
        this.id = id;
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();

        nbt.putString("id", this.id.toString());

        return nbt;
    }

    public static Persona fromNbt(NbtCompound nbt) {
        Persona created = new Persona(new Identifier(nbt.getString("id")));

        return created;
    }
}
