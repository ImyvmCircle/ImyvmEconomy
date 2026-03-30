package com.imyvm.economy.commands;

import com.imyvm.economy.EconomyMod;
import com.imyvm.economy.PlayerData;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.Supplier;

import static com.imyvm.economy.Translator.tr;

public class BalanceTopCommand extends BaseCommand {
    public final int MAX_TOP_PLAYERS = 16;

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        PriorityQueue<PlayerData> heap = new PriorityQueue<>(Comparator.comparing(PlayerData::getMoney));
        for (PlayerData player : EconomyMod.data.peekPlayers()) {
            heap.add(player);
            if (heap.size() > MAX_TOP_PLAYERS)
                heap.remove();
        }

        PlayerData[] topPlayers = heap.toArray(new PlayerData[0]);
        Arrays.sort(topPlayers, Comparator.comparing(PlayerData::getMoney).reversed());

        MutableComponent text = (MutableComponent) tr("commands.balance_top.header");
        int index = 0;
        for (PlayerData player : topPlayers) {
            ++index;
            text.append("\n").append(tr("commands.balance_top.item", index, player.getName(), player.getMoneyFormatted()));
        }
        Supplier<Component> textSupplier = () -> text;
        context.getSource().sendSuccess(textSupplier, false);

        return Command.SINGLE_SUCCESS;
    }
}
