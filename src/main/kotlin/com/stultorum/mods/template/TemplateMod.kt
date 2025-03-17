package com.stultorum.mods.template // TODO: TEMPLATE: Change

import com.mojang.logging.LogUtils
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent

// TODO: TEMPLATE: Don't forget to change the package for mixins
// TODO: TEMPLATE: Don't forget to change the name of template.mixins.json and the referenced package

@Mod(TemplateMod.MODID)
class TemplateMod { // TODO: TEMPLATE: CHANGE
    companion object {
        const val MODID = "template" // TODO: TEMPLATE: Change
        private val LOGGER = LogUtils.getLogger()
    }

    private fun commonSetup(event: FMLCommonSetupEvent) {
        LOGGER.info("Template mod loaded: number is {}", TemplateConfig.exampleInt)
    }

    constructor(modEventBus: IEventBus, modContainer: ModContainer) {
        modEventBus.addListener(::commonSetup)

        modContainer.registerConfig(ModConfig.Type.COMMON, TemplateConfig.SPEC)
    }
}