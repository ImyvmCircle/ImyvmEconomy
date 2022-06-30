package com.imyvm.economy;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;

import java.io.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Database {
    public final String DATABASE_FILENAME = "imyvm_economy.db";

    private Map<UUID, PlayerData> data;

    public PlayerData getOrCreate(PlayerEntity player) {
        return data.computeIfAbsent(player.getUuid(), (u) -> new PlayerData(player.getEntityName()));
    }

    public void save() throws IOException {
        File file = this.getDatabasePath().toFile();

        try (DataOutputStream stream = new DataOutputStream(new FileOutputStream(file))) {
            stream.writeInt(data.size());
            for (var entry : data.entrySet()) {
                stream.writeLong(entry.getKey().getLeastSignificantBits());
                stream.writeLong(entry.getKey().getMostSignificantBits());
                entry.getValue().serialize(stream);
            }
        }
    }

    public void load() throws IOException {
        File file = this.getDatabasePath().toFile();

        if (!file.exists()) {
            data = new ConcurrentHashMap<>();
            return;
        }

        try (DataInputStream stream = new DataInputStream(new FileInputStream(file))) {
            int size = stream.readInt();
            data = new ConcurrentHashMap<>(size);
            for (int i = 0; i < size; i++) {
                UUID uuid = new UUID(stream.readLong(), stream.readLong());
                // dummy player name, it will be overridden in `deserialize`
                PlayerData player = new PlayerData("DUMMY");
                player.deserialize(stream);
                data.put(uuid, player);
            }
        }
    }

    public Collection<PlayerData> peekPlayers() {
        return this.data.values();
    }

    private Path getDatabasePath() {
        return FabricLoader.getInstance().getGameDir().resolve("world").resolve(DATABASE_FILENAME);
    }
}
