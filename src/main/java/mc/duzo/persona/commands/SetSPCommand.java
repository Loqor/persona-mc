package mc.duzo.persona.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.data.PlayerData;
import mc.duzo.persona.data.ServerData;
import mc.duzo.persona.network.PersonaMessages;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class SetSPCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(PersonaMod.MOD_ID)
                .then(literal("sp").requires(source -> source.hasPermissionLevel(2))
                        .then(literal("set").then(argument("amount", DoubleArgumentType.doubleArg(0))
                                .executes(SetSPCommand::runCommand)))
        ));
    }

    private static int runCommand(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (player == null) return 0;

        PlayerData playerData = ServerData.getPlayerState(player);
        playerData.setSP(PlayerData.MAX_SP);
        ServerData.getServerState(context.getSource().getServer()).markDirty();
        PersonaMessages.syncData(player, player);

        return Command.SINGLE_SUCCESS;
    }
}
