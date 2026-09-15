package io.github.fareskingtube.hardcore_revived.persistent;

import com.mojang.authlib.GameProfile;
import io.github.fareskingtube.hardcore_revived.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeadPlayersState extends SavedData {
    private final List<GameProfile> deadPlayers = new ArrayList<>();


    public static final Factory<DeadPlayersState> TYPE = new Factory<>(
            DeadPlayersState::new,
            DeadPlayersState::createFromNbt,
            null
    );


    public boolean isDead(UUID uuid) {
        return deadPlayers.stream().anyMatch(p -> p.getId().equals(uuid));
    }

    public GameProfile getDeadPlayer(UUID uuid) {
        return deadPlayers.stream()
                .filter(p -> p.getId().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public void addDeadPlayer(GameProfile profile) {
        if (!isDead(profile.getId())) {
            deadPlayers.add(profile);
            setDirty();
        }
    }

    public void removeDeadPlayer(UUID uuid) {
        if (deadPlayers.removeIf(p -> p.getId().equals(uuid))) {
            setDirty();
        }
    }

    public List<GameProfile> getDeadPlayers() {
        return deadPlayers;
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

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        ListTag list = new ListTag();
        for (GameProfile profile : deadPlayers) {
            list.add(profileToNbt(profile));
        }
        nbt.put("DeadPlayers", list);
        return nbt;
    }

    public static DeadPlayersState createFromNbt(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        DeadPlayersState state = new DeadPlayersState();
        ListTag list = nbt.getList("DeadPlayers", Tag.TAG_COMPOUND);
        for (Tag element : list) {
            try {
                state.deadPlayers.add(state.profileFromNbt((CompoundTag) element));
            } catch (Exception err) {
                Constants.LOG.warn("Skipping corrupted dead player entry", err);
            }
        }
        return state;
    }

    public static DeadPlayersState get(MinecraftServer server) {
        DimensionDataStorage manager = server.overworld().getDataStorage();
        return manager.computeIfAbsent(TYPE, Constants.MOD_ID + "_dead_players");
    }
}
