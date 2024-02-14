package mc.duzo.persona.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import mc.duzo.persona.PersonaMod;
import mc.duzo.persona.util.VelvetUtil;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class EnterRoomCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal(PersonaMod.MOD_ID)
                .then(literal("velvet").requires(source -> source.hasPermissionLevel(2))
                        .then(literal("enter").executes(EnterRoomCommand::runCommand)))
        );
    }

    private static int runCommand(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        if (player == null) return 0;

        VelvetUtil.sendToRoom(player);

        return Command.SINGLE_SUCCESS;
    }
}
