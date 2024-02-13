package mc.duzo.persona.data;

import mc.duzo.persona.PersonaMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

/**
 * Data that will be saved to the world in .nbt form
 * For saving across server restarts.
 * Remember to call markDirty() after setting a value to ensure it saves
 *
 * @author duzo
 */
public class ServerData extends PersistentState {
    private HashMap<UUID, PlayerData> players = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {

        NbtCompound playersNbt = new NbtCompound();

        players.forEach(((uuid, playerData) -> {
            playersNbt.put(uuid.toString(), playerData.toNbt());
        }));

        nbt.put("players", playersNbt);

        return nbt;
    }
    public static ServerData loadNbt(NbtCompound nbt) {
        ServerData data = new ServerData();

        NbtCompound playersNbt = nbt.getCompound("players");

        playersNbt.getKeys().forEach(key -> {
            PlayerData playerData = PlayerData.createFromNbt(playersNbt.getCompound(key));

            UUID uuid = UUID.fromString(key);
            data.players.put(uuid, playerData);
        });

        return data;
    }
    public static ServerData getServerState(MinecraftServer server) {
        PersistentStateManager manager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        ServerData state = manager.getOrCreate(
                ServerData::loadNbt,
                ServerData::new,
                PersonaMod.MOD_ID
        );

        return state;
    }

    public static PlayerData getPlayerState(LivingEntity player) {
        ServerData serverData = getServerState(player.getWorld().getServer());

        PlayerData playerData = serverData.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerData());

        return playerData;
    }
}
