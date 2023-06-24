package com.imyvm.economy;

import com.imyvm.economy.api.TradeTypeEnum;
import com.imyvm.economy.interfaces.Serializable;
import com.imyvm.economy.util.MoneyUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static com.imyvm.economy.EconomyMod.CONFIG;
import static java.lang.Math.abs;

public class PlayerData implements Serializable {
    private String name;
    private long money;

    public PlayerData(String name) {
        this.name = name;
        this.money = CONFIG.USER_DEFAULT_BALANCE.getValue();
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

    public long addMoney(long amount, TradeTypeEnum.TradeTypeExtension type) {
        PlayerData admin = EconomyMod.data.getOrCreate(CONFIG.ADMIN_UUID.getValue(), CONFIG.ADMIN_NAME.getValue());
        Double tax = type.getTax();
        long amountFin;
        if (amount > 0) {
            amountFin = amount - (long) (amount * tax);
        } else {
            amountFin = amount + (long) (amount * tax);
        }
        this.money += amountFin;
        admin.money += abs((long) (tax * amount));
        return this.money;
    }

    public String getMoneyFormatted() {
        return MoneyUtil.format(this.getMoney());
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
