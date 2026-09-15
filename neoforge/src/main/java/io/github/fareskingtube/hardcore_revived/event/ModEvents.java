package io.github.fareskingtube.hardcore_revived.event;

import io.github.fareskingtube.hardcore_revived.Constants;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Post event) {
        EventHelpers.handelDamage(event.getEntity(), event.getSource());
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        EventHelpers.handelDeath(event.getEntity(), event.getSource());
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        EventHelpers.handelJoin(event.getEntity());
    }
}
