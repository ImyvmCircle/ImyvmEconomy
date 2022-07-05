package com.imyvm.economy.commands;

import com.imyvm.economy.EconomyMod;
import com.imyvm.economy.PlayerData;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static com.imyvm.economy.Translator.tr;

public class BalanceCommand extends BaseCommand {
    @Override
    public int run(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();

        PlayerData data = EconomyMod.data.getOrCreate(player);
        String formattedAmount = data.getMoneyFormatted();
        player.sendMessage(tr("commands.balance", formattedAmount));

        return Command.SINGLE_SUCCESS;
    }
}
