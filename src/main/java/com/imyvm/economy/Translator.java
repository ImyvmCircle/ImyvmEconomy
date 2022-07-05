package com.imyvm.economy;

import com.imyvm.hoki.i18n.HokiLanguage;
import com.imyvm.hoki.i18n.HokiTranslator;
import net.minecraft.text.Text;

import java.io.InputStream;

import static com.imyvm.economy.EconomyMod.CONFIG;

public class Translator extends HokiTranslator {
    private static HokiLanguage instance = createLanguage(CONFIG.LANGUAGE.getValue());

    static {
        CONFIG.LANGUAGE.changeEvents.register((option, oldValue, newValue) -> {
            instance = createLanguage(option.getValue());
        });
    }

    public static Text tr(String key, Object... args) {
        return HokiTranslator.translate(getLanguageInstance(), key, args);
    }

    public static HokiLanguage getLanguageInstance() {
        return instance;
    }

    private static HokiLanguage createLanguage(String languageId) {
        String path = HokiLanguage.getResourcePath(EconomyMod.MOD_ID, languageId);
        InputStream inputStream = Translator.class.getResourceAsStream(path);
        return HokiLanguage.create(inputStream);
    }
}
