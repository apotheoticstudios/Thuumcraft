package net.apotheoticstudios.thuumcraft.magic;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class IronSpellbooksItemDisabler {
    private static final String IRONS_SPELLBOOKS_MOD_ID = "irons_spellbooks";
    private static final int INVENTORY_PURGE_INTERVAL_TICKS = 20;

    private IronSpellbooksItemDisabler() {
    }

    public static void removeFromCreativeTab(BuildCreativeModeTabContentsEvent event) {
        List<ItemStack> disabledItems = new ArrayList<>();
        for (Map.Entry<ItemStack, CreativeModeTab.TabVisibility> entry : event.getEntries()) {
            if (isIronSpellbooksItem(entry.getKey())) {
                disabledItems.add(entry.getKey());
            }
        }

        disabledItems.forEach(stack -> event.getEntries().remove(stack));
    }

    @SubscribeEvent
    public static void removeDisabledItemEntities(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof ItemEntity itemEntity)) {
            return;
        }
        if (isIronSpellbooksItem(itemEntity.getItem())) {
            itemEntity.discard();
        }
    }

    @SubscribeEvent
    public static void preventDisabledItemPickup(EntityItemPickupEvent event) {
        if (isIronSpellbooksItem(event.getItem().getItem())) {
            event.getItem().discard();
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void purgeDisabledInventoryItems(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END
                || event.player.level().isClientSide()
                || event.player.tickCount % INVENTORY_PURGE_INTERVAL_TICKS != 0) {
            return;
        }

        Inventory inventory = event.player.getInventory();
        boolean changed = false;
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            if (isIronSpellbooksItem(inventory.getItem(slot))) {
                inventory.setItem(slot, ItemStack.EMPTY);
                changed = true;
            }
        }
        if (removeDisabledCurios(event.player)) {
            changed = true;
        }
        if (changed) {
            inventory.setChanged();
        }
    }

    @SubscribeEvent
    public static void preventDisabledItemUse(PlayerInteractEvent.RightClickItem event) {
        if (isIronSpellbooksItem(event.getItemStack())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void preventDisabledItemBlockUse(PlayerInteractEvent.RightClickBlock event) {
        if (isIronSpellbooksItem(event.getItemStack())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void preventDisabledItemLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        if (isIronSpellbooksItem(event.getEntity().getItemInHand(InteractionHand.MAIN_HAND))) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void preventDisabledItemAttacks(AttackEntityEvent event) {
        if (isIronSpellbooksItem(event.getEntity().getMainHandItem())) {
            event.setCanceled(true);
        }
    }

    private static boolean isIronSpellbooksItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        var itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return itemId != null && IRONS_SPELLBOOKS_MOD_ID.equals(itemId.getNamespace());
    }

    private static boolean removeDisabledCurios(net.minecraft.world.entity.player.Player player) {
        if (!ModList.get().isLoaded("curios")) {
            return false;
        }

        AtomicBoolean changed = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            IItemHandlerModifiable curios = handler.getEquippedCurios();
            for (int slot = 0; slot < curios.getSlots(); slot++) {
                if (isIronSpellbooksItem(curios.getStackInSlot(slot))) {
                    curios.setStackInSlot(slot, ItemStack.EMPTY);
                    changed.set(true);
                }
            }
        });
        return changed.get();
    }
}
