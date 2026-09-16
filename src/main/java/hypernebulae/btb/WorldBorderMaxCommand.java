package hypernebulae.btb;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class WorldBorderMaxCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> registerCommands(dispatcher));
    }

    private static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("wbmax")
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();

                            source.getServer().getCommands().performPrefixedCommand(
                                    source,
                                    "worldborder set 4294967296"
                            );
                            return 1;
                        })
        );
    }

    public static void execute(ServerLevel world) {
        MinecraftServer server = world.getServer();
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withLevel(world).withSuppressedOutput(), "wbmax");
    }
}
