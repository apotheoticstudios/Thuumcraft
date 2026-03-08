package net.apotheoticstudios.thuumcraft.entity;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.entity.custom.DraugrEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Thuumcraft.MOD_ID);

    public static final RegistryObject<EntityType<DraugrEntity>> DRAUGR =
            ENTITY_TYPES.register("draugr", () -> EntityType.Builder.of(DraugrEntity::new, MobCategory.MONSTER)
                    .sized(2f, 2f).build("draugr"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}