package io.github.fareskingtube.hardcore_revived.platform;

import io.github.fareskingtube.hardcore_revived.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public CreativeModeTab.Builder creativeTabBuilder() {
        return FabricItemGroup.builder();
    }

    @Override
    public void sendPacketC2S(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    @Override
    public void sendPacketS2C(ServerPlayer player, CustomPacketPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }
}
