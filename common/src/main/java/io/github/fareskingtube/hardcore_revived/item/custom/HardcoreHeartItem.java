package io.github.fareskingtube.hardcore_revived.item.custom;

import com.mojang.authlib.GameProfile;
import io.github.fareskingtube.hardcore_revived.Constants;
import io.github.fareskingtube.hardcore_revived.component.ModDataComponentTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

// TODO: Make duration a config
//  Make HardcoreHeartItem spawn in ancient city chests
public class HardcoreHeartItem extends HoldActivateItem {

    public HardcoreHeartItem(Properties settings) {
        super(settings, getHeartActivationTime());
    }

    private static int getHeartActivationTime() {
        return Math.max(20, 1);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        MinecraftServer server = world.getServer();
        if (server != null && !world.isClientSide && user instanceof ServerPlayer player) {
            /* Gets the list of Players from Persistent Data and sends a Packet to the Client with the list of the dead players */
            // ServerPlayNetworking.send(player, new DeadPlayersPayloadS2C(DeadPlayersState.get(server).getDeadPlayers()));
            // TODO: Delete this after testing
            // GameProfile profile = new GameProfile(player.getUuid(), player.getNameForScoreboard());
            // ServerPlayNetworking.send(player, new DeadPlayersPayloadS2C(List.of(
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile,
            //         profile)));
        }
        return stack;
    }

    @Override
    public Component getName(ItemStack stack) {
        GameProfile selected = stack.get(ModDataComponentTypes.SELECTED_PLAYER);
        String selectedPlayerName = selected != null ? selected.getName() : null;

        MutableComponent name = Component.translatable("item." + Constants.MOD_ID + ".hardcore_heart");

        if (selectedPlayerName != null) {
            name = name.append(Component.literal(" (" + selectedPlayerName + ")").withStyle(ChatFormatting.GREEN));
        }

        return name;
    }
}
