package io.github.fareskingtube.hardcore_revived.network;

import com.mojang.authlib.GameProfile;
import io.github.fareskingtube.hardcore_revived.Constants;
import io.github.fareskingtube.hardcore_revived.component.ModDataComponentTypes;
import io.github.fareskingtube.hardcore_revived.item.custom.HardcoreHeartItem;
import io.github.fareskingtube.hardcore_revived.network.packet.PlayerSelectionPayloadC2S;
import io.github.fareskingtube.hardcore_revived.persistent.DeadPlayersState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

// Runs ON SERVER on receive
public class ServerBoundPackets {
    // Handles when a player is selected from a hardcore heart by a client
    public static void handlePlayerSelectionPayload(PlayerSelectionPayloadC2S playerSelectionPayloadC2S,
                                                    ServerPlayer player,
                                                    MinecraftServer server) {

        GameProfile trustedProfile = DeadPlayersState.get(server).getDeadPlayer(playerSelectionPayloadC2S.player().getId());

        if (trustedProfile == null) {
            Constants.LOG.warn("Player {} tried to select an invalid/non-dead profile: {}",
                    player.getName().getString(), player.getId());
            return;
        }

        InteractionHand targetHand = null;
        for (InteractionHand hand : InteractionHand.values()) {
            if (player.getItemInHand(hand).getItem() instanceof HardcoreHeartItem) {
                targetHand = hand;
                break;
            }
        }
        if (targetHand == null) return;

        ItemStack stack = player.getItemInHand(targetHand);

        stack.set(ModDataComponentTypes.SELECTED_PLAYER, trustedProfile);
        player.level().playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1f, 1f);
    }
}
