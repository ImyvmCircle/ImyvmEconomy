package com.imyvm.economy.commands;

import com.imyvm.economy.EconomyMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class CommandRegistry {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess, CommandManager.RegistrationEnvironment environment) {
        LiteralCommandNode<ServerCommandSource> node = dispatcher.register(
            literal("balance")
                .requires(ServerCommandSource::isExecutedByPlayer)
                .requires(source -> Permissions.check(source, EconomyMod.MOD_ID + ".balance", true))
                .executes(new BalanceCommand()));

        dispatcher.register(literal("bal").redirect(node));
        dispatcher.register(literal("money").redirect(node));

        dispatcher.register(
            literal("pay")
                .requires(ServerCommandSource::isExecutedByPlayer)
                .requires(source -> Permissions.check(source, EconomyMod.MOD_ID + ".pay", true))
                .then(argument("target", EntityArgumentType.player())
                    .then(argument("amount", DoubleArgumentType.doubleArg(0))
                        .executes(new PayCommand()))));

        WalletCommand walletCommand = new WalletCommand();
        dispatcher.register(
            literal("wallet")
                .requires(source -> Permissions.check(source, EconomyMod.MOD_ID + ".wallet", 2))
                .then(literal("add")
                    .then(argument("player", EntityArgumentType.player())
                        .then(argument("amount", DoubleArgumentType.doubleArg(0))
                            .executes(walletCommand::runAddMoney)))
                .then(literal("take")
                    .then(argument("player", EntityArgumentType.player())
                        .then(argument("amount", DoubleArgumentType.doubleArg(0))
                            .executes(walletCommand::runTakeMoney))))
                .then(literal("set")
                    .then(argument("player", EntityArgumentType.player())
                        .then(argument("amount", DoubleArgumentType.doubleArg(0))
                            .executes(walletCommand::runSetMoney)))
                .then(literal("get")
                    .then(argument("player", EntityArgumentType.player())
                        .executes(walletCommand))))));

        dispatcher.register(
            literal("balance_top")
                .requires(source -> Permissions.check(source, EconomyMod.MOD_ID + ".balance_top", true))
                .executes(new BalanceTopCommand()));
    }
}
