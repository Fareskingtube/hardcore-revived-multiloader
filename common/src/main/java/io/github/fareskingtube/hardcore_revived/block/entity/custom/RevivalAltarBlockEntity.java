package io.github.fareskingtube.hardcore_revived.block.entity.custom;

import com.mojang.authlib.GameProfile;
import io.github.fareskingtube.hardcore_revived.block.entity.ImplementedInventory;
import io.github.fareskingtube.hardcore_revived.block.entity.ModBlockEntities;
import io.github.fareskingtube.hardcore_revived.block.entity.TickableBlockEntity;
import io.github.fareskingtube.hardcore_revived.component.ModDataComponentTypes;
import io.github.fareskingtube.hardcore_revived.item.ModItems;
import io.github.fareskingtube.hardcore_revived.persistent.DeadPlayersState;
import io.github.fareskingtube.hardcore_revived.persistent.QueuedPlayer;
import io.github.fareskingtube.hardcore_revived.persistent.RevivalQueueState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class RevivalAltarBlockEntity extends BlockEntity implements ImplementedInventory, TickableBlockEntity {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    private boolean isMultiblock = false;
    private int ticks = 0;
    private float prevRotation = 0;
    private float rotation = 0;

    public RevivalAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.REVIVAL_ALTAR_BE, pos, state);
    }


    @Override
    public void tick() {
        if (this.getLevel() == null) return;
        this.prevRotation = this.rotation;
        this.rotation = this.rotation + 5f % 360f;

        Level world = this.getLevel();

        if (this.ticks++ % 20 == 0) {
            boolean currentIsMultiblock = isMultiblock(world, this.getBlockPos());
            if (this.isMultiblock && !currentIsMultiblock) {
                world.playSound(null, this.worldPosition, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1F, 1F);
                this.removeQueuedPlayer();
            }
            this.isMultiblock = currentIsMultiblock;
            if (this.isMultiblock && this.getItem(0) != null) {
                this.queuePlayerRevival();
            }
        }

        if (this.isMultiblock) {
            if (world.isClientSide()) {
                spawnParticles(world);
                spawnCherryBlossomParticle(world);
            } else {
                applyEffectsToNearbyPlayers(world);
            }
        }
    }

    // Claude made most of both of those particle spawning methods (SCGF: Small Calude Generated Function)
    private void spawnParticles(Level world) {
        if (this.ticks % 4 != 0) return; // every 2 ticks for density

        RandomSource random = world.getRandom();
        BlockPos pos = this.getBlockPos();

        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 0.5;
        double centerZ = pos.getZ() + 0.5;

        for (int i = 0; i < 8; i++) {
            double x = centerX + (random.nextDouble() * 11 - 5.5);
            double y = centerY + 0.5 + random.nextDouble() * 6;
            double z = centerZ + 0.5 + (random.nextDouble() * 11 - 5.5);

            double dx = centerX - x;
            double dy = centerY - y;
            double dz = centerZ - z;

            double length = Math.sqrt(dx * dx + dy * dy + dz * dz);

            double speed = 0.05;
            double vx = (dx / length) * speed;
            double vy = (dy / length) * speed;
            double vz = (dz / length) * speed;


            world.addParticle(
                    ParticleTypes.ASH,
                    x, y, z,
                    vx,
                    vy,
                    vz
            );

        }
    }

    private void spawnCherryBlossomParticle(Level world) {
        // Only runs every few ticks to avoid overwhelming the client
        if (this.ticks % 20 != 0) return;

        RandomSource random = world.getRandom();
        BlockPos pos = this.getBlockPos();

        // Spawns particles in a radius around the center block
        for (int i = 0; i < 3; i++) {
            double x = pos.getX() + 0.5 + (random.nextDouble() * 11 - 5.5);
            double y = pos.getY() + 5 + random.nextDouble() * 2;
            double z = pos.getZ() + 0.5 + (random.nextDouble() * 11 - 5.5);

            world.addParticle(
                    ParticleTypes.CHERRY_LEAVES, // swap for any ParticleTypes constant you like
                    x, y, z,
                    (random.nextDouble() - 0.5) * 0.05, -0.05, (random.nextDouble() - 0.5) * 0.05
            );
        }
    }

    private void applyEffectsToNearbyPlayers(Level world) {
        // Only apply effects once per second (every 20 ticks)
        if (this.ticks % 80 != 0) return;

        BlockPos pos = this.getBlockPos();
        double radius = 5.5;


        // Get all players within radius
        List<Player> players = world.getEntitiesOfClass(
                Player.class,
                new AABB(pos).inflate(radius),
                player -> true
        );

        players.sort(Comparator.comparingDouble(p ->
                p.distanceToSqr(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5)
        ));

        for (Player player : players) {
            player.addEffect(new MobEffectInstance(
                    MobEffects.REGENERATION, // swap for any StatusEffects constant
                    120,   // duration in ticks
                    0,    // amplifier (0 = level I, 1 = level II, etc.)
                    true, // ambient (true makes particles more transparent, like beacons)
                    true,  // show particles
                    true   // show icon in HUD
            ));
        }
    }

    // TODO: Make revive player start a craft
    public void revivePlayer() {
        Level world = this.getLevel();

        if (world == null) return;

        if (!this.isMultiblock(world, this.getBlockPos())) return;

        MinecraftServer server = world.getServer();

        if (server == null) return;

        RevivalQueueState state = RevivalQueueState.get(server);

        QueuedPlayer queuedPlayer = state.getPlayer(this.worldPosition, world.dimension());

        GameProfile profile = this.getItem(0).get(ModDataComponentTypes.SELECTED_PLAYER);

        if (profile == null) return;

        if (!queuedPlayer.player().getId().equals(profile.getId())) return;

        ServerPlayer player = server.getPlayerList().getPlayer(queuedPlayer.player().getId());

        if (player == null) return;

        boolean isHeart = this.inventory.getFirst().getItem() == ModItems.HARDCORE_HEART;

        if (isHeart && player instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR) {
            setItem(0, ItemStack.EMPTY);
            setChanged();
            serverPlayer.teleportTo(
                    (ServerLevel) world,
                    worldPosition.getX() + 0.5,
                    worldPosition.getY() + 1,
                    worldPosition.getZ() + 0.5,
                    180,
                    0
            );
            serverPlayer.setGameMode(GameType.SURVIVAL);
            world.playSound(null, worldPosition, SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1F, 1F);
            if (world instanceof ServerLevel serverWorld) {
                serverWorld.sendParticles(
                        ParticleTypes.TOTEM_OF_UNDYING,
                        serverPlayer.getX(), serverPlayer.getY() + 1, serverPlayer.getZ(),
                        30,
                        0.5, 0.5, 0.5,
                        0.1
                );
            }
            DeadPlayersState.get(server).removeDeadPlayer(player.getUUID());
            state.removeQueuedPlayer(player.getUUID(), this.getBlockPos(), world.dimension());
        }
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    public boolean isMultiblock(Level world, BlockPos pos) {
        // boolean validateMultiblock = ModMultiblocks.REVIVAL_ALTAR_MULTIBLOCK.validate(world, pos.below(), Rotation.NONE);

        // if (!this.isMultiblock && validateMultiblock) {
        //     world.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1F, 1F);
        // }

        // return validateMultiblock;
        return false;
    }

    public boolean getIsMultiblock() {
        return isMultiblock;
    }

    public void queuePlayerRevival() {
        if (!this.isMultiblock) return;

        if (this.level == null) return;
        MinecraftServer server = this.level.getServer();
        if (server == null) return;
        ItemStack stack = this.getItem(0);
        GameProfile player = stack.get(ModDataComponentTypes.SELECTED_PLAYER);
        if (player == null) return;

        RevivalQueueState.get(server).addQueuedPlayer(new QueuedPlayer(player, this.getBlockPos(), this.level.dimension()));

        revivePlayer();
    }

    public void removeQueuedPlayer() {
        if (this.level == null) return;
        MinecraftServer server = this.level.getServer();
        if (server == null) return;

        RevivalQueueState.get(server).removeQueuedPlayer(this.getBlockPos(), level.dimension());
    }

    public float getRotation() {
        return rotation;
    }

    public float getPrevRotation() {
        return prevRotation;
    }

    public float getRenderingRotation(RevivalAltarBlockEntity entity, float tickDelta) {
        float delta = entity.getRotation() - entity.getPrevRotation();
        if (delta < -180) delta += 360; // Triggers every rotation
        if (delta > 180) delta -= 360;
        return entity.getPrevRotation() + delta * tickDelta;
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

        nbt.putInt("ticks", this.ticks);
        nbt.putBoolean("isMultiBlock", this.isMultiblock);
        ContainerHelper.saveAllItems(nbt, inventory, registryLookup);
    }

    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        super.loadAdditional(nbt, registryLookup);

        this.ticks = nbt.getInt("ticks");
        this.isMultiblock = nbt.getBoolean("isMultiblock");
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
