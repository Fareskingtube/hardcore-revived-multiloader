package io.github.fareskingtube.hardcore_revived.datagen;

import io.github.fareskingtube.hardcore_revived.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookup) {
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                // .add(ModBlocks.REVIVAL_ALTAR)
                .add(ModBlocks.BLOOD_BLOCK);
        // getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
        //         .add(ModBlocks.REVIVAL_ALTAR);
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.BLOOD_BLOCK);
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_HOE)
                .add(ModBlocks.BLOOD_BLOCK);
    }
}
