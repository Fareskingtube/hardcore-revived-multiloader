package io.github.fareskingtube.hardcore_revived.block.entity.custom;

import com.mojang.authlib.GameProfile;
import io.github.fareskingtube.hardcore_revived.block.custom.DeadManSwitchBlock;
import io.github.fareskingtube.hardcore_revived.block.entity.ImplementedInventory;
import io.github.fareskingtube.hardcore_revived.block.entity.ModBlockEntities;
import io.github.fareskingtube.hardcore_revived.component.ModDataComponentTypes;
import io.github.fareskingtube.hardcore_revived.persistent.DeadManSwitchState;
import io.github.fareskingtube.hardcore_revived.persistent.QueuedPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DeadManSwitchBlockEntity extends BlockEntity implements ImplementedInventory {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

    public DeadManSwitchBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DEAD_MAN_SWITCH_BE, pos, blockState);
    }

    public void pulse() {
        Level level = this.getLevel();
        if (level == null) return;
        MinecraftServer server = level.getServer();
        if (server == null) return;
        BlockPos pos = this.getBlockPos();

        GameProfile inventoryPlayer = this.getItem(0).get(ModDataComponentTypes.SELECTED_PLAYER);
        if (inventoryPlayer == null) return;

        QueuedPlayer queuedPlayer = DeadManSwitchState.get(server).getPlayer(pos, level.dimension());

        if (queuedPlayer.player().getId() != inventoryPlayer.getId()) return;

        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof DeadManSwitchBlock deadManSwitchBlock) {
            deadManSwitchBlock.pulse(state, level, pos);
        }
    }

    public void queuePlayerDeath() {
        if (this.level == null) return;
        MinecraftServer server = this.level.getServer();
        if (server == null) return;
        ItemStack stack = this.getItem(0);
        GameProfile player = stack.get(ModDataComponentTypes.SELECTED_PLAYER);
        if (player == null) return;

        DeadManSwitchState.get(server).addQueuedPlayer(new QueuedPlayer(player, this.getBlockPos(), this.level.dimension()));
    }

    public void removeQueuedPlayer() {
        if (this.level == null) return;
        MinecraftServer server = this.level.getServer();
        if (server == null) return;

        DeadManSwitchState.get(server).removeQueuedPlayer(this.getBlockPos(), level.dimension());
    }


    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }


    @Override
    public void setChanged() {
        super.setChanged();

        if (level != null) {
            level.blockEntityChanged(worldPosition);

            if (!level.isClientSide) {
                level.sendBlockUpdated(
                        worldPosition,
                        getBlockState(),
                        getBlockState(),
                        Block.UPDATE_ALL
                );
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.saveAdditional(nbt, registryLookup);

        ContainerHelper.saveAllItems(nbt, inventory, registryLookup);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);

        inventory.set(0, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, inventory, registryLookup);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }
}
