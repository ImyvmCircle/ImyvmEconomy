package com.imyvm.economy.api;

import com.imyvm.economy.PlayerData;
import com.imyvm.economy.util.MoneyUtil;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.imyvm.economy.Translator.tr;

public class PlayerWallet {
    private static final Dynamic3CommandExceptionType INSUFFICIENT_BALANCE_EXCEPTION = new Dynamic3CommandExceptionType((amount, goods, balance) -> tr("api.buy_goods.failed.insufficient_balance", MoneyUtil.format((Long) amount), goods, balance));

    private final Player player;
    private final PlayerData data;

    PlayerWallet(Player player, PlayerData data) {
        this.player = player;
        this.data = data;
    }

    public long getMoney() {
        return data.getMoney();
    }

    public String getMoneyFormatted() {
        return data.getMoneyFormatted();
    }

    public boolean takeMoney(long amount) {
        assert amount >= 0 : "PlayerWallet.addMoney: the amount must not be negative value";
        if (data.getMoney() < amount)
            return false;
        data.addMoney(-amount);
        return true;
    }

    public void addMoney(long amount) {
        assert amount >= 0 : "PlayerWallet.addMoney: the amount must not be negative value";
        data.addMoney(amount);
    }

    public boolean payTo(@NotNull PlayerWallet target, long amount) {
        Objects.requireNonNull(target);
        if (!this.takeMoney(amount))
            return false;
        target.addMoney(amount);
        return true;
    }

    public boolean buyGoodsWithNotification(long amount, @NotNull Component goods) {
        Objects.requireNonNull(goods);
        boolean result = this.takeMoney(amount);

        if (result)
            this.player.sendSystemMessage(tr("api.buy_goods.success", MoneyUtil.format(amount), goods, this.getMoneyFormatted()));
        else
            this.player.sendSystemMessage(tr("api.buy_goods.failed.insufficient_balance", MoneyUtil.format(amount), goods, this.getMoneyFormatted()));

        return result;
    }

    public void buyGoodsWithNotificationInCommand(long amount, @NotNull Component goods) throws CommandSyntaxException {
        Objects.requireNonNull(goods);
        if (!this.takeMoney(amount))
            throw INSUFFICIENT_BALANCE_EXCEPTION.create(amount, goods, this.getMoneyFormatted());

        this.player.sendSystemMessage(tr("api.buy_goods.success", MoneyUtil.format(amount), goods, this.getMoneyFormatted()));
    }
}
