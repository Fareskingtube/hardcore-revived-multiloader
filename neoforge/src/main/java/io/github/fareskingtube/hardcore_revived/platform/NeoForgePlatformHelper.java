package io.github.fareskingtube.hardcore_revived.platform;

import io.github.fareskingtube.hardcore_revived.platform.services.IPlatformHelper;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.PacketDistributor;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    public CreativeModeTab.Builder creativeTabBuilder() {
        return CreativeModeTab.builder();
    }

    @Override
    public void sendPacketC2S(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    @Override
    public void sendPacketS2C(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }
}