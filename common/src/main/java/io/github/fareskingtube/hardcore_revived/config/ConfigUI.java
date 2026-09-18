package io.github.fareskingtube.hardcore_revived.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import io.github.fareskingtube.hardcore_revived.Constants;
import io.github.fareskingtube.hardcore_revived.config.helper.ConfigTranslations;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


public class ConfigUI {
    public static Screen createConfigScreen(Screen parent) {
        ClientConfig instance = ClientConfig.HANDLER.instance();
        CommonConfig commonInstance = CommonConfig.HANDLER.instance();
        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("config." + Constants.MOD_ID + ".title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("config." + Constants.MOD_ID + ".category.client"))
                        .tooltip(Component.translatable("config." + Constants.MOD_ID + ".category.description.client"))
                        .group(OptionGroup.createBuilder()
                                .name(ConfigTranslations.getGroupName("player_selection_screen"))
                                .description(OptionDescription.of(ConfigTranslations.getGroupDescription("player_selection_screen")))
                                .option(Option.<Boolean>createBuilder()
                                        .name(ConfigTranslations.getOptionName("apply_blur"))
                                        .description(OptionDescription.of(ConfigTranslations.getOptionDescription("apply_blur")))
                                        .binding(true, () -> instance.isApplyBlur, val -> instance.isApplyBlur = val)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(ConfigTranslations.getOptionName("apply_darkening"))
                                        .description(OptionDescription.of(ConfigTranslations.getOptionDescription("apply_darkening")))
                                        .binding(true, () -> instance.isApplyDarkening, val -> instance.isApplyDarkening = val)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("config." + Constants.MOD_ID + ".category.common"))
                        .tooltip(Component.translatable("config." + Constants.MOD_ID + ".category.description.common"))
                        .group(OptionGroup.createBuilder()
                                .name(ConfigTranslations.getGroupName("items"))
                                .option(Option.<Integer>createBuilder()
                                        .name(ConfigTranslations.getOptionName("heartActivationTime"))
                                        .description(OptionDescription.of(ConfigTranslations.getOptionDescription("heartActivationTime")))
                                        .binding(20, () -> commonInstance.heartActivationTime,
                                                val -> commonInstance.heartActivationTime = val)
                                        .controller(opt -> IntegerFieldControllerBuilder.create(opt)
                                                .min(1)
                                                .formatValue(val -> Component.literal(val + "t")))
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(ConfigTranslations.getOptionName("killPenalty"))
                                        .description(OptionDescription.of(ConfigTranslations.getOptionDescription("killPenalty")))
                                        .binding(4, () -> commonInstance.killPenalty,
                                                val -> commonInstance.killPenalty = val)
                                        .controller(opt -> IntegerFieldControllerBuilder.create(opt)
                                                .min(0)
                                                .formatValue(val -> Component.literal(val + "h")))
                                        .build())
                                .build())
                        .build())
                .save(() -> {
                    ClientConfig.save();
                    CommonConfig.save();
                })
                .build()
                .generateScreen(parent);
    }
}

