package mc.duzo.persona.common.entity.door;

import mc.duzo.persona.PersonaMod;
import net.minecraft.util.Identifier;

public enum VelvetDoorVariant {
    FOUR("four");

    final Identifier texture;
    VelvetDoorVariant(Identifier texture) {
        this.texture = texture;
    }
    VelvetDoorVariant(String name) {
        this(new Identifier(PersonaMod.MOD_ID, "textures/entity/door/" + name + ".png"));
    }

    public Identifier texture() {
        return this.texture;
    }

    public static VelvetDoorVariant getRandomVariant() {
        return VelvetDoorVariant.values()[PersonaMod.RANDOM.nextInt(VelvetDoorVariant.values().length)];
    }
}
