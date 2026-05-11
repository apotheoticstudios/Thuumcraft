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
        Set<String> knownEffects = getKnownEffects(player);
        if (knownEffects.add(ingredientId)) {
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
        return CLIENT_KNOWN_EFFECTS.contains(ingredientId);
    }

    public static void markKnownClient(String ingredientId) {
        CLIENT_KNOWN_EFFECTS.add(ingredientId);
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
}
