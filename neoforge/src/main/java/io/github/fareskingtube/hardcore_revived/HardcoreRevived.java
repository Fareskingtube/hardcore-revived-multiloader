package io.github.fareskingtube.hardcore_revived;


import io.github.fareskingtube.hardcore_revived.block.ModBlocks;
import io.github.fareskingtube.hardcore_revived.component.ModDataComponentTypes;
import io.github.fareskingtube.hardcore_revived.item.ModCreativeTabs;
import io.github.fareskingtube.hardcore_revived.item.ModItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class HardcoreRevived {

    public static IEventBus eventBus;

    public HardcoreRevived(IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        HardcoreRevived.eventBus = eventBus;

        Constants.LOG.info("Hello NeoForge world!");
        CommonClass.init();

        bind(Registries.BLOCK, ModBlocks::registerModBlocks);
        bind(Registries.ITEM, ModBlocks::registerModBlockItems);
        bind(Registries.ITEM, ModItems::registerModItems);
        bind(Registries.CREATIVE_MODE_TAB, ModCreativeTabs::registerModCreativeTabs);
        bind(Registries.DATA_COMPONENT_TYPE, ModDataComponentTypes::registerDataComponentTypes);
    }

    /**
     * Adapted from <a href="https://github.com/VazkiiMods/Botania">botania</a>
     **/
    private static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        eventBus.addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }
}