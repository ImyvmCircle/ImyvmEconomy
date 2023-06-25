package com.imyvm.economy.commands;

import com.imyvm.economy.EconomyMod;
import com.imyvm.economy.PlayerData;
import com.imyvm.economy.api.TradeTypeEnum;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.imyvm.economy.Translator.tr;

public class WalletCommand extends BaseCommand {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
        PlayerData data = EconomyMod.data.getOrCreate(player);

        String formattedAmount = data.getMoneyFormatted();
        Supplier<Text> textSupplier = () -> tr("commands.wallet.get", player.getName(), formattedAmount);
        context.getSource().sendFeedback(textSupplier, true);

        return Command.SINGLE_SUCCESS;
    }

    public int runAddMoney(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return this.updateOnesBalance(context, (data, amount) -> data.addMoney(amount, TradeTypeEnum.TradeType.DUTY_FREE));
    }

    public int runTakeMoney(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return this.updateOnesBalance(context, (data, amount) -> data.addMoney(-amount, TradeTypeEnum.TradeType.DUTY_FREE));
    }

    public int runSetMoney(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        return this.updateOnesBalance(context, PlayerData::setMoney);
    }

    private int updateOnesBalance(CommandContext<ServerCommandSource> context, BiConsumer<PlayerData, Long> modifier) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
        long amount = (long) (DoubleArgumentType.getDouble(context, "amount") * 100);

        PlayerData data = EconomyMod.data.getOrCreate(player);
        modifier.accept(data, amount);

        // make sure balance >= 0
        data.setMoney(Long.max(0, data.getMoney()));

        String formattedAmount = data.getMoneyFormatted();
        Supplier<Text> textSupplier = () ->  tr("commands.wallet.set", player.getName(), formattedAmount);
        context.getSource().sendFeedback(textSupplier, true);

        return Command.SINGLE_SUCCESS;
    }
}
