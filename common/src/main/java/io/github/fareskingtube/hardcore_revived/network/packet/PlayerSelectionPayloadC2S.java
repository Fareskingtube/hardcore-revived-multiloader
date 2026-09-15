package io.github.fareskingtube.hardcore_revived.network.packet;

import com.mojang.authlib.GameProfile;
import io.github.fareskingtube.hardcore_revived.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record PlayerSelectionPayloadC2S(GameProfile player) implements CustomPacketPayload {
    public static final Type<PlayerSelectionPayloadC2S> ID =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "player_select"));

    public static final StreamCodec<FriendlyByteBuf, PlayerSelectionPayloadC2S> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.GAME_PROFILE,
            PlayerSelectionPayloadC2S::player,

            PlayerSelectionPayloadC2S::new
    );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
