package io.github.fareskingtube.hardcore_revived;

import io.github.fareskingtube.hardcore_revived.block.ModBlocks;
import io.github.fareskingtube.hardcore_revived.component.ModDataComponentTypes;
import io.github.fareskingtube.hardcore_revived.item.ModCreativeTabs;
import io.github.fareskingtube.hardcore_revived.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class HardcoreRevived implements ModInitializer {

    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();

        bind(BuiltInRegistries.BLOCK, ModBlocks::registerModBlocks);
        bind(BuiltInRegistries.ITEM, ModBlocks::registerModBlockItems);
        bind(BuiltInRegistries.ITEM, ModItems::registerModItems);
        bind(BuiltInRegistries.CREATIVE_MODE_TAB, ModCreativeTabs::registerModCreativeTabs);
        bind(BuiltInRegistries.DATA_COMPONENT_TYPE, ModDataComponentTypes::registerDataComponentTypes);
    }

    /**
     * Adapted from <a href="https://github.com/VazkiiMods/Botania">botania</a>
     **/
    private static <T> void bind(Registry<T> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        source.accept((t, resourceLocation) -> Registry.register(registry, resourceLocation, t));
    }
}
