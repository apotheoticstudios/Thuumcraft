package net.apotheoticstudios.thuumcraft.magic;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public final class IronLearnedSpellHelper {
    private static final String LEARNED_SPELLS = "learnedSpells";

    private IronLearnedSpellHelper() {
    }

    public static Set<ResourceLocation> learnedSpellIds(MagicData magicData) {
        if (magicData == null) {
            return Set.of();
        }
        return learnedSpellIds(magicData.getSyncedData());
    }

    public static Set<ResourceLocation> learnedSpellIds(SyncedSpellData syncedSpellData) {
        if (syncedSpellData == null) {
            return Set.of();
        }

        CompoundTag tag = new CompoundTag();
        try {
            syncedSpellData.saveNBTData(tag);
        } catch (RuntimeException exception) {
            return Set.of();
        }
        ListTag learnedSpells = tag.getList(LEARNED_SPELLS, Tag.TAG_STRING);
        Set<ResourceLocation> result = new HashSet<>();
        for (int i = 0; i < learnedSpells.size(); i++) {
            ResourceLocation spellId = ResourceLocation.tryParse(learnedSpells.getString(i));
            if (spellId != null) {
                result.add(spellId);
            }
        }
        return result;
    }

    public static boolean hasLearned(MagicData magicData, AbstractSpell spell) {
        if (spell == null || spell.getSpellResource() == null) {
            return false;
        }
        return learnedSpellIds(magicData).contains(spell.getSpellResource());
    }
}
