package mc.duzo.persona;

import mc.duzo.persona.common.persona.PersonaRegistry;
import mc.duzo.persona.common.skill.SkillRegistry;
import mc.duzo.persona.data.PlayerData;
import mc.duzo.persona.data.ServerData;
import mc.duzo.persona.network.PersonaMessages;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonaMod implements ModInitializer {
	public static final String MOD_ID = "persona";
    public static final Logger LOGGER = LoggerFactory.getLogger("persona");

	@Override
	public void onInitialize() {
		SkillRegistry.init();
		PersonaRegistry.init();
		PersonaMessages.initialise();

		ServerPlayerEvents.AFTER_RESPAWN.register((old, player, alive) -> {
			PlayerData data = ServerData.getPlayerState(player);

			data.addSP(PlayerData.MAX_SP);
			ServerData.getServerState(player.getServer()).markDirty();
		});

		ServerEntityCombatEvents.AFTER_KILLED_OTHER_ENTITY.register((world, killer, victim) -> {
			if (!(killer instanceof ServerPlayerEntity)) return;

			PlayerData data = ServerData.getPlayerState((ServerPlayerEntity) killer);

			data.addSP((int) (victim.getMaxHealth() * 0.1f));
		});

		// Temporary for testing, remove soon.
		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();

			ServerData.getPlayerState(player).setPersona(PersonaRegistry.DEV, server);
		}));
	}
}