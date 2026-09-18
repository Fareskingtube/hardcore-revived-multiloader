package io.github.fareskingtube.hardcore_revived.config.helper;

import io.github.fareskingtube.hardcore_revived.Constants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ConfigTranslations {

    public static MutableComponent getGroupName(String id) {
        return Component.translatable("config." + Constants.MOD_ID + ".group." + id);
    }

    public static MutableComponent getGroupDescription(String id) {
        return Component.translatable("config." + Constants.MOD_ID + ".group.description." + id);
    }

    public static MutableComponent getOptionName(String id) {
        return Component.translatable("config." + Constants.MOD_ID + ".option." + id);
    }

    public static MutableComponent getOptionDescription(String id) {
        return Component.translatable("config." + Constants.MOD_ID + ".option.description." + id);
    }
}