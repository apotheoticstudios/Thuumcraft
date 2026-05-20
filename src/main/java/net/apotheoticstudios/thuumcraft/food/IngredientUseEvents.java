package net.apotheoticstudios.thuumcraft.food;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.item.IngredientItem;
import net.apotheoticstudios.thuumcraft.item.IngredientKnowledge;
import net.apotheoticstudios.thuumcraft.item.ModFoods;
import net.apotheoticstudios.thuumcraft.util.ModTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID)
public final class IngredientUseEvents {
    private IngredientUseEvents() {
    }

    @SubscribeEvent
    public static void useVanillaIngredient(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()
                || stack.getItem() instanceof IngredientItem
                || !stack.is(ModTags.Items.INGREDIENT)) {
            return;
        }

        String ingredientId = ModFoods.getIngredientId(stack);
        if (ingredientId == null) {
            return;
        }

        event.setCancellationResult(InteractionResult.CONSUME);
        event.setCanceled(true);
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        player.getFoodData().eat(ModFoods.INGREDIENT.getNutrition(), ModFoods.INGREDIENT.getSaturationModifier());
        ModFoods.applyIngredientEffects(ingredientId, player);
        IngredientKnowledge.discover(player, ingredientId);
        player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
        player.swing(event.getHand(), true);
    }
}
