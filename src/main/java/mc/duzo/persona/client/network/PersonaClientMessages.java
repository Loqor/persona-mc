package mc.duzo.persona.client.network;

import mc.duzo.persona.client.data.ClientData;
import mc.duzo.persona.network.PersonaMessages;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

import java.util.UUID;

public class PersonaClientMessages {
    public static void initialise() {
        ClientPlayNetworking.registerGlobalReceiver(PersonaMessages.SYNC_DATA, ((client, handler, buf, responseSender) -> recievePlayerData(buf)));
    }

    private static void recievePlayerData(UUID uuid, NbtCompound nbt) {
        ClientData.addPlayer(uuid, nbt);
    }
    private static void recievePlayerData(PacketByteBuf buf) {
        UUID uuid = buf.readUuid();
        NbtCompound nbt = buf.readNbt();
        recievePlayerData(uuid, nbt);
    }

    public static void sendTargetChangeRequest() {
        ClientPlayNetworking.send(PersonaMessages.PRESS_TARGET, PacketByteBufs.empty());
    }

    public static void sendChangeSkillRequest(boolean next) {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeBoolean(next);

        ClientPlayNetworking.send(PersonaMessages.CHANGE_SKILL, buf);
    }

    public static void sendUseSkillRequest() {
        ClientPlayNetworking.send(PersonaMessages.USE_SKILL, PacketByteBufs.empty());
    }
}
