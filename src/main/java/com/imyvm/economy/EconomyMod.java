package com.imyvm.economy;

import com.imyvm.economy.commands.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

public class EconomyMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Economy");
	public static ModConfig config;
	public static Database data;

	@Override
	public void onInitialize() {
		registerCommands();
		registerEvents();
		registerLazyTick();

		initializeData();

		ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
			LOGGER.info("The economy database is saving");
			try {
				data.save();
			} catch (Exception e) {
				LOGGER.error("Failed to save data: " + e);
				throw new RuntimeException("Failed to save data", e);
			}
		});

		LOGGER.info("Imyvm Economy initialized");
	}

	public void initializeData() {
		try {
			config = new ModConfig();
			data = new Database();
			data.load();
		} catch (Exception e) {
			LOGGER.error("Failed to initialize data: " + e);
			throw new RuntimeException("Failed to initialize data", e);
		}
	}

	public void registerEvents() {
	}

	public void registerCommands() {
		new MoneyCommand();
		new PayCommand();
		new WalletCommand();
	}

	public void registerLazyTick() {
		final int PERIOD = 20 - 1;
		IntUnaryOperator periodIncrease = v -> v == PERIOD ? 0 : v + 1;

		AtomicInteger i = new AtomicInteger();
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			int value = i.getAndUpdate(periodIncrease);
		});
	}
}
