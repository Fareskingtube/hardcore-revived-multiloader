package io.github.fareskingtube.hardcore_revived.event;

import com.mojang.authlib.GameProfile;
import io.github.fareskingtube.hardcore_revived.Constants;
import io.github.fareskingtube.hardcore_revived.block.entity.custom.RevivalAltarBlockEntity;
import io.github.fareskingtube.hardcore_revived.item.ModItems;
import io.github.fareskingtube.hardcore_revived.persistent.DeadPlayersState;
import io.github.fareskingtube.hardcore_revived.persistent.QueuedPlayer;
import io.github.fareskingtube.hardcore_revived.persistent.RevivalQueueState;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EventHelpers {
    // On damaging a player past half health
    public static void handelDamage(Entity livingEntity, DamageSource damageSource) {
        if (livingEntity instanceof Player victim) {
            if (damageSource.getEntity() instanceof ServerPlayer killer && victim.getHealth() < victim.getMaxHealth() / 2) {
                if (killer.getMaxHealth() - 4 > 0) {
                    killer.sendSystemMessage(Component.translatable("misc." + Constants.MOD_ID + ".player_kill_warn").withStyle(ChatFormatting.YELLOW));
                }
            }
        }
    }


    public static void handelDeath(Entity livingEntity, DamageSource damageSource) {
        if (livingEntity instanceof ServerPlayer player) {
            MinecraftServer server = player.getServer();
            if (server == null) return;
            DeadPlayersState.get(server).addDeadPlayer(new GameProfile(player.getUUID(), player.getScoreboardName()));
        }

        // CommonConfig config = CommonConfig.HANDLER.instance();
        if (damageSource.getEntity() instanceof ServerPlayer killer) {
            if (livingEntity instanceof ServerPlayer) {
                // TODO: Idea: Revive the victim instead of making the killer lose health
                Level world = livingEntity.level();
                AttributeInstance maxHealth = killer.getAttribute(Attributes.MAX_HEALTH);
                if (maxHealth != null && maxHealth.getValue() - 4 > 0) {
                    killer.sendSystemMessage(Component.translatable("misc." + Constants.MOD_ID + ".player_kill").withStyle(ChatFormatting.RED));
                    EntityType.LIGHTNING_BOLT.spawn((ServerLevel) world, killer.blockPosition(), MobSpawnType.TRIGGERED);
                    maxHealth.setBaseValue(maxHealth.getValue() - 20);
                }
            }
            if (livingEntity instanceof AgeableMob victim && killer.getMainHandItem().getItem() == ModItems.BUTCHER_KNIFE) {
                int count = victim.getRandom().nextIntBetweenInclusive(1, 3);
                Containers.dropItemStack(
                        victim.level(),
                        victim.getX(), victim.getY(), victim.getZ(),
                        new ItemStack(ModItems.BLOOD, count)
                );
            }
        }
    }

    // On player join
    public static void handelJoin(Player player) {
        MinecraftServer server = player.getServer();

        if (server == null) return;

        RevivalQueueState state = RevivalQueueState.get(server);

        QueuedPlayer queuedPlayer = state.getPlayer(player.getUUID());

        if (queuedPlayer == null) return;

        ServerLevel world = server.getLevel(queuedPlayer.world());

        if (world == null) return;

        if (world.getBlockEntity(queuedPlayer.pos()) instanceof RevivalAltarBlockEntity revivalAltarBlockEntity) {
            if (revivalAltarBlockEntity.isMultiblock(world, revivalAltarBlockEntity.getBlockPos())) {
                revivalAltarBlockEntity.revivePlayer();
            }
        }
    }
}
