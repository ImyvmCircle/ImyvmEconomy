package com.imyvm.economy.commands;

import com.imyvm.economy.EconomyMod;
import com.imyvm.economy.ImmediatelyTranslator;
import com.imyvm.economy.PlayerData;
import com.imyvm.economy.util.MoneyUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class BalanceCommand extends BaseCommand {
    @Override
    protected void registerCommand(CommandDispatcher<Object> dispatcher) {
        LiteralCommandNode<Object> node = dispatcher.register(
            LiteralArgumentBuilder.literal("balance")
                .requires(source -> ((ServerCommandSource) source).isExecutedByPlayer())
                .requires(source -> Permissions.check((ServerCommandSource) source, EconomyMod.MOD_ID + ".balance"))
                .executes(ctx -> {
                    ServerPlayerEntity player = this.castCommandContext(ctx).getSource().getPlayer();

                    PlayerData data = EconomyMod.data.getOrCreate(player);
                    String formattedAmount = MoneyUtil.format(data.getMoney());
                    player.sendMessage(ImmediatelyTranslator.translatable("commands.balance", formattedAmount));

                    return Command.SINGLE_SUCCESS;
                }));

        dispatcher.register(LiteralArgumentBuilder.literal("bal").redirect(node));
        dispatcher.register(LiteralArgumentBuilder.literal("money").redirect(node));
    }
}
