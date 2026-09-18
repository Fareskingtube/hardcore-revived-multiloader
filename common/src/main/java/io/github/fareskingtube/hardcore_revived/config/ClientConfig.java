package io.github.fareskingtube.hardcore_revived.config;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import io.github.fareskingtube.hardcore_revived.Constants;
import net.minecraft.resources.ResourceLocation;

public class ClientConfig {
    public static ConfigClassHandler<ClientConfig> HANDLER = ConfigClassHandler.createBuilder(ClientConfig.class)
            .id(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "client-config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve(Constants.MOD_ID + "-client.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();
    @SerialEntry(value = "applyDarkening", comment = "Whether the player selection screen has a dark background\nDefault: true")
    public boolean isApplyDarkening = true;
    @SerialEntry(value = "applyBlur", comment = "Whether the player selection screen has a blur on the background\nDefault: true")
    public boolean isApplyBlur = true;

    public static void save() {
        HANDLER.save();
    }

    public static void load() {
        HANDLER.load();
    }
}
