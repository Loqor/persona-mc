package mc.duzo.persona;

import mc.duzo.persona.common.item.WearableItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * This is where all things are registered
 * Based off how Bug registered things
 *
 * @author bug
 * @author duzo
 */
public class Register {
    // Items
    public static final WearableItem JOKER_MASK = register(Registries.ITEM, "joker_mask", new WearableItem(EquipmentSlot.HEAD, true, new FabricItemSettings()));

    // Initialising & Registering

    public static void initialize() {}

    public static <V, T extends V> T register(Registry<V> registry, String name, T entry) {
        return Registry.register(registry, new Identifier(PersonaMod.MOD_ID, name), entry);
    }

    public static <T extends Block> T registerBlockAndItem(String name, T entry) {
        T output = Register.register(Registries.BLOCK, name, entry);
        Registry.register(Registries.ITEM, new Identifier(PersonaMod.MOD_ID, name), new BlockItem(output, new FabricItemSettings()));
        return output;
    }
}
