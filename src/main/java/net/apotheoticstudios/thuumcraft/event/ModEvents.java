package net.apotheoticstudios.thuumcraft.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.item.IngredientKnowledge;
import net.apotheoticstudios.thuumcraft.item.ModItems;
import net.apotheoticstudios.thuumcraft.skill.SkillPerk;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber (modid = Thuumcraft.MOD_ID)
public class ModEvents {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            IngredientKnowledge.sync(player);
            SkillPerk.sync(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        IngredientKnowledge.copy(event.getOriginal(), event.getEntity());
        if (event.getEntity() instanceof ServerPlayer player) {
            SkillPerk.copy(event.getOriginal(), player);
            IngredientKnowledge.sync(player);
        }
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == VillagerProfession.WEAPONSMITH) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

        // Silver Ingots

            // Level 1
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(ModItems.SEPTIM.get(), 50),
                    new ItemStack(ModItems.SILVER_INGOT.get(), 1),
                    10, 8, 0.02f));

            // Level 2
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(ModItems.SEPTIM.get(), 40),
                    new ItemStack(ModItems.SILVER_INGOT.get(), 1),
                    10, 9, 0.02f));

            // Level 3
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(ModItems.SEPTIM.get(), 30),
                    new ItemStack(ModItems.SILVER_INGOT.get(), 1),
                    10, 10, 0.02f));

            // Level 4
            trades.get(4).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(ModItems.SEPTIM.get(), 20),
                    new ItemStack(ModItems.SILVER_INGOT.get(), 1),
                    10, 11, 0.02f));

            // Level 5
            trades.get(5).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(ModItems.SEPTIM.get(), 10),
                    new ItemStack(ModItems.SILVER_INGOT.get(), 1),
                    10, 12, 0.02f));


        }


    }

}
