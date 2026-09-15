package io.github.fareskingtube.hardcore_revived.network;

import com.mojang.authlib.GameProfile;
import io.github.fareskingtube.hardcore_revived.gui.screen.custom.PlayerSelectorScreen;
import io.github.fareskingtube.hardcore_revived.network.packet.DeadPlayersPayloadS2C;
import io.github.fareskingtube.hardcore_revived.network.packet.PlayerSelectionPayloadC2S;
import io.github.fareskingtube.hardcore_revived.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

// Runs ON CLIENT on receive
public class ClientBoundPackets {
    public static void handleDeadPlayersPayload(DeadPlayersPayloadS2C deadPlayersPayloadS2C, LocalPlayer player) {
        Minecraft.getInstance().setScreen(
                new PlayerSelectorScreen(deadPlayersPayloadS2C.deadPlayers(),
                        new GameProfile(player.getUUID(), player.getScoreboardName()),
                        chosen -> Services.PLATFORM.sendPacketC2S(new PlayerSelectionPayloadC2S(chosen))
                )
        );
    }
}
