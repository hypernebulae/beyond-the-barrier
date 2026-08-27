package hypernebulae.btb;

import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public final class FarlandsCommand {

    private FarlandsCommand() {}

    public static void register() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) ->
                        registerCommands(dispatcher)
        );
    }

    private static void registerCommands(
            CommandDispatcher<CommandSourceStack> dispatcher
    ) {
        dispatcher.register(
                Commands.literal("farlands")
                        .then(
                                Commands.literal("enable")
                                        .executes(context -> {
                                        BeyondTheBarrier.value = true;
                                        context.getSource().sendSuccess(() -> Component.literal("Farlands enabled."), false);
                                        return 1;
                                })
                        )
                        .then(
                                Commands.literal("disable")
                                        .executes(context -> {
                                        BeyondTheBarrier.value = false;
                                        context.getSource().sendSuccess(() -> Component.literal("Farlands disabled."), false);
                                        return 1;
                                })
                        )
        );
    }
}