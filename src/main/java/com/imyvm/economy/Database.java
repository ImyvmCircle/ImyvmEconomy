package com.imyvm.economy;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Database {
    public final String DATABASE_FILENAME = "imyvm_economy.db";

    private Map<UUID, PlayerData> data;

    public PlayerData getOrCreate(UUID uuid) {
        return data.computeIfAbsent(uuid, (u) -> new PlayerData());
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
            data = new HashMap<>();
            return;
        }

        try (DataInputStream stream = new DataInputStream(new FileInputStream(file))) {
            int size = stream.readInt();
            data = new HashMap<>(size);
            for (int i = 0; i < size; i++) {
                UUID uuid = new UUID(stream.readLong(), stream.readLong());
                PlayerData player = new PlayerData();
                player.deserialize(stream);
                data.put(uuid, player);
            }
        }
    }

    private Path getDatabasePath() {
        return FabricLoader.getInstance().getGameDir().resolve("world").resolve(DATABASE_FILENAME);
    }
}
