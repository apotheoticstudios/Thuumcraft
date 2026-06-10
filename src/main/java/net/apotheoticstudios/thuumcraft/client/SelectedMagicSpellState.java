package net.apotheoticstudios.thuumcraft.client;

import com.mojang.logging.LogUtils;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.UUID;

public final class SelectedMagicSpellState {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String STORAGE_FILE = "thuumcraft_selected_spells.properties";
    private static final String MAIN_HAND_SUFFIX = ".main_hand";
    private static final String OFF_HAND_SUFFIX = ".off_hand";

    private static ResourceLocation mainHandSpellId;
    private static ResourceLocation offHandSpellId;
    private static UUID loadedPlayerUuid;

    private SelectedMagicSpellState() {
    }

    public static void load(LocalPlayer player) {
        if (player == null) {
            return;
        }

        UUID playerUuid = player.getUUID();
        Properties properties = readProperties();
        mainHandSpellId = parsePersistedSpellId(properties.getProperty(key(playerUuid, MAIN_HAND_SUFFIX)));
        offHandSpellId = parsePersistedSpellId(properties.getProperty(key(playerUuid, OFF_HAND_SUFFIX)));
        loadedPlayerUuid = playerUuid;
    }

    public static void forgetLoadedPlayer() {
        loadedPlayerUuid = null;
    }

    public static void select(AbstractSpell spell, InteractionHand hand) {
        if (spell == null || spell == SpellRegistry.none()) {
            clear(hand);
            return;
        }

        if (hand == InteractionHand.OFF_HAND) {
            offHandSpellId = spell.getSpellResource();
        } else {
            mainHandSpellId = spell.getSpellResource();
        }
        save();
    }

    public static void clear() {
        mainHandSpellId = null;
        offHandSpellId = null;
        save();
    }

    public static void clear(InteractionHand hand) {
        if (hand == InteractionHand.OFF_HAND) {
            offHandSpellId = null;
        } else {
            mainHandSpellId = null;
        }
        save();
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

    private static void save() {
        UUID playerUuid = currentPlayerUuid();
        if (playerUuid == null) {
            return;
        }

        Properties properties = readProperties();
        writeSpellId(properties, key(playerUuid, MAIN_HAND_SUFFIX), mainHandSpellId);
        writeSpellId(properties, key(playerUuid, OFF_HAND_SUFFIX), offHandSpellId);
        writeProperties(properties);
        loadedPlayerUuid = playerUuid;
    }

    private static UUID currentPlayerUuid() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            return minecraft.player.getUUID();
        }
        return loadedPlayerUuid;
    }

    private static String key(UUID playerUuid, String suffix) {
        return playerUuid + suffix;
    }

    private static void writeSpellId(Properties properties, String key, ResourceLocation spellId) {
        if (spellId == null) {
            properties.remove(key);
        } else {
            properties.setProperty(key, spellId.toString());
        }
    }

    private static ResourceLocation parsePersistedSpellId(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        ResourceLocation spellId = ResourceLocation.tryParse(value);
        if (spellId == null) {
            return null;
        }

        AbstractSpell spell = SpellRegistry.getSpell(spellId);
        return spell == null || spell == SpellRegistry.none() ? null : spellId;
    }

    private static Properties readProperties() {
        Properties properties = new Properties();
        Path path = storagePath();
        if (!Files.isRegularFile(path)) {
            return properties;
        }

        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
        } catch (IOException exception) {
            LOGGER.warn("Failed to load persisted Skyrim hand spell selections from {}", path, exception);
        }
        return properties;
    }

    private static void writeProperties(Properties properties) {
        Path path = storagePath();
        try {
            Files.createDirectories(path.getParent());
            try (OutputStream output = Files.newOutputStream(path)) {
                properties.store(output, "Thu'umcraft selected Skyrim magic hand spells");
            }
        } catch (IOException exception) {
            LOGGER.warn("Failed to save persisted Skyrim hand spell selections to {}", path, exception);
        }
    }

    private static Path storagePath() {
        return Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve(STORAGE_FILE);
    }
}
