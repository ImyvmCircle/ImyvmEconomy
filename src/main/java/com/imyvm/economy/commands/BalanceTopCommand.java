package com.imyvm.economy.commands;

import com.imyvm.economy.EconomyMod;
import com.imyvm.economy.ImmediatelyTranslator;
import com.imyvm.economy.PlayerData;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class BalanceTopCommand extends BaseCommand {
    public final int MAX_TOP_PLAYERS = 16;

    @Override
    protected void registerCommand(CommandDispatcher<Object> dispatcher) {
        dispatcher.register(
            LiteralArgumentBuilder.literal("balance_top")
                .requires(source -> Permissions.check((ServerCommandSource) source, EconomyMod.MOD_ID + ".balance_top", true))
                .executes(ctx -> {
                    PriorityQueue<PlayerData> heap = new PriorityQueue<>(Comparator.comparing(PlayerData::getMoney));
                    for (PlayerData player : EconomyMod.data.peekPlayers()) {
                        heap.add(player);
                        if (heap.size() > MAX_TOP_PLAYERS)
                            heap.remove();
                    }

                    PlayerData[] topPlayers = heap.toArray(new PlayerData[0]);
                    Arrays.sort(topPlayers, Comparator.comparing(PlayerData::getMoney).reversed());

                    MutableText text = (MutableText) ImmediatelyTranslator.translatable("commands.balance_top.header");
                    int index = 0;
                    for (PlayerData player : topPlayers) {
                        ++index;
                        text.append("\n").append(ImmediatelyTranslator.translatable("commands.balance_top.item", index, player.getName(), player.getMoneyFormatted()));
                    }
                    this.castCommandContext(ctx).getSource().sendFeedback(text, false);

                    return Command.SINGLE_SUCCESS;
                }));
    }
}
