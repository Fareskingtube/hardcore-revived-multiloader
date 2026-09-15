package io.github.fareskingtube.hardcore_revived.network;

import io.github.fareskingtube.hardcore_revived.Constants;
import io.github.fareskingtube.hardcore_revived.network.packet.DeadPlayersPayloadS2C;
import io.github.fareskingtube.hardcore_revived.network.packet.PlayerSelectionPayloadC2S;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Constants.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModPackets {
    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        Constants.LOG.info("Registering ServerBound Packets for " + Constants.MOD_ID);
        registrar.playToServer(PlayerSelectionPayloadC2S.ID, PlayerSelectionPayloadC2S.STREAM_CODEC, (payloadC2S, context) -> {
            Player player = context.player();
            if (player instanceof ServerPlayer serverPlayer) {
                MinecraftServer server = serverPlayer.getServer();
                ServerBoundPackets.handlePlayerSelectionPayload(payloadC2S, serverPlayer, server);
            }
        });

        Constants.LOG.info("Registering ClientBound Packets for " + Constants.MOD_ID);
        registrar.playToClient(DeadPlayersPayloadS2C.ID, DeadPlayersPayloadS2C.STREAM_CODEC, (payloadS2C, context) -> {
            Player player = context.player();
            if (player instanceof LocalPlayer localPlayer) {
                ClientBoundPackets.handleDeadPlayersPayload(payloadS2C, localPlayer);
            }
        });
    }
}
