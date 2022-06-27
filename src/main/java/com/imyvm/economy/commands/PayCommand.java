package com.imyvm.economy.commands;

import com.imyvm.economy.EconomyMod;
import com.imyvm.economy.ImmediatelyTranslator;
import com.imyvm.economy.PlayerData;
import com.imyvm.economy.util.MoneyUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class PayCommand extends BaseCommand {
    @Override
    protected void registerCommand(CommandDispatcher<Object> dispatcher) {
        dispatcher.register(
            LiteralArgumentBuilder.literal("pay")
                .requires(source -> ((ServerCommandSource) source).isExecutedByPlayer())
                .requires(source -> Permissions.check((ServerCommandSource) source, EconomyMod.MOD_ID + ".pay"))
                .then(RequiredArgumentBuilder.argument("target", EntityArgumentType.player())
                    .then(RequiredArgumentBuilder.argument("amount", DoubleArgumentType.doubleArg(0))
                        .executes(ctx -> {
                            CommandContext<ServerCommandSource> context = this.castCommandContext(ctx);
                            ServerPlayerEntity source = context.getSource().getPlayer();

                            ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                            long amount = (long) (DoubleArgumentType.getDouble(context, "amount") * 100);

                            PlayerData sourceData = EconomyMod.data.getOrCreate(source.getUuid());
                            PlayerData targetData = EconomyMod.data.getOrCreate(target.getUuid());

                            if (amount > sourceData.getMoney()) {
                                source.sendMessage(ImmediatelyTranslator.translatable("commands.pay.failed.lack"));
                                return -1;
                            }

                            String formattedAmount = MoneyUtil.format(amount);
                            source.sendMessage(ImmediatelyTranslator.translatable("commands.pay.success.sender", formattedAmount, target.getName()));
                            target.sendMessage(ImmediatelyTranslator.translatable("commands.pay.success.receiver", formattedAmount, source.getName()));

                            sourceData.addMoney(-amount);
                            targetData.addMoney(amount);

                            return Command.SINGLE_SUCCESS;
                        }))));
    }
}
