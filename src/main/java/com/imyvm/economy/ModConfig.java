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
    public final Option<Double> TAX_RATE = new Option<>(
            "core.tax_rate",
            0.0,
            "The tax rate players pay per transaction",
            Config::getDouble
    );

    @ConfigOption
    public final Option<String> ADMIN_UUID = new Option<>(
            "core.adminUuid",
            "71076404-af4c-4670-bbe3-69b8104104f9",
            "The uuid of admin",
            Config::getString
    );

    @ConfigOption
    public final Option<String> ADMIN_NAME = new Option<>(
            "core.adminName",
            "Yilya",
            "The name of admin",
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
