package mc.duzo.persona.common;

import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.common.persona.Persona;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class PersonaDimensions {
    public static final RegistryKey<World> VELVET_DIM_WORLD = RegistryKey.of(RegistryKeys.WORLD, new Identifier(PersonaMod.MOD_ID, "velvet_room"));
}
