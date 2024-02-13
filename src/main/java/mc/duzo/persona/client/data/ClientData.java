package mc.duzo.persona.client.data;

import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.data.PlayerData;
import mc.duzo.persona.data.ServerData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.UUID;

/**
 * The clients version of {@link ServerData}
 * Should be synced within reason to avoid overload
 * Loads from NBT
 *
 * @author duzo
 */
public class ClientData {
    private static ClientData instance;
    private HashMap<UUID, PlayerData> players = new HashMap<>();

    public static ClientData getInstance() {
        if (instance == null) {
            PersonaMod.LOGGER.warn("ClientData has not been initialized, creating new instance.");
            instance = new ClientData();
        }
        return instance;
    }

    public static void addPlayer(UUID uuid, NbtCompound data) {
        PlayerData playerData = PlayerData.createFromNbt(data);
        getInstance().players.put(uuid, playerData);
    }

    public static PlayerData getPlayerState(UUID uuid) {
        return getInstance().players.computeIfAbsent(uuid, key -> new PlayerData());
    }
    public static PlayerData getPlayerState(LivingEntity player) {
        return getPlayerState(player.getUuid());
    }
}
