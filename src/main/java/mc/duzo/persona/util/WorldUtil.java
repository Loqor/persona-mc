package mc.duzo.persona.util;

import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.common.PersonaDimensions;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Set;

/**
 * Utilities related to the world and the server
 *
 * @author duzo
 * */
public class WorldUtil {
    public static MinecraftServer getServer() {
        return PersonaMod.server;
    }

    public static ServerWorld findWorld(RegistryKey<World> key) {
        return WorldUtil.getServer().getWorld(key);
    }

    public static ServerWorld findWorld(Identifier identifier) {
        return WorldUtil.findWorld(RegistryKey.of(RegistryKeys.WORLD, identifier));
    }

    public static ServerWorld findWorld(String identifier) {
        return WorldUtil.findWorld(new Identifier(identifier));
    }

    public static void teleport(LivingEntity entity, ServerWorld target, Vec3d pos, float yaw, float pitch) {
        if (entity instanceof ServerPlayerEntity player) {
            WorldUtil.teleportToWorld(player, target, pos, player.getYaw(), player.getPitch());
            return;
        }

        if (entity.getWorld().getRegistryKey().equals(target.getRegistryKey())) {
            entity.refreshPositionAndAngles(pos.x, pos.y, pos.z, yaw, pitch);
            return;
        }

        entity.teleport(target, pos.x, pos.y, pos.z, Set.of(), yaw, pitch);
    }

    private static void teleportToWorld(ServerPlayerEntity player, ServerWorld target, Vec3d pos, float yaw, float pitch) {
        player.teleport(target, pos.x, pos.y, pos.z, yaw, pitch);
        player.addExperience(0);

        player.getStatusEffects().forEach(effect -> {
            player.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(player.getId(), effect));
        });
        player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
    }
}
