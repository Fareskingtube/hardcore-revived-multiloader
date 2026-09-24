package io.github.fareskingtube.hardcore_revived.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.fareskingtube.hardcore_revived.block.custom.DeadManSwitchBlock;
import io.github.fareskingtube.hardcore_revived.block.entity.custom.DeadManSwitchBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class DeadManSwitchBlockEntityRenderer implements BlockEntityRenderer<DeadManSwitchBlockEntity> {
    public DeadManSwitchBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(DeadManSwitchBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers,
                       int light, int overlay) {
        Direction facing = entity.getBlockState().getValue(DeadManSwitchBlock.FACING);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = entity.getItem(0);

        if (stack.isEmpty()) return;

        matrices.pushPose();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        matrices.translate(0, -0.25, -0.05);
        matrices.scale(0.6f, 0.6f, 0.6f);
        matrices.mulPose(Axis.XN.rotationDegrees(50));

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
