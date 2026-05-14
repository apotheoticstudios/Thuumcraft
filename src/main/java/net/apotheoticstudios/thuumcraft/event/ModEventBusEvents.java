package net.apotheoticstudios.thuumcraft.event;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.attribute.ModAttributes;
import net.apotheoticstudios.thuumcraft.entity.ModEntities;
import net.apotheoticstudios.thuumcraft.entity.custom.DraugrEntity;
import net.apotheoticstudios.thuumcraft.entity.custom.GiantEntity;
import net.apotheoticstudios.thuumcraft.entity.custom.SkeeverEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Thuumcraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    private static final int SKEEVER_MIN_BLOCKS_BELOW_SURFACE = 4;
    private static final ResourceLocation ATTRIBUTESLIB_ARROW_DAMAGE = ResourceLocation.fromNamespaceAndPath("attributeslib", "arrow_damage");
    private static final ResourceLocation ATTRIBUTESLIB_ARROW_VELOCITY = ResourceLocation.fromNamespaceAndPath("attributeslib", "arrow_velocity");

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DRAUGR.get(), DraugrEntity.createAttributes().build());
        event.put(ModEntities.GIANT.get(), GiantEntity.createAttributes().build());
        event.put(ModEntities.SKEEVER.get(), SkeeverEntity.createAttributes().build());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addPlayerAttributes(EntityAttributeModificationEvent event) {
        ModAttributes.customAttributes().forEach(attribute -> event.add(EntityType.PLAYER, attribute.get()));
        addOptionalPlayerAttribute(event, ATTRIBUTESLIB_ARROW_DAMAGE);
        addOptionalPlayerAttribute(event, ATTRIBUTESLIB_ARROW_VELOCITY);
    }

    private static void addOptionalPlayerAttribute(EntityAttributeModificationEvent event, ResourceLocation attributeId) {
        Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(attributeId);
        if (attribute != null && !event.has(EntityType.PLAYER, attribute)) {
            event.add(EntityType.PLAYER, attribute);
        }
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.GIANT.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkAnyLightMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ModEntities.SKEEVER.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, level, spawnType, pos, random) -> {
                    int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos.getX(), pos.getZ());
                    return pos.getY() <= surfaceY - SKEEVER_MIN_BLOCKS_BELOW_SURFACE
                            && Monster.checkMonsterSpawnRules(entityType, level, spawnType, pos, random);
                },
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

}
