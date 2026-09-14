package io.github.fareskingtube.hardcore_revived.block;

import io.github.fareskingtube.hardcore_revived.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModBlocks {
    private static final Map<Block, ResourceLocation> BLOCKS = new LinkedHashMap<>();
    private static final Map<BlockItem, ResourceLocation> BLOCK_ITEMS = new LinkedHashMap<>();

    public static final Block BLOOD_BLOCK = registerBlock("blood_block", new Block(BlockBehaviour.Properties.of()
            .strength(0.6F, 2F)
            .sound(SoundType.STEM)
    ));

    public static Block registerBlock(String name, Block block) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name);
        BLOCKS.put(block, id);
        BLOCK_ITEMS.put(new BlockItem(block, new Item.Properties()), id);
        return block;
    }

    public static void registerModBlocks(BiConsumer<Block, ResourceLocation> consumer) {
        Constants.LOG.info("Registering Mod Blocks for " + Constants.MOD_ID);
        BLOCKS.forEach(consumer);
    }

    public static void registerModBlockItems(BiConsumer<Item, ResourceLocation> consumer) {
        Constants.LOG.info("Registering Mod Block Items for " + Constants.MOD_ID);
        BLOCK_ITEMS.forEach(consumer);
    }
}
