package com.imyvm.economy.api;

import com.imyvm.economy.Database;
import com.imyvm.economy.EconomyMod;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DatabaseApi {
    private final Database database;

    private static final DatabaseApi instance = new DatabaseApi(EconomyMod.data);

    private DatabaseApi(Database database) {
        this.database = database;
    }

    @NotNull
    public PlayerWallet getPlayer(@NotNull PlayerEntity player) {
        Objects.requireNonNull(player);
        return new PlayerWallet(this.database.getOrCreate(player));
    }
}
