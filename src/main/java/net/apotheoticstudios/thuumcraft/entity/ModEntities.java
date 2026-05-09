package net.apotheoticstudios.thuumcraft.entity;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.entity.custom.DraugrEntity;
import net.apotheoticstudios.thuumcraft.entity.custom.GiantEntity;
import net.apotheoticstudios.thuumcraft.entity.custom.SkeeverEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Thuumcraft.MOD_ID);

    public static final RegistryObject<EntityType<DraugrEntity>> DRAUGR =
            ENTITY_TYPES.register("draugr", () -> EntityType.Builder.of(DraugrEntity::new, MobCategory.MONSTER)
                    .sized(1f, 2f).build("draugr"));

    public static final RegistryObject<EntityType<GiantEntity>> GIANT =
            ENTITY_TYPES.register("giant", () -> EntityType.Builder.of(GiantEntity::new, MobCategory.MONSTER)
                    .sized(3f, 7f).build("giant"));

    public static final RegistryObject<EntityType<SkeeverEntity>> SKEEVER =
            ENTITY_TYPES.register("skeever", () -> EntityType.Builder.of(SkeeverEntity::new, MobCategory.MONSTER)
                    .sized(2f, 1f).build("skeever"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}