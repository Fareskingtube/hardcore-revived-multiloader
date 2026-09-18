package io.github.fareskingtube.hardcore_revived.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import io.github.fareskingtube.hardcore_revived.Constants;
import net.minecraft.resources.ResourceLocation;

public class CommonConfig {
    public static ConfigClassHandler<CommonConfig> HANDLER = ConfigClassHandler.createBuilder(CommonConfig.class)
            .id(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "common-config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve(Constants.MOD_ID + "-common.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();
    @SerialEntry(comment = "Hardcore Heart activation time in ticks\nDefault: 20 (1 Second)")
    public int heartActivationTime = 20;
    @SerialEntry(comment = "The amount of hp a player loses when killing another player\nDefault: 4 (2 Hearts)")
    public int killPenalty = 4;

    public static void save() {
        HANDLER.save();
    }

    public static void load() {
        HANDLER.load();
    }
}
