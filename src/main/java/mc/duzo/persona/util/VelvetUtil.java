package mc.duzo.persona.util;

import mc.duzo.persona.common.PersonaDimensions;
import mc.duzo.persona.network.PersonaMessages;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

/**
 * Welcome to the Velvet Room.
 * Utilities for the Room.
 *
 * @author duzo
 */
public class VelvetUtil {
    public static boolean isInVelvetRoom(LivingEntity entity) {
        return isVelvetRoom(entity.getWorld());
    }
    public static boolean isVelvetRoom(World world) {
        return PersonaDimensions.VELVET_DIM_WORLD.equals(world.getRegistryKey());
    }

    public static void onEnter(LivingEntity entity) {
        if (entity instanceof ServerPlayerEntity player) {
            PersonaMessages.sendVelvetChange(player, true);
        }
    }
    public static void onExit(LivingEntity entity) {
        if (entity instanceof ServerPlayerEntity player) {
            PersonaMessages.sendVelvetChange(player, false);
        }
    }
}
