package io.github.fareskingtube.hardcore_revived.persistent;

import com.mojang.authlib.GameProfile;
import io.github.fareskingtube.hardcore_revived.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeadManSwitchState extends SavedData {
    private final List<QueuedPlayer> queuedPlayers = new ArrayList<>();


    public static final Factory<DeadManSwitchState> TYPE = new Factory<>(
            DeadManSwitchState::new,
            DeadManSwitchState::createFromNbt,
            null
    );

    public QueuedPlayer getPlayer(UUID uuid) {
        return queuedPlayers.stream()
                .filter(p -> p.player().getId().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public QueuedPlayer getPlayer(BlockPos pos, ResourceKey<Level> world) {
        return queuedPlayers.stream()
                .filter(p -> p.pos().equals(pos) && p.world().equals(world))
                .findFirst()
                .orElse(null);
    }

    public boolean isQueued(UUID uuid) {
        return queuedPlayers.stream().anyMatch(p -> p.player().getId().equals(uuid));
    }

    public boolean isQueued(BlockPos pos, ResourceKey<Level> world) {
        return queuedPlayers.stream().anyMatch(p -> p.pos().equals(pos) && p.world().equals(world));
    }


    public void addQueuedPlayer(QueuedPlayer profile) {
        if (!isQueued(profile.player().getId())) {
            queuedPlayers.add(profile);
            setDirty();
        }
    }

    public void removeQueuedPlayer(UUID uuid, BlockPos pos, ResourceKey<Level> world) {
        if (queuedPlayers.removeIf(p -> p.player().getId().equals(uuid)
                && p.pos().equals(pos)
                && p.world().equals(world))) {
            setDirty();
        }
    }

    public void removeQueuedPlayer(BlockPos pos, ResourceKey<Level> world) {
        if (queuedPlayers.removeIf(p -> p.pos().equals(pos) && p.world().equals(world))) {
            setDirty();
        }
    }

    public List<QueuedPlayer> getQueuedPlayers() {
        return queuedPlayers;
    }


    private CompoundTag profileToNbt(GameProfile profile) {
        CompoundTag nbt = new CompoundTag();
        nbt.putUUID("Id", profile.getId());
        if (profile.getName() != null) {
            nbt.putString("Name", profile.getName());
        }
        return nbt;
    }

    private GameProfile profileFromNbt(CompoundTag nbt) {
        UUID id = nbt.getUUID("Id");
        String name = nbt.contains("Name") ? nbt.getString("Name") : "";
        return new GameProfile(id, name);
    }

    private CompoundTag queuedPlayerToNbt(QueuedPlayer queuedPlayer) {
        CompoundTag nbt = new CompoundTag();
        nbt.put("Profile", profileToNbt(queuedPlayer.player()));
        nbt.putLong("Pos", queuedPlayer.pos().asLong());
        nbt.putString("World", queuedPlayer.world().location().toString());
        return nbt;
    }

    private QueuedPlayer queuedPlayerFromNbt(CompoundTag nbt) {
        GameProfile profile = profileFromNbt(nbt.getCompound("Profile"));
        BlockPos pos = BlockPos.of(nbt.getLong("Pos"));
        ResourceLocation worldId = ResourceLocation.parse(nbt.getString("World"));
        ResourceKey<Level> world = ResourceKey.create(Registries.DIMENSION, worldId);
        return new QueuedPlayer(profile, pos, world);
    }

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        ListTag list = new ListTag();
        for (QueuedPlayer queuedPlayer : queuedPlayers) {
            list.add(queuedPlayerToNbt(queuedPlayer));
        }
        nbt.put("QueuedPlayers", list);
        return nbt;
    }

    public static DeadManSwitchState createFromNbt(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        DeadManSwitchState state = new DeadManSwitchState();
        ListTag list = nbt.getList("QueuedPlayers", Tag.TAG_COMPOUND);
        for (Tag element : list) {
            try {
                state.queuedPlayers.add(state.queuedPlayerFromNbt((CompoundTag) element));
            } catch (Exception err) {
                Constants.LOG.warn("Skipping corrupted queued player dead man switch entry", err);
            }
        }
        return state;
    }

    public static DeadManSwitchState get(MinecraftServer server) {
        DimensionDataStorage manager = server.overworld().getDataStorage();
        return manager.computeIfAbsent(TYPE, Constants.MOD_ID + "_queued_dead_man_switch");
    }
}
