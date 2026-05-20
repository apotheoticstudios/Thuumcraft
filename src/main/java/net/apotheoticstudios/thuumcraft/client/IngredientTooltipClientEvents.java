package net.apotheoticstudios.thuumcraft.client;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.item.IngredientItem;
import net.apotheoticstudios.thuumcraft.item.IngredientKnowledge;
import net.apotheoticstudios.thuumcraft.item.ModFoods;
import net.apotheoticstudios.thuumcraft.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID, value = Dist.CLIENT)
public final class IngredientTooltipClientEvents {
    private IngredientTooltipClientEvents() {
    }

    @SubscribeEvent
    public static void appendVanillaIngredientTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()
                || stack.getItem() instanceof IngredientItem
                || !stack.is(ModTags.Items.INGREDIENT)) {
            return;
        }

        String ingredientId = ModFoods.getIngredientId(stack);
        if (ingredientId == null || !IngredientKnowledge.isKnownClient(ingredientId)) {
            return;
        }

        for (Component effectName : ModFoods.getIngredientEffectNames(ingredientId,
                IngredientKnowledge.knownEffectCountClient(ingredientId))) {
            event.getToolTip().add(Component.translatable("tooltip.thuumcraft.ingredient_effect", effectName)
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
