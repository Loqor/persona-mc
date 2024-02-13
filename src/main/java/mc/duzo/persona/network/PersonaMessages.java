package mc.duzo.persona.network;

import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.data.PlayerData;
import mc.duzo.persona.data.ServerData;
import mc.duzo.persona.util.TargetingUtil;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.UUID;

public class PersonaMessages {
    public static final Identifier SYNC_DATA = new Identifier(PersonaMod.MOD_ID, "sync_data");
    public static final Identifier PRESS_TARGET = new Identifier(PersonaMod.MOD_ID, "press_ability");

    public static void initialise() {
        ServerPlayNetworking.registerGlobalReceiver(PRESS_TARGET, ((server, player, handler, buf, responseSender) -> recieveTargetRequest(player)));
    }

    public static void syncAllData(ServerPlayerEntity target) {
        for (UUID uuid : ServerData.getKeys(target.getServer())) {
            syncData(target, uuid);
        }
    }
    public static void syncData(ServerPlayerEntity target, ServerPlayerEntity player) {
        PlayerData data = ServerData.getPlayerState(player);

        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeUuid(player.getUuid());
        buf.writeNbt(data.toNbt());

        ServerPlayNetworking.send(target, SYNC_DATA, buf);
    }
    public static void syncData(ServerPlayerEntity target, UUID uuid) {
        ServerPlayerEntity player = target.getServer().getPlayerManager().getPlayer(uuid);

        if (player == null) {
            PersonaMod.LOGGER.warn("Tried to sync non-existent player: " + uuid.toString());
            return;
        }

        syncData(target, player);
    }

    private static void recieveTargetRequest(ServerPlayerEntity player) {
        Optional<LivingEntity> found = TargetingUtil.findEntityBeingLookedAt(player);

        LivingEntity target = found.orElse(null);

        PlayerData data = ServerData.getPlayerState(player);
        data.setTarget(target);
    }
}