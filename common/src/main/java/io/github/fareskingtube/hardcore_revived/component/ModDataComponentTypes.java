package io.github.fareskingtube.hardcore_revived.component;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import io.github.fareskingtube.hardcore_revived.Constants;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    private static final Map<DataComponentType<?>, ResourceLocation> DATA_COMPONENT_TYPES = new LinkedHashMap<>();

    public static final DataComponentType<Boolean> HAS_HEART = registerDataComponentType("has_heart",
            booleanBuilder -> booleanBuilder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static final DataComponentType<GameProfile> SELECTED_PLAYER = registerDataComponentType("selected_player",
            gameProfileBuilder -> gameProfileBuilder.persistent(ExtraCodecs.GAME_PROFILE).networkSynchronized(ByteBufCodecs.GAME_PROFILE));

    private static <T> DataComponentType<T> registerDataComponentType(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        DataComponentType<T> type = builderOperator.apply(DataComponentType.builder()).build();
        DATA_COMPONENT_TYPES.put(type, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        return type;
    }

    public static void registerDataComponentTypes(BiConsumer<DataComponentType<?>, ResourceLocation> consumer) {
        Constants.LOG.info("Registering Data Component Types for " + Constants.MOD_ID);
        DATA_COMPONENT_TYPES.forEach(consumer);
    }
}
