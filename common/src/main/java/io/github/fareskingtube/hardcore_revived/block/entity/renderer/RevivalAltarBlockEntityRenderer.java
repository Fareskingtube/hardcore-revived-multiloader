package io.github.fareskingtube.hardcore_revived.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.fareskingtube.hardcore_revived.block.entity.custom.RevivalAltarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class RevivalAltarBlockEntityRenderer implements BlockEntityRenderer<RevivalAltarBlockEntity> {
    public RevivalAltarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(RevivalAltarBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers,
                       int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = entity.getItem(0);
        if (stack.isEmpty()) return;

        matrices.pushPose();
        matrices.translate(0.5, 1.25, 0.5);
        matrices.scale(0.6f, 0.6f, 0.6f);
        matrices.mulPose(Axis.YP.rotationDegrees(entity.getRenderingRotation(entity, tickDelta)));

        itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.GUI,
                getLightLevel(entity.getLevel(), entity.getBlockPos()),
                OverlayTexture.NO_OVERLAY,
                matrices,
                vertexConsumers,
                entity.getLevel(),
                0
        );

        matrices.popPose();
    }

    private int getLightLevel(Level world, BlockPos pos) {
        int bLight = world.getLightEngine().getLayerListener(LightLayer.BLOCK).getLightValue(pos);
        int sLight = world.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(pos);
        return LightTexture.pack(bLight, sLight);
    }
}
