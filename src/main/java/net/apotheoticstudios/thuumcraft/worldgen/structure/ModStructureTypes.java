package net.apotheoticstudios.thuumcraft.worldgen.structure;

import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.apotheoticstudios.thuumcraft.worldgen.structure.custom.RiverFacingJigsawStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructureTypes {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, Thuumcraft.MOD_ID);

    public static final RegistryObject<StructureType<RiverFacingJigsawStructure>> RIVER_FACING_JIGSAW =
            STRUCTURE_TYPES.register("river_facing_jigsaw", () -> () -> RiverFacingJigsawStructure.CODEC);

    public static void register(IEventBus eventBus) {
        STRUCTURE_TYPES.register(eventBus);
    }
}
