package net.apotheoticstudios.thuumcraft.event;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.world.item.BowItem;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class BowEvents {
    private BowEvents() {
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void requireFullBowDraw(ArrowLooseEvent event) {
        if (event.getEntity().level().isClientSide()
                || event.isCanceled()
                || !(event.getBow().getItem() instanceof BowItem)) {
            return;
        }

        if (BowItem.getPowerForTime(event.getCharge()) < 1.0F) {
            event.setCharge(-1);
            event.setCanceled(true);
        }
    }
}
