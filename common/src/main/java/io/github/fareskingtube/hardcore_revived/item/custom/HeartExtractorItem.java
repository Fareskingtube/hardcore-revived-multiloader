package io.github.fareskingtube.hardcore_revived.item.custom;


import io.github.fareskingtube.hardcore_revived.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HeartExtractorItem extends HoldActivateItem {
    public HeartExtractorItem(Properties settings) {
        super(settings, 50);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (!world.isClientSide()) {
            AttributeInstance maxHealth = user.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealth != null && maxHealth.getValue() - 2 > 0) {
                maxHealth.setBaseValue(maxHealth.getValue() - 2);
                stack.hurtAndBreak(1, ((ServerLevel) world), ((ServerPlayer) user),
                        item -> user.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                ((ServerPlayer) user).addItem(ModItems.HARDCORE_HEART.getDefaultInstance());
            }
        }
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        AttributeInstance maxHealth = user.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null && maxHealth.getValue() - 2 > 0) {
            if (getUseDuration(itemStack, user) <= 0) {
                finishUsingItem(itemStack, world, user);
            } else {
                user.startUsingItem(hand);
            }
            return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
        }
        world.playSound(null, user.blockPosition(), SoundEvents.LAVA_EXTINGUISH, SoundSource.PLAYERS, 1F, 1F);
        return InteractionResultHolder.fail(itemStack);
    }
}
