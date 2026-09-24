package io.github.fareskingtube.hardcore_revived.datagen;

import io.github.fareskingtube.hardcore_revived.Constants;
import io.github.fareskingtube.hardcore_revived.block.ModBlocks;
import io.github.fareskingtube.hardcore_revived.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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

        // Blood Recipes
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLOOD_BLOCK)
                .requires(ModItems.BLOOD, 4)
                .unlockedBy(getHasName(ModItems.BLOOD), has(ModBlocks.BLOOD_BLOCK))
                .save(exporter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModItems.BLOOD, 4)
                .requires(ModBlocks.BLOOD_BLOCK)
                .unlockedBy(getHasName(ModBlocks.BLOOD_BLOCK), has(ModItems.BLOOD))
                .save(exporter);

        // Reset Hardcore Heart Recipe
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.HARDCORE_HEART)
                .requires(ModItems.HARDCORE_HEART)
                .unlockedBy(getHasName(ModItems.HARDCORE_HEART), has(ModItems.HARDCORE_HEART))
                .save(exporter);

        // Reset Binding Tablet Recipe
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BINDING_TABLET)
                .requires(ModItems.BINDING_TABLET)
                .unlockedBy(getHasName(ModItems.BINDING_TABLET), has(ModItems.BINDING_TABLET))
                .save(exporter, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "binding_tablet_reset"));

        Ingredient deepslateFamily = Ingredient.of(Items.DEEPSLATE, Items.COBBLED_DEEPSLATE, Items.POLISHED_DEEPSLATE,
                Items.DEEPSLATE_TILES, Items.DEEPSLATE_BRICKS, Items.CHISELED_DEEPSLATE);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.BINDING_TABLET)
                .pattern("DDD")
                .pattern("DBD")
                .pattern("DDD")
                .define('D', deepslateFamily)
                .define('B', ModItems.BLOOD)
                .unlockedBy(getHasName(ModItems.BLOOD), has(ModItems.BLOOD))
                .save(exporter);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.DEAD_MAN_SWITCH)
                .pattern(" R ")
                .pattern("RDR")
                .pattern("DDD")
                .define('D', deepslateFamily)
                .define('R', Items.REDSTONE)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(exporter);
    }
}
