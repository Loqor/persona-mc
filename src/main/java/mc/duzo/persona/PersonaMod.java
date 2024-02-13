package mc.duzo.persona;

import mc.duzo.persona.network.PersonaMessages;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonaMod implements ModInitializer {
	public static final String MOD_ID = "persona";
    public static final Logger LOGGER = LoggerFactory.getLogger("persona");

	@Override
	public void onInitialize() {
		PersonaMessages.initialise();
	}
}