package net.apotheoticstudios.thuumcraft.loot;

import net.apotheoticstudios.thuumcraft.Config;
import net.apotheoticstudios.thuumcraft.Thuumcraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = Thuumcraft.MOD_ID
)

public class AddRandomBookLoot {
    private static final ResourceLocation RANDOM_BOOK = new ResourceLocation("thuumcraft", "random_book");
    private static final ResourceLocation ZOMBIE = new ResourceLocation("minecraft", "entities/zombie");
    private static final ResourceLocation SKELETON = new ResourceLocation("minecraft", "entities/skeleton");
    private static final ResourceLocation PILLAGER = new ResourceLocation("minecraft", "entities/pillager");
    private static final ResourceLocation WITCH = new ResourceLocation("minecraft", "entities/witch");
    private static final ResourceLocation SHIPWRECK_TREASURE = new ResourceLocation("minecraft", "chests/shipwreck_treasure");
    private static final ResourceLocation SHIPWRECK_SUPPLY = new ResourceLocation("minecraft", "chests/shipwreck_supply");
    private static final ResourceLocation DUNGEON = new ResourceLocation("minecraft", "chests/simple_dungeon");
    private static final ResourceLocation DESERT = new ResourceLocation("minecraft", "chests/desert_pyramid");
    private static final ResourceLocation JUNGLE = new ResourceLocation("minecraft", "chests/jungle_temple");
    private static final ResourceLocation WOODLAND = new ResourceLocation("minecraft", "chests/woodland_mansion");
    private static final ResourceLocation ANCIENT = new ResourceLocation("minecraft", "chests/ancient_city");
    private static final ResourceLocation MINESHAFT = new ResourceLocation("minecraft", "chests/abandoned_mineshaft");
    private static final ResourceLocation NETHER = new ResourceLocation("minecraft", "chests/nether_bridge");
    private static final ResourceLocation IGLOO = new ResourceLocation("minecraft", "chests/igloo_chest");
    private static final ResourceLocation LIBRARY = new ResourceLocation("minecraft", "chests/stronghold_library");
    private static final ResourceLocation BASTION_BRIDGE = new ResourceLocation("minecraft", "chests/bastion_bridge");
    private static final ResourceLocation BASTION_OTHER = new ResourceLocation("minecraft", "chests/bastion_other");
    private static final ResourceLocation BASTION_TREASURE = new ResourceLocation("minecraft", "chests/bastion_treasure");
    private static final ResourceLocation FISHING = new ResourceLocation("minecraft", "gameplay/fishing/junk");

    @SubscribeEvent
    public static void lootLoad(LootTableLoadEvent event) {
        ResourceLocation name = event.getName();
        if (!name.equals(ZOMBIE) && !name.equals(SKELETON) && !name.equals(PILLAGER) && !name.equals(WITCH)) {
            if (!name.equals(SHIPWRECK_TREASURE) && !name.equals(SHIPWRECK_SUPPLY) && !name.equals(DUNGEON) && !name.equals(DESERT) && !name.equals(JUNGLE) && !name.equals(WOODLAND) && !name.equals(ANCIENT) && !name.equals(MINESHAFT) && !name.equals(NETHER) && !name.equals(IGLOO) && !name.equals(LIBRARY) && !name.equals(BASTION_BRIDGE) && !name.equals(BASTION_OTHER) && !name.equals(BASTION_TREASURE)) {
                if (name.equals(FISHING)) {
                    LootPool pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(RANDOM_BOOK).setWeight(1).when(LootItemRandomChanceCondition.randomChance(((Double)Config.FISHING_DROP_CHANCE.get()).floatValue()))).build();
                    event.getTable().addPool(pool);
                }
            } else {
                LootPool pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(RANDOM_BOOK).setWeight(1).when(LootItemRandomChanceCondition.randomChance(((Double)Config.CHEST_DROP_CHANCE.get()).floatValue()))).build();
                event.getTable().addPool(pool);
            }
        } else {
            LootPool pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(RANDOM_BOOK).setWeight(1).when(LootItemRandomChanceCondition.randomChance(((Double)Config.MOB_DROP_CHANCE.get()).floatValue()))).build();
            event.getTable().addPool(pool);
        }

    }

}