package com.imyvm.economy.commands;

import com.imyvm.economy.EconomyMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.Commands.argument;

public class CommandRegistry {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        LiteralCommandNode<CommandSourceStack> node = dispatcher.register(
            literal("balance")
                .requires(CommandSourceStack::isPlayer)
                .requires(source -> true)
                .executes(new BalanceCommand()));

        dispatcher.register(literal("bal").redirect(node));
        dispatcher.register(literal("money").redirect(node));

        dispatcher.register(
            literal("pay")
                .requires(CommandSourceStack::isPlayer)
                .requires(source -> true)
                .then(argument("target", EntityArgument.player())
                    .then(argument("amount", DoubleArgumentType.doubleArg(0))
                        .executes(new PayCommand()))));

        WalletCommand walletCommand = new WalletCommand();
        dispatcher.register(
            literal("wallet")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(literal("add")
                    .then(argument("player", EntityArgument.player())
                        .then(argument("amount", DoubleArgumentType.doubleArg(0))
                            .executes(walletCommand::runAddMoney))))
                .then(literal("take")
                    .then(argument("player", EntityArgument.player())
                        .then(argument("amount", DoubleArgumentType.doubleArg(0))
                            .executes(walletCommand::runTakeMoney))))
                .then(literal("set")
                    .then(argument("player", EntityArgument.player())
                        .then(argument("amount", DoubleArgumentType.doubleArg(0))
                            .executes(walletCommand::runSetMoney))))
                .then(literal("get")
                    .then(argument("player", EntityArgument.player())
                        .executes(walletCommand))));

        dispatcher.register(
            literal("balance_top")
                .requires(source -> true)
                .executes(new BalanceTopCommand()));
    }
}
