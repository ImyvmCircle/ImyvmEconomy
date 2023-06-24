package com.imyvm.economy.api;

import com.imyvm.economy.PlayerData;
import com.imyvm.economy.util.MoneyUtil;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.imyvm.economy.Translator.tr;

public class PlayerWallet {
    private static final Dynamic3CommandExceptionType INSUFFICIENT_BALANCE_EXCEPTION = new Dynamic3CommandExceptionType((amount, goods, balance) -> tr("api.buy_goods.failed.insufficient_balance", MoneyUtil.format((Long) amount), goods, balance));

    private final PlayerEntity player;
    private final PlayerData data;

    PlayerWallet(PlayerEntity player, PlayerData data) {
        this.player = player;
        this.data = data;
    }

    public long getMoney() {
        return data.getMoney();
    }

    public String getMoneyFormatted() {
        return data.getMoneyFormatted();
    }

    public boolean takeMoney(long amount, TradeTypeEnum.TradeTypeExtension tradeType) {
        assert amount >= 0 : "PlayerWallet.addMoney: the amount must not be negative value";
        if (data.getMoney() < amount)
            return false;
        data.addMoney(-amount, tradeType);
        return true;
    }

    public void addMoney(long amount, TradeTypeEnum.TradeTypeExtension tradeType) {
        assert amount >= 0 : "PlayerWallet.addMoney: the amount must not be negative value";
        data.addMoney(amount, tradeType);
    }

    public boolean payTo(@NotNull PlayerWallet target, long amount, TradeTypeEnum.TradeType tradeType) {
        Objects.requireNonNull(target);
        if (!this.takeMoney(amount, tradeType))
            return false;
        target.addMoney(amount, tradeType);
        return true;
    }

    public boolean buyGoodsWithNotification(long amount, @NotNull Text goods, TradeTypeEnum.TradeType tradeType) {
        Objects.requireNonNull(goods);
        boolean result = this.takeMoney(amount, tradeType);

        if (result)
            this.player.sendMessage(tr("api.buy_goods.success", MoneyUtil.format(amount), goods, this.getMoneyFormatted()));
        else
            this.player.sendMessage(tr("api.buy_goods.failed.insufficient_balance", MoneyUtil.format(amount), goods, this.getMoneyFormatted()));

        return result;
    }

    public void buyGoodsWithNotificationInCommand(long amount, @NotNull Text goods, TradeTypeEnum.TradeTypeExtension tradeType) throws CommandSyntaxException {
        Objects.requireNonNull(goods);
        if (!this.takeMoney(amount, tradeType))
            throw INSUFFICIENT_BALANCE_EXCEPTION.create(amount, goods, this.getMoneyFormatted());

        this.player.sendMessage(tr("api.buy_goods.success", MoneyUtil.format(amount), goods, this.getMoneyFormatted()));
    }
}
