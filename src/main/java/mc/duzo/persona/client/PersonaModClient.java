package mc.duzo.persona.client;

import mc.duzo.persona.data.PlayerData;
import net.fabricmc.api.ClientModInitializer;

public class PersonaModClient implements ClientModInitializer {
    public static PlayerData playerData = new PlayerData();

    @Override
    public void onInitializeClient() {

    }
}
