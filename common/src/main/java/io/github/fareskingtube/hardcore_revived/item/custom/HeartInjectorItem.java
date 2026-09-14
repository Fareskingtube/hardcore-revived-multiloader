package io.github.fareskingtube.hardcore_revived.item.custom;

import io.github.fareskingtube.hardcore_revived.Constants;
import io.github.fareskingtube.hardcore_revived.component.ModDataComponentTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HeartInjectorItem extends HoldActivateItem {
    public HeartInjectorItem(Properties settings) {
        super(settings, 60);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        Boolean hasHeart = stack.get(ModDataComponentTypes.HAS_HEART);
        AttributeInstance maxHealth = user.getAttribute(Attributes.MAX_HEALTH);
        if (!world.isClientSide() && hasHeart != null && hasHeart && maxHealth != null) {
            maxHealth.setBaseValue(maxHealth.getValue() + 2);
            stack.set(ModDataComponentTypes.HAS_HEART, false);
        }
        world.playSound(null, user.blockPosition(), SoundEvents.WITCH_DRINK, SoundSource.PLAYERS, 1F, 1F);
        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        Boolean hasHeart = itemStack.get(ModDataComponentTypes.HAS_HEART);

        if (hasHeart != null && hasHeart) {
            if (getUseDuration(itemStack, user) <= 0) {
                finishUsingItem(itemStack, world, user);
            } else {
                user.startUsingItem(hand);
            }
            return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
        }
        return InteractionResultHolder.fail(itemStack);
    }

    //    Changing the name of the Heart Importer based on the HAS_HEART Data Component
    @Override
    public Component getName(ItemStack stack) {
        Boolean hasHeart = stack.get(ModDataComponentTypes.HAS_HEART);
        if (hasHeart != null && hasHeart) {
            return Component.translatable("item." + Constants.MOD_ID + ".heart_injector.filled");
        }
        return Component.translatable("item." + Constants.MOD_ID + ".heart_injector.empty");
    }
}
