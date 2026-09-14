package io.github.fareskingtube.hardcore_revived.item;

import io.github.fareskingtube.hardcore_revived.Constants;
import io.github.fareskingtube.hardcore_revived.item.custom.HardcoreHeartItem;
import io.github.fareskingtube.hardcore_revived.item.custom.HeartExtractorItem;
import io.github.fareskingtube.hardcore_revived.item.custom.HeartInjectorItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModItems {
    private static final Map<Item, ResourceLocation> ITEMS = new LinkedHashMap<>();

    public static final Item HARDCORE_HEART = registerItem("hardcore_heart", new HardcoreHeartItem(new Item.Properties().fireResistant().stacksTo(1)));
    public static final Item HEART_EXTRACTOR = registerItem("heart_extractor", new HeartExtractorItem(new Item.Properties().durability(1).stacksTo(1)));
    public static final Item HEART_INJECTOR = registerItem("heart_injector", new HeartInjectorItem(new Item.Properties().stacksTo(1)));
    public static final Item BUTCHER_KNIFE = registerItem("butcher_knife", new SwordItem(ModToolMaterials.BUTCHER_KNIFE_MATERIAL, new Item.Properties()
            .attributes(SwordItem.createAttributes(ModToolMaterials.BUTCHER_KNIFE_MATERIAL, 3, -1.8f))));
    // TODO: Add tooltip for how to get
    public static final Item BLOOD = registerItem("blood", new Item(new Item.Properties()));


    public static Item registerItem(String name, Item item) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name);
        ITEMS.put(item, id);
        return item;
    }

    public static void registerModItems(BiConsumer<Item, ResourceLocation> consumer) {
        Constants.LOG.info("Registering Mod Items for " + Constants.MOD_ID);
        ITEMS.forEach(consumer);
    }

}
