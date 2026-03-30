package com.imyvm.economy.commands;

import com.imyvm.economy.EconomyMod;
import com.imyvm.economy.PlayerData;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import static com.imyvm.economy.Translator.tr;

public class BalanceCommand extends BaseCommand {
    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();

        PlayerData data = EconomyMod.data.getOrCreate(player);
        String formattedAmount = data.getMoneyFormatted();
        player.sendSystemMessage(tr("commands.balance", formattedAmount));

        return Command.SINGLE_SUCCESS;
    }
}
