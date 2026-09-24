package io.github.fareskingtube.hardcore_revived.item.custom;

import com.mojang.authlib.GameProfile;
import io.github.fareskingtube.hardcore_revived.Constants;
import io.github.fareskingtube.hardcore_revived.component.ModDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BindingTabletItem extends Item {
    public BindingTabletItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        boolean selectedPlayer = stack.has(ModDataComponentTypes.SELECTED_PLAYER);

        if (!player.isShiftKeyDown()) {
            return InteractionResultHolder.pass(stack);
        }

        if (selectedPlayer) {
            return InteractionResultHolder.fail(stack);
        }

        stack.set(ModDataComponentTypes.SELECTED_PLAYER, new GameProfile(player.getUUID(), player.getName().getString()));
        level.playSound(player, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1F, 1F);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.has(ModDataComponentTypes.SELECTED_PLAYER) || super.isFoil(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        GameProfile selected = stack.get(ModDataComponentTypes.SELECTED_PLAYER);
        String selectedPlayerName = selected != null ? selected.getName() : null;

        MutableComponent name =
                Component.translatable("item." + Constants.MOD_ID + ".binding_tablet" + (selectedPlayerName != null ?
                        ".bound" : ""));

        if (selectedPlayerName != null) {
            name = name.append(Component.literal(" (" + selectedPlayerName + ")").withStyle(ChatFormatting.DARK_RED));
        }

        return name;
    }
}
