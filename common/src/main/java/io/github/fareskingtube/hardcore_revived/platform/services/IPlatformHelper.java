package io.github.fareskingtube.hardcore_revived.platform.services;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.CreativeModeTab;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    CreativeModeTab.Builder creativeTabBuilder();

    /**
     * Sends a packet from the client to the server
     **/
    void sendPacketC2S(CustomPacketPayload payload);

    /**
     * Sends a packet from the server to the client
     **/
    void sendPacketS2C(ServerPlayer player, CustomPacketPayload payload);
}