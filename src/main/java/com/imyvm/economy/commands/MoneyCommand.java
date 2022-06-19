package com.imyvm.economy.commands;

import com.imyvm.economy.EconomyMod;
import com.imyvm.economy.ImmediatelyTranslator;
import com.imyvm.economy.PlayerData;
import com.imyvm.economy.util.MoneyUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.message.MessageType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class MoneyCommand extends BaseCommand {
    @Override
    protected void registerCommand(CommandDispatcher<Object> dispatcher) {
        dispatcher.register(
            LiteralArgumentBuilder.literal("money")
                .requires(source -> ((ServerCommandSource) source).isExecutedByPlayer())
                .executes(ctx -> {
                    ServerPlayerEntity player = this.castCommandContext(ctx).getSource().getPlayer();

                    PlayerData data = EconomyMod.data.getOrNew(player.getUuid());
                    String formattedAmount = MoneyUtil.format(data.getMoney());
                    player.sendMessage(ImmediatelyTranslator.translatable("commands.money", formattedAmount));

                    return Command.SINGLE_SUCCESS;
                }));
    }
}
