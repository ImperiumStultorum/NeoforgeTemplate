package com.stultorum.mods.template

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.config.ModConfigEvent
import net.neoforged.neoforge.common.ModConfigSpec

@EventBusSubscriber(modid = TemplateMod.MODID, bus = EventBusSubscriber.Bus.MOD)
object TemplateConfig { // TODO: TEMPLATE: CHANGE
    private val EXAMPLE_INT: ModConfigSpec.IntValue

    var exampleInt: Int = 0
        private set

    val SPEC: ModConfigSpec

    @SubscribeEvent
    fun onLoad(event: ModConfigEvent) {
        exampleInt = EXAMPLE_INT.get()
    }

    init {
        val builder = ModConfigSpec.Builder()

        EXAMPLE_INT = builder.comment("example config").defineInRange("number", 0, 0, 100)

        SPEC = builder.build()
    }
}