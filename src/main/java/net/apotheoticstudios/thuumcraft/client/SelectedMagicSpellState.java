package net.apotheoticstudios.thuumcraft.client;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

public final class SelectedMagicSpellState {
    private static ResourceLocation mainHandSpellId;
    private static ResourceLocation offHandSpellId;

    private SelectedMagicSpellState() {
    }

    public static void select(AbstractSpell spell, InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND) {
            offHandSpellId = spell.getSpellResource();
        } else {
            mainHandSpellId = spell.getSpellResource();
        }
    }

    public static void clear() {
        mainHandSpellId = null;
        offHandSpellId = null;
    }

    public static void clear(InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND) {
            offHandSpellId = null;
        } else {
            mainHandSpellId = null;
        }
    }

    public static ResourceLocation selectedSpellId(InteractionHand hand) {
        return hand == InteractionHand.OFF_HAND ? offHandSpellId : mainHandSpellId;
    }

    public static AbstractSpell selectedSpell(InteractionHand hand) {
        ResourceLocation spellId = selectedSpellId(hand);
        return spellId == null ? null : SpellRegistry.getSpell(spellId);
    }

    public static boolean isSelected(AbstractSpell spell, InteractionHand hand) {
        ResourceLocation spellId = selectedSpellId(hand);
        return spellId != null && spellId.equals(spell.getSpellResource());
    }

    public static boolean isSelected(AbstractSpell spell) {
        return isSelected(spell, InteractionHand.MAIN_HAND) || isSelected(spell, InteractionHand.OFF_HAND);
    }
}
