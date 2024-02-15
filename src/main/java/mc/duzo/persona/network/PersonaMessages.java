package mc.duzo.persona.network;

import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.client.network.PersonaClientMessages;
import mc.duzo.persona.common.persona.Persona;
import mc.duzo.persona.data.PlayerData;
import mc.duzo.persona.data.ServerData;
import mc.duzo.persona.util.PersonaUtil;
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
    // Data
    public static final Identifier SYNC_DATA = new Identifier(PersonaMod.MOD_ID, "sync_data");
    public static final Identifier ASK_DATA = new Identifier(PersonaMod.MOD_ID, "ask_data");

    // Skills
    public static final Identifier PRESS_TARGET = new Identifier(PersonaMod.MOD_ID, "press_target");
    public static final Identifier CHANGE_SKILL = new Identifier(PersonaMod.MOD_ID, "change_skill");
    public static final Identifier USE_SKILL = new Identifier(PersonaMod.MOD_ID, "use_skill");

    // Personas
    public static final Identifier PERSONA_TOGGLE = new Identifier(PersonaMod.MOD_ID, "persona_toggle");

    // Velvet Room
    public static final Identifier CHANGED_VELVET = new Identifier(PersonaMod.MOD_ID, "changed_velvet");

    public static void initialise() {
        ServerPlayNetworking.registerGlobalReceiver(PRESS_TARGET, ((server, player, handler, buf, responseSender) -> recieveTargetRequest(player)));
        ServerPlayNetworking.registerGlobalReceiver(CHANGE_SKILL, ((server, player, handler, buf, responseSender) -> recieveSkillChangeRequest(player, buf)));
        ServerPlayNetworking.registerGlobalReceiver(USE_SKILL, ((server, player, handler, buf, responseSender) -> recieveUseSkillRequest(player)));
        ServerPlayNetworking.registerGlobalReceiver(ASK_DATA, ((server, player, handler, buf, responseSender) -> recieveDataRequest(player, buf)));
        ServerPlayNetworking.registerGlobalReceiver(PERSONA_TOGGLE, ((server, player, handler, buf, responseSender) -> recievePersonaToggleRequest(player)));
    }

    public static void sendVelvetChange(ServerPlayerEntity player, boolean entry) {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeBoolean(entry);

        ServerPlayNetworking.send(player, CHANGED_VELVET, buf);
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

        if (player.isSneaking()) {
            target = player;
        }

        data.setTarget(target);
    }

    private static void recieveSkillChangeRequest(ServerPlayerEntity player, boolean next) {
        PlayerData data = ServerData.getPlayerState(player);

        if (data.findPersona().isEmpty()) return;

        Persona persona = data.findPersona().get();

        if (next) {
            persona.skills().selectNext();
        } else {
            persona.skills().selectPrevious();
        }

        ServerData.getServerState(player.getServer()).markDirty();
        PersonaMessages.syncData(player, player);
    }
    private static void recieveSkillChangeRequest(ServerPlayerEntity player, PacketByteBuf buf) {
        boolean next = buf.readBoolean();
        recieveSkillChangeRequest(player, next);
    }

    private static void recieveUseSkillRequest(ServerPlayerEntity player) {
        PersonaUtil.useSkill(player);
    }

    private static void recieveDataRequest(ServerPlayerEntity player, UUID uuid) {
        syncData(player, uuid);
    }
    private static void recieveDataRequest(ServerPlayerEntity player, PacketByteBuf buf) {
        UUID uuid = buf.readUuid();
        recieveDataRequest(player, uuid);
    }

    private static void recievePersonaToggleRequest(ServerPlayerEntity player) {
        PlayerData data = ServerData.getPlayerState(player);

        if (data.isPersonaRevealed()) {
            PersonaUtil.hide(player);
            return;
        }

        PersonaUtil.reveal(player);
    }
}