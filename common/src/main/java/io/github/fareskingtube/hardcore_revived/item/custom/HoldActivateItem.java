package io.github.fareskingtube.hardcore_revived.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;


public abstract class HoldActivateItem extends Item {
    private final int HOLD_DURATION;

    public HoldActivateItem(Properties settings, int duration) {
        super(settings);
        HOLD_DURATION = duration;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return HOLD_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public abstract ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user);

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        if (getUseDuration(itemStack, user) <= 0) {
            finishUsingItem(itemStack, world, user);
        } else {
            user.startUsingItem(hand);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }
}
