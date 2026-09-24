package io.github.fareskingtube.hardcore_revived;

import io.github.fareskingtube.hardcore_revived.block.entity.ModBlockEntities;
import io.github.fareskingtube.hardcore_revived.block.entity.renderer.DeadManSwitchBlockEntityRenderer;
import io.github.fareskingtube.hardcore_revived.block.entity.renderer.RevivalAltarBlockEntityRenderer;
import io.github.fareskingtube.hardcore_revived.config.ClientConfig;
import io.github.fareskingtube.hardcore_revived.util.ModModelPredicates;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class HardcoreRevivedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Loading default client config to disk
        ClientConfig.load();

        ModModelPredicates.registerModelPredicates();
        BlockEntityRenderers.register(ModBlockEntities.REVIVAL_ALTAR_BE, RevivalAltarBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.DEAD_MAN_SWITCH_BE, DeadManSwitchBlockEntityRenderer::new);
    }
}
