package net.apotheoticstudios.thuumcraft.datagen.loot;

import net.apotheoticstudios.thuumcraft.entity.ModEntities;
import net.apotheoticstudios.thuumcraft.item.ModItems;
import net.apotheoticstudios.thuumcraft.loot.AddItemModifier;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;

public class ModEntityLootTables extends EntityLootSubProvider {
    public ModEntityLootTables() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        this.add(ModEntities.DRAUGR.get(), LootTable.lootTable()
                .withPool(singleItem(Items.BONE_MEAL)
                        .when(LootItemRandomChanceCondition.randomChance(0.25F))));
        this.add(ModEntities.GIANT.get(), LootTable.lootTable()
                .withPool(singleItem(ModItems.GIANTS_TOE.get())));
        this.add(ModEntities.SKEEVER.get(), LootTable.lootTable()
                .withPool(singleItem(ModItems.SKEEVER_TAIL.get())));
    }

    private static LootPool.Builder singleItem(ItemLike item) {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F))
                .add(LootItem.lootTableItem(item));
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return ModEntities.ENTITY_TYPES.getEntries().stream().map(RegistryObject::get);
    }
}
