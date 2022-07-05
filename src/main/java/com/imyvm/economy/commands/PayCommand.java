package com.imyvm.economy.commands;

import com.imyvm.economy.EconomyMod;
import com.imyvm.economy.PlayerData;
import com.imyvm.economy.util.MoneyUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static com.imyvm.economy.Translator.tr;

public class PayCommand extends BaseCommand {
    @Override
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity source = context.getSource().getPlayer();

        ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
        long amount = (long) (DoubleArgumentType.getDouble(context, "amount") * 100);

        PlayerData sourceData = EconomyMod.data.getOrCreate(source);
        PlayerData targetData = EconomyMod.data.getOrCreate(target);

        if (amount > sourceData.getMoney()) {
            source.sendMessage(tr("commands.pay.failed.lack"));
            return -1;
        }

        String formattedAmount = MoneyUtil.format(amount);
        source.sendMessage(tr("commands.pay.success.sender", formattedAmount, target.getName()));
        target.sendMessage(tr("commands.pay.success.receiver", formattedAmount, source.getName()));

        sourceData.addMoney(-amount);
        targetData.addMoney(amount);

        return Command.SINGLE_SUCCESS;
    }
}
