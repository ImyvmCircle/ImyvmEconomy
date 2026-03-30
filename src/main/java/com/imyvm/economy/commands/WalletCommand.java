package com.imyvm.economy.commands;

import com.imyvm.economy.EconomyMod;
import com.imyvm.economy.PlayerData;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.imyvm.economy.Translator.tr;

public class WalletCommand extends BaseCommand {
    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        PlayerData data = EconomyMod.data.getOrCreate(player);

        String formattedAmount = data.getMoneyFormatted();
        Supplier<Component> textSupplier = () -> tr("commands.wallet.get", player.getName(), formattedAmount);
        context.getSource().sendSuccess(textSupplier, true);

        return Command.SINGLE_SUCCESS;
    }

    public int runAddMoney(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return this.updateOnesBalance(context, PlayerData::addMoney);
    }

    public int runTakeMoney(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return this.updateOnesBalance(context, (data, amount) -> data.addMoney(-amount));
    }

    public int runSetMoney(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return this.updateOnesBalance(context, PlayerData::setMoney);
    }

    private int updateOnesBalance(CommandContext<CommandSourceStack> context, BiConsumer<PlayerData, Long> modifier) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        long amount = (long) (DoubleArgumentType.getDouble(context, "amount") * 100);

        PlayerData data = EconomyMod.data.getOrCreate(player);
        modifier.accept(data, amount);

        // make sure balance >= 0
        data.setMoney(Long.max(0, data.getMoney()));

        String formattedAmount = data.getMoneyFormatted();
        Supplier<Component> textSupplier = () -> tr("commands.wallet.set", player.getName(), formattedAmount);
        context.getSource().sendSuccess(textSupplier, true);

        return Command.SINGLE_SUCCESS;
    }
}
