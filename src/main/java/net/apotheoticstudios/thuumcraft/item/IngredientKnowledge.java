package net.apotheoticstudios.thuumcraft.item;

import net.apotheoticstudios.thuumcraft.network.ClientboundKnownIngredientEffectsPacket;
import net.apotheoticstudios.thuumcraft.network.ModMessages;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class IngredientKnowledge {
    private static final String KNOWN_EFFECTS_TAG = "thuumcraft_known_ingredient_effects";
    private static final Set<String> CLIENT_KNOWN_EFFECTS = new HashSet<>();

    public static void discover(ServerPlayer player, String ingredientId) {
        discover(player, ingredientId, 1);
    }

    public static void discover(ServerPlayer player, String ingredientId, int effectCount) {
        Set<String> knownEffects = getKnownEffects(player);
        boolean changed = knownEffects.add(ingredientId);
        for (int effectIndex = 0; effectIndex < Math.max(1, effectCount); effectIndex++) {
            changed |= knownEffects.add(effectKey(ingredientId, effectIndex));
        }
        if (changed) {
            saveKnownEffects(player, knownEffects);
            sync(player);
        }
    }

    public static void sync(ServerPlayer player) {
        ModMessages.sendToPlayer(new ClientboundKnownIngredientEffectsPacket(getKnownEffects(player)), player);
    }

    public static void copy(Player original, Player clone) {
        Set<String> knownEffects = getKnownEffects(original);
        if (!knownEffects.isEmpty()) {
            saveKnownEffects(clone, knownEffects);
        }
    }

    public static boolean isKnownClient(String ingredientId) {
        return CLIENT_KNOWN_EFFECTS.contains(ingredientId) || CLIENT_KNOWN_EFFECTS.contains(effectKey(ingredientId, 0));
    }

    public static int knownEffectCountClient(String ingredientId) {
        int count = CLIENT_KNOWN_EFFECTS.contains(ingredientId) ? 1 : 0;
        for (int effectIndex = 0; effectIndex < 4; effectIndex++) {
            if (CLIENT_KNOWN_EFFECTS.contains(effectKey(ingredientId, effectIndex))) {
                count = Math.max(count, effectIndex + 1);
            }
        }
        return count;
    }

    public static void markKnownClient(String ingredientId) {
        CLIENT_KNOWN_EFFECTS.add(ingredientId);
        CLIENT_KNOWN_EFFECTS.add(effectKey(ingredientId, 0));
    }

    public static void setKnownClient(Collection<String> ingredientIds) {
        CLIENT_KNOWN_EFFECTS.clear();
        CLIENT_KNOWN_EFFECTS.addAll(ingredientIds);
    }

    private static Set<String> getKnownEffects(Player player) {
        ListTag knownList = player.getPersistentData().getList(KNOWN_EFFECTS_TAG, Tag.TAG_STRING);
        Set<String> knownEffects = new TreeSet<>();
        knownList.forEach(tag -> knownEffects.add(tag.getAsString()));
        return knownEffects;
    }

    private static void saveKnownEffects(Player player, Collection<String> ingredientIds) {
        CompoundTag persistentData = player.getPersistentData();
        ListTag knownList = new ListTag();
        ingredientIds.stream().sorted().map(StringTag::valueOf).forEach(knownList::add);
        persistentData.put(KNOWN_EFFECTS_TAG, knownList);
    }

    private static String effectKey(String ingredientId, int effectIndex) {
        return ingredientId + "#" + effectIndex;
    }
}
