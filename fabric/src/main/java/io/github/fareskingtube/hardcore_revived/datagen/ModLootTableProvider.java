package io.github.fareskingtube.hardcore_revived.datagen;

import io.github.fareskingtube.hardcore_revived.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        dropSelf(ModBlocks.REVIVAL_ALTAR);
        dropSelf(ModBlocks.DEAD_MAN_SWITCH);
        dropSelf(ModBlocks.BLOOD_BLOCK);
    }
}
