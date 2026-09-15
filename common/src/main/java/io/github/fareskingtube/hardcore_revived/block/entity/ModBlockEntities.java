package io.github.fareskingtube.hardcore_revived.block.entity;

import io.github.fareskingtube.hardcore_revived.Constants;
import io.github.fareskingtube.hardcore_revived.block.ModBlocks;
import io.github.fareskingtube.hardcore_revived.block.entity.custom.RevivalAltarBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModBlockEntities {
    private static final Map<BlockEntityType<?>, ResourceLocation> BLOCK_ENTITIES = new LinkedHashMap<>();

    public static final BlockEntityType<RevivalAltarBlockEntity> REVIVAL_ALTAR_BE = registerBlockEntity(
            "revival_be",
            BlockEntityType.Builder.of(RevivalAltarBlockEntity::new, ModBlocks.REVIVAL_ALTAR).build(null)
    );


    public static <T extends BlockEntityType<?>> T registerBlockEntity(String name, T type) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name);
        BLOCK_ENTITIES.put(type, id);
        return type;
    }

    public static void registerBlockEntities(BiConsumer<BlockEntityType<?>, ResourceLocation> consumer) {
        Constants.LOG.info("Registering Block Entities for " + Constants.MOD_ID);
        BLOCK_ENTITIES.forEach(consumer);
    }
}
