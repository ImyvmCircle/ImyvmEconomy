package com.imyvm.economy;

import com.imyvm.economy.interfaces.Serializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerData implements Serializable {
    private String name;
    private long money;

    public PlayerData(String name) {
        this.name = name;
        this.money = EconomyMod.config.getUserDefaultBalance();
    }

    @Override
    public void serialize(DataOutputStream stream) throws IOException {
        stream.writeUTF(name);
        stream.writeLong(money);
    }

    @Override
    public void deserialize(DataInputStream stream) throws IOException {
        this.name = stream.readUTF();
        this.money = stream.readLong();
    }

    public long addMoney(long amount) {
        this.money += amount;
        return this.money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}
