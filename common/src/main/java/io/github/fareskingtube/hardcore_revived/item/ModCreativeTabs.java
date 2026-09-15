package io.github.fareskingtube.hardcore_revived.item;

import io.github.fareskingtube.hardcore_revived.Constants;
import io.github.fareskingtube.hardcore_revived.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class ModCreativeTabs {
    private static final Map<CreativeModeTab, ResourceLocation> TABS = new LinkedHashMap<>();

    public static final CreativeModeTab HARDCORE_REVIVED_GROUP = registerTab("hardcore_revived_group",
            Services.PLATFORM.creativeTabBuilder()
                    .title(Component.translatable("itemGroup." + Constants.MOD_ID + ".hardcore_revived_group"))
                    .icon(ModItems.HARDCORE_HEART::getDefaultInstance)
                    .displayItems((displayContext, entries) -> BuiltInRegistries.ITEM.keySet()
                            .stream()
                            .filter(key -> key.getNamespace().equals(Constants.MOD_ID))
                            .map(BuiltInRegistries.ITEM::getOptional)
                            .map(Optional::orElseThrow)
                            .forEach(entries::accept))
                    .build());

    private static <T extends CreativeModeTab> T registerTab(String name, T tab) {
        TABS.put(tab, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        return tab;
    }

    public static void registerModCreativeTabs(BiConsumer<CreativeModeTab, ResourceLocation> consumer) {
        Constants.LOG.info("Registering Mod Creative Tabs for " + Constants.MOD_ID);
        TABS.forEach(consumer);
    }
}
