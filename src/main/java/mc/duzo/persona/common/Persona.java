package mc.duzo.persona.common;

import mc.duzo.persona.util.Identifiable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class Persona implements Identifiable {
    private final Identifier id;

    protected Persona(Identifier id) {
        this.id = id;
    }
    public Persona(NbtCompound nbt) {
        this.id = new Identifier(nbt.getString("id"));

        this.loadNbt(nbt);
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

    public Persona loadNbt(NbtCompound nbt) {
        if (!(this.id.equals(new Identifier(nbt.getString("id"))))) {
            throw new RuntimeException("Attempted to load persona with mismatched id");
        }

        return this;
    }
}
