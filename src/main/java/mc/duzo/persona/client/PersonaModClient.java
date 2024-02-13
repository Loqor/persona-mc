package mc.duzo.persona.client;

import mc.duzo.persona.client.network.PersonaClientMessages;
import net.fabricmc.api.ClientModInitializer;

public class PersonaModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PersonaClientMessages.initialise();
    }
}
