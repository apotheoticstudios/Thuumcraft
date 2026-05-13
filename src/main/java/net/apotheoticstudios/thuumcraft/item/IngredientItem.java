package net.apotheoticstudios.thuumcraft.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class IngredientItem extends Item {
    private final String ingredientId;

    public IngredientItem(String ingredientId, Properties properties) {
        super(properties);
        this.ingredientId = ingredientId;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        ItemStack result = super.finishUsingItem(stack, level, entity);

        if (!level.isClientSide) {
            ModFoods.applyIngredientEffects(ingredientId, entity);
            if (entity instanceof ServerPlayer player) {
                IngredientKnowledge.discover(player, ingredientId);
            }
        } else {
            IngredientKnowledge.markKnownClient(ingredientId);
        }

        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        if (IngredientKnowledge.isKnownClient(ingredientId)) {
            for (Component effectName : ModFoods.getIngredientEffectNames(ingredientId,
                    IngredientKnowledge.knownEffectCountClient(ingredientId))) {
                tooltip.add(Component.translatable("tooltip.thuumcraft.ingredient_effect", effectName)
                        .withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
