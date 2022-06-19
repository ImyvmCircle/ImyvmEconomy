package com.imyvm.economy;

import com.imyvm.economy.interfaces.Serializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerData implements Serializable {
    private long money;

    @Override
    public void serialize(DataOutputStream stream) throws IOException {
        stream.writeLong(money);
    }

    @Override
    public void deserialize(DataInputStream stream) throws IOException {
        this.money = stream.readLong();
    }

    public long addMoney(long amount) {
        this.money += amount;
        return this.money;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}
