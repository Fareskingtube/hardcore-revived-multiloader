package io.github.fareskingtube.hardcore_revived.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;

import java.util.*;
import java.util.concurrent.CompletableFuture;

// SCGF (Small Claude Generated Function)
// But I now understand how it works
public final class PlayerProfileTextureCache {
    private static final Map<UUID, GameProfile> FILLED = new HashMap<>();
    private static final Set<UUID> PENDING = new HashSet<>();

    /**
     * Call every frame from renderWidget(). Never blocks.
     */
    public static GameProfile resolve(GameProfile bareProfile) {
        UUID id = bareProfile.getId();

        GameProfile filled = FILLED.get(id);
        if (filled != null) {
            return filled; // has textures - draw the real skin this frame
        }

        if (PENDING.add(id)) { // returns false if already in-flight
            CompletableFuture
                    .supplyAsync(() -> Minecraft.getInstance()
                                    .getMinecraftSessionService() // verify exact accessor via autocomplete
                                    .fetchProfile(id, false), // verify exact signature - may differ in your build
                            Util.backgroundExecutor()) // network work off the render thread
                    .thenAcceptAsync(result -> {
                        if (result != null && result.profile() != null) {
                            FILLED.put(id, result.profile());
                        }
                        PENDING.remove(id);
                    }, Minecraft.getInstance()) // hop back to client thread to touch the cache
                    .exceptionallyAsync(ex -> {
                        PENDING.remove(id);
                        return null;
                    }, Minecraft.getInstance());
        }

        return bareProfile; // this frame: unfilled, draws default - next frame picks up the fix
    }
}