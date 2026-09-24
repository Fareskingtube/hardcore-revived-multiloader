package io.github.fareskingtube.hardcore_revived;

import io.github.fareskingtube.hardcore_revived.block.entity.ModBlockEntities;
import io.github.fareskingtube.hardcore_revived.block.entity.renderer.DeadManSwitchBlockEntityRenderer;
import io.github.fareskingtube.hardcore_revived.block.entity.renderer.RevivalAltarBlockEntityRenderer;
import io.github.fareskingtube.hardcore_revived.config.ClientConfig;
import io.github.fareskingtube.hardcore_revived.util.ModModelPredicates;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class HardcoreRevivedClient {
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(
                () -> {
                    // Loading default client config to disk
                    ClientConfig.load();

                    ModModelPredicates.registerModelPredicates();
                    BlockEntityRenderers.register(ModBlockEntities.REVIVAL_ALTAR_BE, RevivalAltarBlockEntityRenderer::new);
                    BlockEntityRenderers.register(ModBlockEntities.DEAD_MAN_SWITCH_BE, DeadManSwitchBlockEntityRenderer::new);
                }
        );
    }
}
