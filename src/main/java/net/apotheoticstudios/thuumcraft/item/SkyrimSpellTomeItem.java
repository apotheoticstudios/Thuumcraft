package net.apotheoticstudios.thuumcraft.item;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class SkyrimSpellTomeItem extends Item {
    private final Supplier<? extends AbstractSpell> spell;

    public SkyrimSpellTomeItem(Supplier<? extends AbstractSpell> spell, Properties properties) {
        super(properties);
        this.spell = spell;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResultHolder.success(stack);
        }

        AbstractSpell tomeSpell = spell.get();
        SyncedSpellData spellData = MagicData.getPlayerMagicData(player).getSyncedData();
        if (spellData.isSpellLearned(tomeSpell)) {
            player.displayClientMessage(Component.translatable("message.thuumcraft.spell_tome.already_known",
                    tomeSpell.getDisplayName(player)), true);
            level.playSound(null, player.blockPosition(), SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 0.65F, 0.9F);
            return InteractionResultHolder.fail(stack);
        }

        spellData.learnSpell(tomeSpell);
        player.awardStat(Stats.ITEM_USED.get(this));
        player.displayClientMessage(Component.translatable("message.thuumcraft.spell_tome.learned",
                tomeSpell.getDisplayName(player)), true);
        level.playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.75F, 1.25F);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        AbstractSpell tomeSpell = spell.get();
        tooltip.add(Component.translatable("tooltip.thuumcraft.spell_tome.teaches",
                tomeSpell.getDisplayName(null)).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.thuumcraft.spell_tome.rank",
                tomeSpell.getSchoolType().getDisplayName()).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.add(Component.translatable("tooltip.thuumcraft.spell_tome.consumed").withStyle(ChatFormatting.DARK_GRAY));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
