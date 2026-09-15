package io.github.fareskingtube.hardcore_revived.network;

import io.github.fareskingtube.hardcore_revived.Constants;
import io.github.fareskingtube.hardcore_revived.network.packet.DeadPlayersPayloadS2C;
import io.github.fareskingtube.hardcore_revived.network.packet.PlayerSelectionPayloadC2S;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ModPackets {
    // Payload from client to server (S2C) -> Serve to Client
    private static void registerServerBound(PayloadTypeRegistry<RegistryFriendlyByteBuf> registry) {
        registry.register(PlayerSelectionPayloadC2S.ID, PlayerSelectionPayloadC2S.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(PlayerSelectionPayloadC2S.ID, (payloadC2S, context) -> {
            ServerPlayer player = context.player();
            MinecraftServer server = context.server();
            ServerBoundPackets.handlePlayerSelectionPayload(payloadC2S, player, server);
        });
    }

    // Payload from server to client (C2S) -> Client to Server
    private static void registerClientBound(PayloadTypeRegistry<RegistryFriendlyByteBuf> registry) {
        registry.register(DeadPlayersPayloadS2C.ID, DeadPlayersPayloadS2C.STREAM_CODEC);

        ClientPlayNetworking.registerGlobalReceiver(DeadPlayersPayloadS2C.ID, (payloadS2C, context) -> {
            LocalPlayer player = context.player();
            ClientBoundPackets.handleDeadPlayersPayload(payloadS2C, player);
        });
    }

    public static void registerPackets() {
        Constants.LOG.info("Registering ServerBound Packets for " + Constants.MOD_ID);
        registerServerBound(PayloadTypeRegistry.playC2S());
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            Constants.LOG.info("Registering ClientBound Packets for " + Constants.MOD_ID);
            registerClientBound(PayloadTypeRegistry.playS2C());
        }
    }
}
