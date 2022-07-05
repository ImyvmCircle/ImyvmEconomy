package com.imyvm.economy;

import com.imyvm.hoki.config.ConfigOption;
import com.imyvm.hoki.config.HokiConfig;
import com.imyvm.hoki.config.Option;
import com.typesafe.config.Config;

public class ModConfig extends HokiConfig {
    public static final String CONFIG_FILENAME = "imyvm_economy.conf";

    public ModConfig() {
        super(CONFIG_FILENAME);
    }

    @ConfigOption
    public final Option<String> LANGUAGE = new Option<>(
        "core.language",
        "en_us",
        "the display language of Essential mod",
        Config::getString
    );

    @ConfigOption
    public final Option<Long> USER_DEFAULT_BALANCE = new Option<>(
        "user_default.balance",
        88 * 100L,
        "The user's default balance (in cents)",
        Config::getLong
    );
}
