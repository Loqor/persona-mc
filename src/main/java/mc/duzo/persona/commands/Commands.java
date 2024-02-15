package mc.duzo.persona.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

public class Commands {
    public static void init() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> registerCommands(dispatcher)));
    }

    private static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        EnterRoomCommand.register(dispatcher);
        SetSPCommand.register(dispatcher);
    }
}
