package io.github.fareskingtube.hardcore_revived.util;


import io.github.fareskingtube.hardcore_revived.Constants;
import io.github.fareskingtube.hardcore_revived.component.ModDataComponentTypes;
import io.github.fareskingtube.hardcore_revived.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class ModModelPredicates {
    public static void registerModelPredicates() {
        ItemProperties.register(ModItems.HEART_INJECTOR,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "has_heart"),
                (stack, world, entity, seed) -> {
                    Boolean hasHeart = stack.get(ModDataComponentTypes.HAS_HEART);
                    return hasHeart != null && hasHeart ? 1f : 0f;
                }
        );
    }
}
