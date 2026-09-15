package io.github.fareskingtube.hardcore_revived.persistent;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record QueuedPlayer(GameProfile player, BlockPos pos, ResourceKey<Level> world) {
}
