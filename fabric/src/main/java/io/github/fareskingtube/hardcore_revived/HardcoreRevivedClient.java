package io.github.fareskingtube.hardcore_revived;

import io.github.fareskingtube.hardcore_revived.block.entity.ModBlockEntities;
import io.github.fareskingtube.hardcore_revived.block.entity.renderer.RevivalAltarBlockEntityRenderer;
import io.github.fareskingtube.hardcore_revived.util.ModModelPredicates;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class HardcoreRevivedClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModModelPredicates.registerModelPredicates();
        BlockEntityRenderers.register(ModBlockEntities.REVIVAL_ALTAR_BE, RevivalAltarBlockEntityRenderer::new);
    }
}
