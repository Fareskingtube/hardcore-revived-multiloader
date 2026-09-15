package io.github.fareskingtube.hardcore_revived.datagen;

import io.github.fareskingtube.hardcore_revived.block.ModBlocks;
import io.github.fareskingtube.hardcore_revived.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void buildRecipes(RecipeOutput exporter) {
        // Revival Altar Recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.REVIVAL_ALTAR)
                .pattern("BGB")
                .pattern("BDB")
                .pattern("DDD")
                .define('B', ModBlocks.BLOOD_BLOCK)
                .define('G', Blocks.GOLD_BLOCK)
                .define('D', Blocks.DEEPSLATE_TILES)
                .unlockedBy(getHasName(ModBlocks.REVIVAL_ALTAR), has(ModBlocks.BLOOD_BLOCK))
                .save(exporter);
        // Heart Injector recipe (temporary)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEART_INJECTOR)
                .pattern("I ")
                .pattern(" G")
                .define('I', Items.IRON_INGOT)
                .define('G', Items.GLASS_BOTTLE)
                .unlockedBy(getHasName(Items.GLASS_BOTTLE), has(ModItems.HEART_INJECTOR))
                .save(exporter);
        // Heart Extractor recipe (temporary)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HEART_EXTRACTOR)
                .pattern("S S")
                .pattern("SSS")
                .pattern(" S ")
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(ModItems.HEART_EXTRACTOR))
                .save(exporter);

        // Butcher Knife Recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BUTCHER_KNIFE)
                .pattern("I ")
                .pattern(" S")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(ModItems.BUTCHER_KNIFE))
                .save(exporter);

        // Blood Recipe
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLOOD_BLOCK)
                .requires(ModItems.BLOOD, 4)
                .unlockedBy(getHasName(ModItems.BLOOD), has(ModBlocks.BLOOD_BLOCK))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.BLOOD, 4)
                .requires(ModBlocks.BLOOD_BLOCK)
                .unlockedBy(getHasName(ModBlocks.BLOOD_BLOCK), has(ModItems.BLOOD))
                .save(exporter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.HARDCORE_HEART)
                .requires(ModItems.HARDCORE_HEART)
                .unlockedBy(getHasName(ModItems.HARDCORE_HEART), has(ModItems.HARDCORE_HEART))
                .save(exporter);
    }
}
