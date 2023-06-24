package com.imyvm.economy;

import com.imyvm.economy.api.TradeTypeEnum;
import com.imyvm.economy.commands.CommandRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

public class EconomyMod implements ModInitializer {
	public static final String MOD_ID = "imyvm_economy";
	public static final Logger LOGGER = LoggerFactory.getLogger("Economy");
	public static final ModConfig CONFIG = new ModConfig();
	public static Database data = new Database();

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(CommandRegistry::register);
		registerEvents();
		registerLazyTick();

		CONFIG.loadAndSave();
		initializeData();
		TradeTypeEnum.TradeType.PAY.setTax(CONFIG.TAX_RATE.getValue());
		TradeTypeEnum.TradeType.DUTYFREE.setTax(0.0);

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
			data.load();
		} catch (Exception e) {
			LOGGER.error("Failed to initialize data: " + e);
			throw new RuntimeException("Failed to initialize data", e);
		}
	}

	public void registerEvents() {
	}

	public void registerLazyTick() {
		final int PERIOD = 20 - 1;
		IntUnaryOperator periodIncrease = v -> v == PERIOD ? 0 : v + 1;

		AtomicInteger i = new AtomicInteger();
		AtomicInteger ticks = new AtomicInteger();
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			int value = i.getAndUpdate(periodIncrease);
			int ticksNow = ticks.getAndIncrement();

			// periodically save the database
			// 0x7FF -> 2048 ticks -> 1.7 minutes
			if ((ticksNow & 0x7FF) == 0) {
				try {
					data.save();
				} catch (IOException e) {
					LOGGER.error("Error occurred when saving database", e);
				}
			}
		});
	}
}
