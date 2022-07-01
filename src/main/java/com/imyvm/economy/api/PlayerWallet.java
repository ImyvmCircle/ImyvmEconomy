package com.imyvm.economy.api;

import com.imyvm.economy.PlayerData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PlayerWallet {
    private final PlayerData data;

    PlayerWallet(PlayerData data) {
        this.data = data;
    }

    public boolean takeMoney(long amount) {
        assert amount >= 0 : "PlayerWallet.addMoney: the amount must not be negative value";
        if (data.getMoney() < amount)
            return false;
        data.addMoney(-amount);
        return true;
    }

    public long getMoney() {
        return data.getMoney();
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
}
