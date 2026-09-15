package io.github.fareskingtube.hardcore_revived.network.packet;

import com.mojang.authlib.GameProfile;
import io.github.fareskingtube.hardcore_revived.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record DeadPlayersPayloadS2C(List<GameProfile> deadPlayers) implements CustomPacketPayload {
    public static final Type<DeadPlayersPayloadS2C> ID =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "dead_players"));

    public static final StreamCodec<FriendlyByteBuf, DeadPlayersPayloadS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.GAME_PROFILE.apply(ByteBufCodecs.list()),
            DeadPlayersPayloadS2C::deadPlayers,

            DeadPlayersPayloadS2C::new
    );


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
