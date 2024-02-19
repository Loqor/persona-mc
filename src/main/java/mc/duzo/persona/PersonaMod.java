package mc.duzo.persona;

import mc.duzo.persona.commands.Commands;
import mc.duzo.persona.common.PersonaSounds;
import mc.duzo.persona.common.persona.PersonaRegistry;
import mc.duzo.persona.common.skill.SkillRegistry;
import mc.duzo.persona.data.PlayerData;
import mc.duzo.persona.data.ServerData;
import mc.duzo.persona.network.PersonaMessages;
import mc.duzo.persona.util.VelvetUtil;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class PersonaMod implements ModInitializer {
	public static final String MOD_ID = "persona";
    public static final Logger LOGGER = LoggerFactory.getLogger("persona");
	public static final Random RANDOM = new Random();

	public static MinecraftServer SERVER;

	@Override
	public void onInitialize() {
		Register.initialize();

		PersonaSounds.init();
		SkillRegistry.init();
		PersonaRegistry.init();
		PersonaMessages.initialise();
		Commands.init();

		registerEvents();
	}

	private void registerEvents() {
		ServerPlayerEvents.AFTER_RESPAWN.register((old, player, alive) -> {
			PlayerData data = ServerData.getPlayerState(player);

			data.addSP(PlayerData.MAX_SP);
			ServerData.getServerState(player.getServer()).markDirty();
			PersonaMessages.syncData(player, player);
		});

		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, killer, victim) -> {
			if (!(killer instanceof ServerPlayerEntity)) return;

			PlayerData data = ServerData.getPlayerState((ServerPlayerEntity) killer);

			data.addSP((int) (victim.getMaxHealth() * 0.1f));
			ServerData.getServerState(killer.getServer()).markDirty();
			PersonaMessages.syncData((ServerPlayerEntity) killer, (ServerPlayerEntity) killer);
		});

		ServerEntityWorldChangeEvents.AFTER_ENTITY_CHANGE_WORLD.register((old, entity, origin, destination) -> {
			if (!(entity instanceof LivingEntity)) return;

			if (VelvetUtil.isInVelvetRoom((LivingEntity) entity)) {
				VelvetUtil.onEnter((LivingEntity) entity);
			}

			if (VelvetUtil.isVelvetRoom(origin)) {
				VelvetUtil.onExit((LivingEntity) entity);
			}
		});

		ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
			if (VelvetUtil.isInVelvetRoom(player)) {
				VelvetUtil.onEnter(player);
			}

			if (VelvetUtil.isVelvetRoom(origin)) {
				VelvetUtil.onExit(player);
			}
		});

		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();

			if (VelvetUtil.isInVelvetRoom(player)) {
				PersonaMessages.sendVelvetChange(player, true);
			}

			// Temporary for testing, remove soon.
			ServerData.getPlayerState(player).setPersona(PersonaRegistry.DEV, server);
		}));

		ServerLifecycleEvents.SERVER_STARTED.register(server -> PersonaMod.SERVER = server);
		ServerLifecycleEvents.SERVER_STOPPING.register(server -> PersonaMod.SERVER = null);
		ServerWorldEvents.UNLOAD.register((server, world) -> {
			if (world.getRegistryKey() == World.OVERWORLD) {
				PersonaMod.SERVER = null;
			}
		});

		ServerWorldEvents.LOAD.register((server, world) -> {
			if (world.getRegistryKey() == World.OVERWORLD) {
				PersonaMod.SERVER = server;
			}
		});
	}

	public static boolean hasServer() {
		return SERVER != null;
	}
}