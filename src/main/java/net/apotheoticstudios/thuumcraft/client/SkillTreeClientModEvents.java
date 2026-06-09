package net.apotheoticstudios.thuumcraft.client;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SkillTreeClientModEvents {
    private SkillTreeClientModEvents() {
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(SkyrimTweenMenuClientEvents.OPEN_TWEEN_MENU);
        event.register(SkillTreeClientEvents.OPEN_SKILL_TREES);
        event.register(SkillPerkClientEvents.EAGLE_EYE_ZOOM);
    }
}
